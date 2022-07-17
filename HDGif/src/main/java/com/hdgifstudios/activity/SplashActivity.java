package com.hdgifstudios.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.hdgifstudios.R;
import com.hdgifstudios.core.Applications;
import com.hdgifstudios.util.Constant;

import java.io.IOException;


public class SplashActivity extends AppCompatActivity {

    private Applications applications;
    private boolean statusIklan = true;
    int hitung = 0;
    int loadIklanBerapaKali = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Constant.TEMP_ALL_CONTENT == null) {
            try {
                Constant.TEMP_ALL_CONTENT = Constant.LIST_ALL_CONTENT(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        applications = (Applications) getApplication();
        applications.initInterstitial();
        applications.loadIntersTitial();
        applications.getInterstitial().setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                hitung++;
                //Log.d("iklan", "gagal "+ String.valueOf(hitung));
                if (hitung < loadIklanBerapaKali) {
                    if (statusIklan) {
                        applications.loadIntersTitial();
                    }
                }
                if (hitung == loadIklanBerapaKali) {
                    if (statusIklan) {
                        statusIklan = false;
                        applications.setStatusIklan(Constant.FAILED_LOAD_ADS);
                        bukaActivity();
                    }
                }
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                if (statusIklan) {
                    //Log.d("iklan", "berhasil");
                    statusIklan = false;
                    applications.setStatusIklan(Constant.SUCCESFULLY_LOAD_ADS);
                    bukaActivity();
                }
                super.onAdLoaded();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (statusIklan) {
                    statusIklan = false;
                    applications.setStatusIklan(Constant.FAILED_LOAD_ADS);
                    bukaActivity();
                }
            }
        }, 15000);
    }

    private void bukaActivity() {
        Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}