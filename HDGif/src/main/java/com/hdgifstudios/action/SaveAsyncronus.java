package com.hdgifstudios.action;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.hdgifstudios.R;
import com.hdgifstudios.util.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveAsyncronus extends AsyncTask<byte[], Void, File> {

    private Context context;
    private SaveAsyncronusListener saveAsyncronusListener;
    private String resultFileName;

    public SaveAsyncronus(Context context, String resultFileName) {
        this.context = context;
        this.resultFileName = resultFileName;
    }

    @Override
    protected File doInBackground(byte[]... bytes) {
        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/" + PreferenceManager.getDefaultSharedPreferences(context).getString(Constant.PREF_FOLDER_NAME, context.getString(R.string.app_name)));
        if(!folder.exists()){
            folder.mkdirs();
        }
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + PreferenceManager.getDefaultSharedPreferences(context).getString(Constant.PREF_FOLDER_NAME, context.getString(R.string.app_name)), resultFileName);
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes[0]);
            fileOutputStream.flush();
            fileOutputStream.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(File s) {
        saveAsyncronusListener.onFinishSave(s);
    }

    public void setSaveAsyncronus(SaveAsyncronusListener saveAsyncronusListener){
        if(this.saveAsyncronusListener==null){
            this.saveAsyncronusListener = saveAsyncronusListener;
        }
    }

    public interface SaveAsyncronusListener{
        void onFinishSave(File file);
    }
}
