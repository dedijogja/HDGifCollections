package com.hdgifstudios.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.hdgifstudios.R;
import com.hdgifstudios.adapter.GifCollectionsAdapter;
import com.hdgifstudios.core.Applications;
import com.hdgifstudios.core.CoreAppCompactActivity;
import com.hdgifstudios.util.Constant;
import com.hdgifstudios.view.RecycleSpacing;
import com.startapp.android.publish.ads.banner.Banner;

public class GifCollectionsActivity extends CoreAppCompactActivity {

    private RecyclerView recyclerView;
    private GifCollectionsAdapter gifCollectionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_collections);

        final Applications applications = (Applications) getApplication();
        String status = applications.getStatusIklan();
        if(status.equals(Constant.SUCCESFULLY_LOAD_ADS)){
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.adsBannerCollections);
            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(getKodeBanner());
            adView.loadAd(new AdRequest.Builder().build());
            adContainer.addView(adView);
        }else{
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.adsBannerCollections);
            Banner startAppBanner = new Banner(this);
            linearLayout.addView(startAppBanner);
        }


        recyclerView = (RecyclerView) findViewById(R.id.recycleCollections);
        gifCollectionsAdapter = new GifCollectionsAdapter(this);

        int jumlahColumn = PreferenceManager.getDefaultSharedPreferences(this).getInt(Constant.PREF_COLLECTIONS_COLUMN, 2);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, jumlahColumn);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecycleSpacing(20, this));
        recyclerView.setAdapter(gifCollectionsAdapter);
        gifCollectionsAdapter.notifyDataSetChanged();



    }

}
