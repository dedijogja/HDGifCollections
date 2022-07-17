package com.hdgifstudios.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.hdgifstudios.R;
import com.hdgifstudios.activity.online.GiphyActivity;
import com.hdgifstudios.core.Applications;
import com.hdgifstudios.util.Constant;
import com.hdgifstudios.util.Native;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.ads.splash.SplashConfig;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.io.IOException;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final Applications applications = (Applications) getApplication();
        String status = applications.getStatusIklan();
        if(status.equals(Constant.FAILED_LOAD_ADS)) {
            StartAppSDK.init(this, new Native(this).getStartAppId(),false);
            if (!isNetworkConnected()) {
                StartAppAd.disableSplash();
            }else{
                StartAppAd.showSplash(this, savedInstanceState,
                        new SplashConfig()
                                .setTheme(SplashConfig.Theme.USER_DEFINED)
                                .setCustomScreen(R.layout.activity_splash)
                );
            }
        }

        if(status.equals(Constant.SUCCESFULLY_LOAD_ADS)){
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.adsBannerMenu);
            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(new Native(this).getAdBanner());
            adView.loadAd(new AdRequest.Builder().build());
            adContainer.addView(adView);
        }else{
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.adsBannerMenu);
            Banner startAppBanner = new Banner(this);
            linearLayout.addView(startAppBanner);
        }

        if(Constant.TEMP_ALL_CONTENT == null){
            try {
                Constant.TEMP_ALL_CONTENT = Constant.LIST_ALL_CONTENT(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.btnGifCollections:
                startActivity(new Intent(this, GifCollectionsActivity.class));
                break;
            case R.id.btnBookmarkedGif:
                startActivity(new Intent(this, FavoritActivity.class));
                break;
            case R.id.btnSetting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.btnSearchOnline:
                Intent intent = new Intent(this, GiphyActivity.class);
                startActivity(intent);
                break;
            case R.id.btnRate:
                try {
                    Intent intentrate = new Intent(Intent.ACTION_VIEW);
                    intentrate.setData(Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
                    startActivity(intentrate);
                }catch (Exception e){
                    Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id="+ getApplicationContext().getPackageName()));
                    startActivity(i);
                }
                break;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Exit Confirmation");
        alertDialog.setMessage("Sure want to close this app?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
