package com.hdgifstudios.action;


import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.widget.Toast;

import com.hdgifstudios.R;

import java.io.File;

public class ActionManagement {
    public static final String ACTION_SAVE = "actionsave";
    public static final String ACTION_SHARE = "actionshare";
    public static final String ACTION_SET_AS = "setas";

    private Context contexts;
    private byte[] bytes;

    public ActionManagement(Context context, byte[] bytes) {
        this.contexts = context;
        this.bytes = bytes;
    }

    public void execute(String actionCode){
        SaveAsyncronus saveAsyncronus;
        if(actionCode.equals(ACTION_SAVE)){
            saveAsyncronus = new SaveAsyncronus(contexts, contexts.getString(R.string.app_name) + System.currentTimeMillis() + ".gif");
            saveAsyncronus.setSaveAsyncronus(new SaveAsyncronus.SaveAsyncronusListener() {
                @Override
                public void onFinishSave(File file) {
                    if (file != null) {
                        Toast.makeText(contexts, "Gif saved successfully", Toast.LENGTH_SHORT).show();
                        MediaScannerConnection.scanFile(contexts, new String[]{
                                        file.getAbsolutePath()},
                                null, new MediaScannerConnection.OnScanCompletedListener() {
                                    public void onScanCompleted(String path, Uri uri) {

                                    }
                                });
                    } else {
                        Toast.makeText(contexts, "Gif Failed To save", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            saveAsyncronus.execute(bytes);
        }


        if(actionCode.equals(ACTION_SHARE)){
            saveAsyncronus = new SaveAsyncronus(contexts, "share_temp.gif");
            saveAsyncronus.setSaveAsyncronus(new SaveAsyncronus.SaveAsyncronusListener() {
                @Override
                public void onFinishSave(File file) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/gif");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    contexts.startActivity(Intent.createChooser(intent, "Share This Gif..."));
                }
            });
            saveAsyncronus.execute(bytes);
        }


        if(actionCode.equals(ACTION_SET_AS)){
            saveAsyncronus = new SaveAsyncronus(contexts, "set_as_temp.gif");
            saveAsyncronus.setSaveAsyncronus(new SaveAsyncronus.SaveAsyncronusListener() {
                @Override
                public void onFinishSave(File file) {
                    Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(file), "image/gif");
                    intent.putExtra("mimeType", "image/*");
                    contexts.startActivity(Intent.createChooser(intent, "Set Gif As..."));
                }
            });
            saveAsyncronus.execute(bytes);
        }

    }
}
