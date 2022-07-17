package com.hdgifstudios.asyncronus;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

import com.hdgifstudios.util.Constant;
import com.hdgifstudios.util.Native;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SingleDecryption extends AsyncTask<String, Void, byte[]>{

    private Context context;
    private ListenerSingleDecryption listenerSingleDecryption;

    public SingleDecryption(Context context) {
        this.context = context;
    }

    @Override
    protected byte[] doInBackground(String... strings) {
        SecretKey secretKey =  new SecretKeySpec(Base64.decode(new Native(context).getKeyAssets(), Base64.DEFAULT),
                0, Base64.decode(new Native(context).getKeyAssets(), Base64.DEFAULT).length, "AES");
        byte[] hasil = null;
        try {
            InputStream inputStream = context.getAssets().open(Constant.CONTENT_FOLDER + "/" + strings[0]);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();

            Cipher AesCipher = Cipher.getInstance("AES");
            AesCipher.init(Cipher.DECRYPT_MODE, secretKey);
            hasil = AesCipher.doFinal(bytes);
        } catch (IOException | BadPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return hasil;
    }


    @Override
    protected void onPostExecute(byte[] bytes) {
        listenerSingleDecryption.onSuccesSingleDecryption(bytes);
    }

    public void setListenerSingleDecryption(ListenerSingleDecryption listenerSingleDecryption){
        if(this.listenerSingleDecryption==null){
            this.listenerSingleDecryption = listenerSingleDecryption;
        }
    }

    public interface ListenerSingleDecryption{
        void onSuccesSingleDecryption(byte[] resultSingleDecryption);
    }
}
