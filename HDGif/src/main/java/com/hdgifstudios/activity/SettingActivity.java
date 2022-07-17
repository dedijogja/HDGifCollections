package com.hdgifstudios.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hdgifstudios.R;
import com.hdgifstudios.core.CoreAppCompactActivity;
import com.hdgifstudios.util.Constant;


public class SettingActivity extends CoreAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }


    private void init(){
        ((EditText) findViewById(R.id.editFolderName)).setText(PreferenceManager.getDefaultSharedPreferences(this).getString(Constant.PREF_FOLDER_NAME, getString(R.string.app_name)));
        ((EditText) findViewById(R.id.editColumnCollections)).setText(String.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getInt(Constant.PREF_COLLECTIONS_COLUMN, 2)));
        ((EditText) findViewById(R.id.editColumnFavorite)).setText(String.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getInt(Constant.PREF_FAVORITE_COLUMN, 2)));
        ((EditText) findViewById(R.id.editColumnSearch)).setText(String.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getInt(Constant.PREF_SEARCH_COLUMN, 2)));
    }

    public void onClickEvent(View view){
        int id = view.getId();
        switch (id){
            case R.id.btnSettingSave:
                if(validate()){
                    saveSetting();
                    Toast.makeText(this, "Setting saved succesfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case R.id.btnSettingCancel:
                finish();
                break;
        }
    }



    int columnCollections = 0;
    int columnFavorite = 0;
    int columnSearch = 0;
    String folderName;

    private boolean validate(){

        boolean status = true;

        folderName = ((EditText) findViewById(R.id.editFolderName)).getText().toString();
        try {
            columnCollections = Integer.parseInt(((EditText) findViewById(R.id.editColumnCollections)).getText().toString());
            columnFavorite = Integer.parseInt(((EditText) findViewById(R.id.editColumnFavorite)).getText().toString());
            columnSearch = Integer.parseInt(((EditText) findViewById(R.id.editColumnSearch)).getText().toString());
        }catch (Exception e){

        }

        if(folderName.equals("")){
            ((EditText) findViewById(R.id.editFolderName)).setError("This field cannot be empty");
            status = false;
        }

        if(columnCollections < 1 || columnCollections > 5){
            ((EditText) findViewById(R.id.editColumnCollections)).setError("Value must between 1-5");
            status = false;
        }


        if(columnFavorite < 1 || columnFavorite > 5){
            ((EditText) findViewById(R.id.editColumnFavorite)).setError("Value must between 1-5");
            status = false;
        }

        if(columnSearch < 1 || columnSearch > 5){
            ((EditText) findViewById(R.id.editColumnSearch)).setError("Value must between 1-5");
            status = false;
        }

        return status;
    }

    private void saveSetting(){
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(Constant.PREF_FOLDER_NAME, folderName).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(Constant.PREF_COLLECTIONS_COLUMN, columnCollections).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(Constant.PREF_FAVORITE_COLUMN, columnFavorite).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(Constant.PREF_SEARCH_COLUMN, columnSearch).apply();
    }
}
