package com.hdgifstudios.view;


import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

public class RecycleSpacing extends RecyclerView.ItemDecoration {
    private int jarak;
    private Context context;

    public RecycleSpacing(int jarak, Context context) {
        this.jarak = jarak;
        this.context = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = pxToDp(jarak);
        outRect.top = pxToDp(jarak);
        outRect.left = pxToDp(jarak);
        outRect.right = pxToDp(jarak);

        if((parent.getChildAdapterPosition(view) % 2 )== 0){
            outRect.bottom = pxToDp(jarak/2);
            outRect.top = pxToDp(jarak/2);
            outRect.left = pxToDp(jarak);
            outRect.right = pxToDp(jarak/2);
        }else {
            outRect.bottom = pxToDp(jarak/2);
            outRect.top = pxToDp(jarak/2);
            outRect.left = pxToDp(jarak/2);
            outRect.right = pxToDp(jarak);
        }
    }

    private int pxToDp(int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}