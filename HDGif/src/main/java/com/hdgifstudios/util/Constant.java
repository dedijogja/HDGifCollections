package com.hdgifstudios.util;


import android.content.Context;
import android.net.Uri;

import java.io.IOException;

public class Constant {

    public static final String SUCCESFULLY_LOAD_ADS = "succesLoadAds";
    public static final String FAILED_LOAD_ADS = "failedLoadAds";

    public static final String INDEX_CODE = "indexcode";

    public static final String CONTENT_FOLDER = "gif";

    public static final int REQ_CODE_FOR_RESULT = 6;

    public static String[] LIST_ALL_CONTENT(Context context) throws IOException {
        return context.getAssets().list(CONTENT_FOLDER);
    }

    public static String[] TEMP_ALL_CONTENT = null;
    public static byte[][] LIST_GIFT_DECRYPTED = null;

    public static final String CODE_GIPHY = "3767521";
    public static Uri TEMP_DOWNLOAD_GIPHY = null;


    public static final String PREF_FOLDER_NAME = "preffoldername";
    public static final String PREF_COLLECTIONS_COLUMN = "prefcolumncount";
    public static final String PREF_FAVORITE_COLUMN = "preffavoritecolumn";
    public static final String PREF_SEARCH_COLUMN = "prefsearchcolumn";

}
