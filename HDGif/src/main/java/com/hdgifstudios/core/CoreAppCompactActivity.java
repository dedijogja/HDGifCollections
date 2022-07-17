package com.hdgifstudios.core;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.hdgifstudios.util.Constant;
import com.hdgifstudios.util.Native;
import com.startapp.android.publish.adsCommon.StartAppAd;

public class CoreAppCompactActivity extends AppCompatActivity{

    private String kodeBanner;
    private String kodeNativeMenu;
    private String kodeStartApp;
    private Context context;

    public String getKodeBanner() {
        return kodeBanner;
    }

    public String getKodeNativeMenu() {
        return kodeNativeMenu;
    }

    public String getKodeStartApp() {
        return kodeStartApp;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Native aNative = new Native(this);
        context = this;
        kodeBanner = aNative.getAdBanner();
        kodeNativeMenu = aNative.getAdNativeMenu();
        kodeStartApp = aNative.getStartAppId();
    }

    @Override
    public void startActivity(final Intent intent) {
        final Applications app = (Applications) getApplication();
        String status = app.getStatusIklan();
        if (status.equals(Constant.SUCCESFULLY_LOAD_ADS)) {
            if (!app.isBolehMenampilkanIklanHitung() || !app.isBolehMenampilkanIklanWaktu()
                    || !app.getInterstitial().isLoaded()) {
                start(intent);
            }
            app.getInterstitial().setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    start(intent);

                    app.loadIntersTitial();
                    super.onAdClosed();
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    if (app.getHitungFailed() < 2) {
                        app.loadIntersTitial();
                        app.setHitungFailed();
                    }
                    super.onAdFailedToLoad(i);
                }

                @Override
                public void onAdLoaded() {
                    app.setHitungFailed(0);
                    super.onAdLoaded();
                }
            });
            app.tampilkanInterstitial();
        } else {
            if (app.getPenghitungStartApp() == 0) {
                app.setPenghitungStartApp(1);
                start(intent);
                StartAppAd.showAd(context);
            } else {
                app.setPenghitungStartApp(0);
                start(intent);
            }
        }
    }

    private void start(Intent intent){
        super.startActivity(intent);
    }
}
