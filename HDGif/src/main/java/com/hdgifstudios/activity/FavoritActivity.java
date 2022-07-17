package com.hdgifstudios.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hdgifstudios.R;
import com.hdgifstudios.adapter.FavoriteGifAdapter;
import com.hdgifstudios.core.CoreAppCompactActivity;
import com.hdgifstudios.util.Constant;
import com.hdgifstudios.view.RecycleSpacing;


public class FavoritActivity extends CoreAppCompactActivity {

    private RecyclerView recyclerView;
    private FavoriteGifAdapter favoriteGifAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorit);

        recyclerView = (RecyclerView) findViewById(R.id.recycleFavorite);
        favoriteGifAdapter = new FavoriteGifAdapter(this);

        int jumlahColumn = PreferenceManager.getDefaultSharedPreferences(this).getInt(Constant.PREF_FAVORITE_COLUMN, 2);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, jumlahColumn);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecycleSpacing(20, this));
        recyclerView.setAdapter(favoriteGifAdapter);
        favoriteGifAdapter.notifyDataSetChanged();
    }

    public void startActivityResult(int position){
        Intent intent = new Intent(this, GifPreviewActivity.class);
        intent.putExtra(Constant.INDEX_CODE, String.valueOf(position));
        startActivityForResult(intent, Constant.REQ_CODE_FOR_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constant.REQ_CODE_FOR_RESULT){
            boolean hasil = data.getBooleanExtra("keyresult", false);
            if(hasil){
                favoriteGifAdapter.updateFavoriteData();
            }
        }
    }

    @Override
    public void onBackPressed() {
        recyclerView = null;
        favoriteGifAdapter = null;
        finish();
    }
}
