package com.hdgifstudios.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.hdgifstudios.R;
import com.hdgifstudios.action.ActionManagement;
import com.hdgifstudios.asyncronus.SingleDecryption;
import com.hdgifstudios.core.Applications;
import com.hdgifstudios.core.CoreAppCompactActivity;
import com.hdgifstudios.util.Constant;
import com.hdgifstudios.util.FavoritHelper;
import com.startapp.android.publish.ads.banner.Banner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class GifPreviewActivity extends CoreAppCompactActivity {

    private ImageView previewGif;
    private ProgressBar previewProgress;
    private int position;
    private SingleDecryption singleDecryption;
    private Context context;
    private byte[] gifBytes;
    private ActionManagement actionManagement;
    private ImageView imgFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_preview);
        context = this;


        final Applications applications = (Applications) getApplication();
        String status = applications.getStatusIklan();

        if(status.equals(Constant.SUCCESFULLY_LOAD_ADS)){
            NativeExpressAdView adViewAtas = new NativeExpressAdView(this);
            adViewAtas.setAdSize(new AdSize(AdSize.FULL_WIDTH, 132));
            adViewAtas.setAdUnitId(getKodeNativeMenu());
            adViewAtas.loadAd(new AdRequest.Builder().build());
            ((LinearLayout) findViewById(R.id.adsAtas)).addView(adViewAtas);
        }else{
            RelativeLayout.LayoutParams parameter = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            parameter.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            findViewById(R.id.adsAtas).setLayoutParams(parameter);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.adsAtas);
            Banner startAppBanner = new Banner(this);
            linearLayout.addView(startAppBanner);
        }

        if(getIntent().getStringExtra(Constant.INDEX_CODE)!=null){
            position = Integer.parseInt(getIntent().getStringExtra(Constant.INDEX_CODE));
        }

        initView();

        if(position == Integer.parseInt(Constant.CODE_GIPHY)){
            if(Constant.TEMP_DOWNLOAD_GIPHY!=null) {
                findViewById(R.id.btnFavoritUnfavorit).setVisibility(View.GONE);
                imgFavorite.setVisibility(View.GONE);
                previewGif.setVisibility(View.VISIBLE);
                previewProgress.setVisibility(View.GONE);
                Glide.with(context)
                        .load(Constant.TEMP_DOWNLOAD_GIPHY)
                        .into(previewGif);
                try {
                    InputStream iStream = getContentResolver().openInputStream(Constant.TEMP_DOWNLOAD_GIPHY);
                    gifBytes = getBytes(iStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            if(FavoritHelper.apakahSudahterbookmark(position, context)){
                imgFavorite.setImageResource(R.drawable.on);
            }else{
                imgFavorite.setImageResource(R.drawable.off);
            }

            if(Constant.TEMP_ALL_CONTENT == null){
                try {
                    Constant.TEMP_ALL_CONTENT = Constant.LIST_ALL_CONTENT(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(Constant.LIST_GIFT_DECRYPTED == null){
                Constant.LIST_GIFT_DECRYPTED = new byte[Constant.TEMP_ALL_CONTENT.length][];
            }

            if (Constant.LIST_GIFT_DECRYPTED[position] == null) {
                doDecryption(position);
            } else {
                previewGif.setVisibility(View.VISIBLE);
                previewProgress.setVisibility(View.GONE);
                Glide.with(context)
                        .load(Constant.LIST_GIFT_DECRYPTED[position])
                        .into(previewGif);
                gifBytes = Constant.LIST_GIFT_DECRYPTED[position];
            }
        }
    }

    private void doDecryption(int position){
        singleDecryption = new SingleDecryption(this);
        singleDecryption.setListenerSingleDecryption(new SingleDecryption.ListenerSingleDecryption() {
            @Override
            public void onSuccesSingleDecryption(byte[] resultSingleDecryption) {
                previewGif.setVisibility(View.VISIBLE);
                previewProgress.setVisibility(View.GONE);
                Glide.with(context)
                        .load(resultSingleDecryption)
                        .into(previewGif);
                gifBytes = resultSingleDecryption;
            }
        });
        singleDecryption.execute(Constant.TEMP_ALL_CONTENT[position]);
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void initView(){
        previewGif = (ImageView) findViewById(R.id.imgPreview);
        previewProgress = (ProgressBar) findViewById(R.id.progresPreview);
        imgFavorite = (ImageView) findViewById(R.id.imgFavorite);
    }

    public void clickListener(View view){
        if(gifBytes==null){
            Toast.makeText(context, "Please wait, Gif is still loading", Toast.LENGTH_SHORT).show();
        }else {
            int id = view.getId();
            actionManagement = new ActionManagement(context, gifBytes);
            switch (id) {
                case R.id.btnSave:
                    actionManagement.execute(ActionManagement.ACTION_SAVE);
                    break;
                case R.id.btnShare:
                    actionManagement.execute(ActionManagement.ACTION_SHARE);
                    break;
                case R.id.btnSetAs:
                    actionManagement.execute(ActionManagement.ACTION_SET_AS);
                    break;
                case R.id.btnFavoritUnfavorit:
                    if(FavoritHelper.apakahSudahterbookmark(position, context)){
                        imgFavorite.setImageResource(R.drawable.off);
                        FavoritHelper.hapusDariFavorit(position, context);
                    }else{
                        imgFavorite.setImageResource(R.drawable.on);
                        FavoritHelper.simpanKeFavorit(position, context);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        previewGif = null;
        previewProgress = null;
        singleDecryption = null;
        gifBytes = null;
        actionManagement = null;
        imgFavorite = null;
    }

    @Override
    public void onBackPressed() {
        previewGif = null;
        previewProgress = null;
        singleDecryption = null;
        gifBytes = null;
        actionManagement = null;
        imgFavorite = null;

        Intent resultIntent = new Intent();
        resultIntent.putExtra("keyresult", true);
        setResult(Activity.RESULT_OK, resultIntent);

        finish();
    }
}
