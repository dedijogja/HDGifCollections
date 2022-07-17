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

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.hdgifstudios.R;
import com.hdgifstudios.core.Applications;
import com.hdgifstudios.core.CoreAppCompactActivity;
import com.hdgifstudios.util.Constant;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.startapp.android.publish.ads.banner.Banner;

import java.util.List;


public class GiphyActivity extends CoreAppCompactActivity {

    public static final String EXTRA_API_KEY = "api_key";
    public static final String EXTRA_SIZE_LIMIT = "size_limit";

    private GiphyApiHelper helper;

    private RecyclerView recycler;
    private GiphyAdapter adapter;

    private View progressSpinner;
    private MaterialSearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new GiphyApiHelper("f48a483679cd447389c44cad72e9bfeb", GiphyApiHelper.NO_SIZE_LIMIT);

        setContentView(R.layout.giffy_search_activity);


        final Applications applications = (Applications) getApplication();
        String status = applications.getStatusIklan();
        if(status.equals(Constant.SUCCESFULLY_LOAD_ADS)){
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.adSearhGiphyBanner);
            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(getKodeBanner());
            adView.loadAd(new AdRequest.Builder().build());
            adContainer.addView(adView);
        }else{
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.adSearhGiphyBanner);
            Banner startAppBanner = new Banner(this);
            linearLayout.addView(startAppBanner);
        }


        recycler = (RecyclerView) findViewById(R.id.recycler_view);
        progressSpinner = findViewById(R.id.list_progress);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                executeQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                //setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadTrending();
            }
        }, 500);
    }

    @Override
    public void onStart() {
        super.onStart();
        searchView.showSearch(false);
    }

    private void loadTrending() {
        progressSpinner.setVisibility(View.VISIBLE);
        helper.trends(new GiphyApiHelper.Callback() {
            @Override
            public void onResponse(List<GiphyApiHelper.Gif> gifs) {
                setAdapter(gifs);
            }
        });
    }

    private void executeQuery(String query) {
        progressSpinner.setVisibility(View.VISIBLE);
        dismissKeyboard();

        helper.search(query, new GiphyApiHelper.Callback() {
            @Override
            public void onResponse(List<GiphyApiHelper.Gif> gifs) {
                setAdapter(gifs);
            }
        });
    }

    private void setAdapter(List<GiphyApiHelper.Gif> gifs) {
        progressSpinner.setVisibility(View.GONE);
        adapter = new GiphyAdapter(gifs, new GiphyAdapter.Callback() {
            @Override
            public void onClick(final GiphyApiHelper.Gif item) {
                new DownloadGif(GiphyActivity.this, item.gifUrl).execute();
            }
        });

        int jumlahColumn = PreferenceManager.getDefaultSharedPreferences(this).getInt(Constant.PREF_SEARCH_COLUMN, 2);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, jumlahColumn);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
