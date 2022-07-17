package com.hdgifstudios.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hdgifstudios.R;
import com.hdgifstudios.activity.GifPreviewActivity;
import com.hdgifstudios.asyncronus.SingleDecryption;
import com.hdgifstudios.util.Constant;

import java.io.IOException;

public class GifCollectionsAdapter extends RecyclerView.Adapter<GifCollectionsAdapter.HolderView>{
    private Context context;

    public GifCollectionsAdapter(Context context) {
        this.context = context;
        if(Constant.TEMP_ALL_CONTENT == null){
            try {
                Constant.TEMP_ALL_CONTENT = Constant.LIST_ALL_CONTENT(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_item_collections, parent, false);
        return new HolderView(item);
    }

    @Override
    public void onBindViewHolder(final HolderView holder, final int position) {
        if(Constant.LIST_GIFT_DECRYPTED == null){
            Constant.LIST_GIFT_DECRYPTED = new byte[Constant.TEMP_ALL_CONTENT.length][];
        }

        if(Constant.LIST_GIFT_DECRYPTED[position] != null){
            holder.progressBar.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(Constant.LIST_GIFT_DECRYPTED[position])
                    .into(holder.imageView);
        }else {
            SingleDecryption singleDecryption = new SingleDecryption(context);
            singleDecryption.setListenerSingleDecryption(new SingleDecryption.ListenerSingleDecryption() {
                @Override
                public void onSuccesSingleDecryption(byte[] resultSingleDecryption) {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.imageView.setVisibility(View.VISIBLE);
                    Constant.LIST_GIFT_DECRYPTED[position] = resultSingleDecryption;
                    Glide.with(context)
                            .load(Constant.LIST_GIFT_DECRYPTED[position])
                            .into(holder.imageView);
                }
            });
            singleDecryption.execute(Constant.TEMP_ALL_CONTENT[position]);
        }

        holder.clickabe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GifPreviewActivity.class);
                intent.putExtra(Constant.INDEX_CODE, String.valueOf(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Constant.TEMP_ALL_CONTENT.length;
    }

    class HolderView extends RecyclerView.ViewHolder{

        private LinearLayout clickabe;
        private ImageView imageView;
        private ProgressBar progressBar;

        public HolderView(View itemView) {
            super(itemView);

            clickabe = itemView.findViewById(R.id.design_item_collections_clickable);
            imageView = itemView.findViewById(R.id.imgviewCollections);
            progressBar = itemView.findViewById(R.id.progress_design_item_collections);
        }
    }
}
