package com.hdgifstudios.util;


import android.content.Context;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FavoritHelper {
    private static int kodeUnik = 9999;  //maksimum item yang bisa di bookmark

    public static boolean simpanKeFavorit(int index, Context context){
        if(dataBelumAda(index, context)) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("index_" + index, index).apply();
            return true;
        }else{
            return false;
        }
    }

    public static boolean hapusDariFavorit(int index, Context context){
        if(!dataBelumAda(index, context)) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().remove("index_" + index).apply();
            return true;
        }else{
            return false;
        }
    }

    public static boolean apakahSudahterbookmark(int index, Context context){
        return !dataBelumAda(index, context);
    }

    private static boolean dataBelumAda(int index, Context context){
        int hasil = PreferenceManager.getDefaultSharedPreferences(context).getInt("index_"+index, kodeUnik);
        return hasil == kodeUnik;
    }

    public static List<Integer> getDataFavorit(Context context) {

        if(Constant.TEMP_ALL_CONTENT == null){
            try {
                Constant.TEMP_ALL_CONTENT = Constant.LIST_ALL_CONTENT(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<Integer> semuaDataFavorit = new ArrayList<>();
        for(int i = 0; i< Constant.TEMP_ALL_CONTENT.length; i++) {
            int data = PreferenceManager.getDefaultSharedPreferences(context).getInt("index_"+i, kodeUnik);
            if(data != kodeUnik){
                semuaDataFavorit.add(data);
            }
        }
        return semuaDataFavorit;
    }

    public static int getIndexSesungguhnya(String namaKonten, Context context){

        if(Constant.TEMP_ALL_CONTENT == null){
            try {
                Constant.TEMP_ALL_CONTENT = Constant.LIST_ALL_CONTENT(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i<Constant.TEMP_ALL_CONTENT.length; i++){
            if(namaKonten.equals(Constant.TEMP_ALL_CONTENT[i])){
                return i;
            }
        }
        return 0;
    }
}
