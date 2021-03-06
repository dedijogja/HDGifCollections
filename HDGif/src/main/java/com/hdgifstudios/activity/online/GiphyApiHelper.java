/*
 * Copyright (C) 2016 Jacob Klinker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hdgifstudios.activity.online;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Helper for working with Giphy data. To use, create a new object and pass in your api key and
 * max size, then call search() or trends().
 * <p>
 * new GiphyHelper(apiKey, 1024 * 1024)
 * .search(this);
 */
class GiphyApiHelper {

    public static final int NO_SIZE_LIMIT = -1;

    private String apiKey;
    private long maxSize;

    GiphyApiHelper(String apiKey, long maxSize) {
        this.apiKey = apiKey;
        this.maxSize = maxSize;
    }

    private static final String[] SIZE_OPTIONS = new String[]{
            "original", "downsized_medium", "fixed_height", "fixed_width", "fixed_height_small",
            /*"fixed_width_small",*/ "downsized_large", "downsized_medium", "downsized"
    };

    interface Callback {
        void onResponse(List<Gif> gifs);
    }

    void search(String query, Callback callback) {
        new SearchGiffy(apiKey, maxSize, query, callback).execute();
    }

    void trends(Callback callback) {
        new GiffyTrends(apiKey, maxSize, callback).execute();
    }

    private static class GiffyTrends extends SearchGiffy {

        GiffyTrends(String apiKey, long maxSize, Callback callback) {
            super(apiKey, maxSize, null, callback);
        }

        @Override
        protected String buildSearchUrl(String query) throws UnsupportedEncodingException {
            return "http://api.giphy.com/v1/gifs/trending?api_key=" + getApiKey();
        }
    }

    private static class SearchGiffy extends AsyncTask<Void, Void, List<Gif>> {

        private String apiKey;
        private long maxSize;
        private String query;
        private Callback callback;

        SearchGiffy(String apiKey, long maxSize, String query, Callback callback) {
            this.apiKey = apiKey;
            this.maxSize = maxSize;
            this.query = query;
            this.callback = callback;
        }

        String getApiKey() {
            return apiKey;
        }

        @Override
        protected List<Gif> doInBackground(Void... arg0) {
            List<Gif> gifList = new ArrayList<>();

            try {
                // create the connection
                URL urlToRequest = new URL(buildSearchUrl(query));
                HttpURLConnection urlConnection = (HttpURLConnection)
                        urlToRequest.openConnection();

                // create JSON object from content
                InputStream in = new BufferedInputStream(
                        urlConnection.getInputStream());
                JSONObject root = new JSONObject(getResponseText(in));
                JSONArray data = root.getJSONArray("data");

                try {
                    in.close();
                } catch (Exception e) { }

                try {
                    urlConnection.disconnect();
                } catch (Exception e) { }

                for (int i = 0; i < data.length(); i++) {
                    JSONObject gif = data.getJSONObject(i);
                    JSONObject images = gif.getJSONObject("images");
                    JSONObject originalStill = images.getJSONObject("original_still");
                    JSONObject originalSize = images.getJSONObject("original");
                    JSONObject downsized = null;

                    // get the highest quality that twitter can post (5 mb)
                    for (String size : SIZE_OPTIONS) {
                        downsized = images.getJSONObject(size);
                        Log.v("giphy", size + ": " + downsized.getString("size") + " bytes");

                        if (Long.parseLong(downsized.getString("size")) < maxSize ||
                                maxSize == NO_SIZE_LIMIT) {
                            break;
                        } else {
                            downsized = null;
                        }
                    }

                    if (downsized != null) {
                        gifList.add(
                                new Gif(originalStill.getString("url"),
                                        downsized.getString("url"),
                                        originalSize.getString("mp4"))
                        );
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return gifList;
        }

        @Override
        protected void onPostExecute(List<Gif> result) {
            if (callback != null) {
                callback.onResponse(result);
            }
        }

        protected String buildSearchUrl(String query) throws UnsupportedEncodingException {
            return "http://api.giphy.com/v1/gifs/search?" +
                    "q=" + URLEncoder.encode(query, "UTF-8") + "&" +
                    "limit=80&" +
                    "api_key=" + apiKey;
        }

        private String getResponseText(InputStream inStream) {
            return new Scanner(inStream).useDelimiter("\\A").next();
        }
    }

    static class Gif {
        String previewImage;
        String gifUrl;
        String mp4Url;

        Gif(String previewImage, String gifUrl, String mp4Url) {
            this.previewImage = URLDecoder.decode(previewImage);
            this.gifUrl = URLDecoder.decode(gifUrl);
            this.mp4Url = URLDecoder.decode(mp4Url);
        }
    }

}
