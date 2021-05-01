package com.websarva.wings.android.newsapp;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpRequestClass extends NewsAppActivity{
    // コンテキストフィールド
    private final Context con;

    // ProgressBarフィールド
    private ProgressBar progressBar;

    // AES関連のフィールド
    private String jsonStr;
    private byte[] iv_decode = null;
    private byte[] en2 = null;
    private SecretKeySpec key;
    private byte[] bytes =null;
    private byte[] keys = null;
    private String result;


    HttpRequestClass(Context context){
        this.con = context;
    }

    String httpRequest(String[] aes_data, String url,int api_flag) {
        // コンテキストからNewsAppActivityのProgressBarを取得
        progressBar = (ProgressBar) ((Activity)con).findViewById(R.id.progressbar);
        final int[] val = {0};
        progressBar.setMax(100);
        progressBar.setProgress(val[0]);

        // OkHttp3で通信を行う
        OkHttpClient client = new OkHttpClient();
        Request request;
        if (api_flag == 0) {
            // AES復号開始
            bytes = new byte[256 / 8];
            keys = Base64.decode(aes_data[1],Base64.DEFAULT);

            for (int i = 0; i < new String(keys).length(); i++){
                if (i >= bytes.length){
                    break;
                }
                bytes[i] = keys[i];
            }
            key = new SecretKeySpec(bytes,"AES");

            iv_decode = Base64.decode(aes_data[2],Base64.DEFAULT);

            try {
                IvParameterSpec ips = new IvParameterSpec(iv_decode);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE,key,ips);
                en2 = cipher.doFinal(Base64.decode(aes_data[0].getBytes(StandardCharsets.UTF_8),Base64.DEFAULT));

                result = new String(en2,StandardCharsets.US_ASCII);
            }catch (Exception e){
               // Log.e("eroor_decode",e.getMessage());
            }

            val[0] += 25;
            progressBar.setProgress(val[0]);

            // コンテキストからNewsAPPActivityのEditTextを取得
            EditText editText = (EditText) ((Activity)con).findViewById(R.id.search_conditions3_edit);
            String word = editText.getText().toString();
           // Log.d("web_word",word);

            // POSTリクエストパラメータの設定
            RequestBody formBody = new FormBody.Builder()
                    .add("word",word)
                    .build();

            try {
                request = new Request.Builder()
                        .url(result)
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();
                // JSON取得
                jsonStr = response.body().string();

                val[0] += 25;
                progressBar.setProgress(val[0]);
            } catch (IOException e) {
               // Log.e("eroor_news", e.getMessage());
            }

            // カギ関連のデータをメモリから削除
            Arrays.fill(bytes,(byte) 0);
            Arrays.fill(keys,(byte) 0);
            Arrays.fill(iv_decode,(byte) 0);
            Arrays.fill(en2,(byte) 0);
            result = "";

        } else {
            try {
                request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();

                Response response = client.newCall(request).execute();
                // JSON取得
                jsonStr = response.body().string();
            } catch (IOException e) {
               // Log.e("eroor_news", e.getMessage());
            }
        }

        return jsonStr;
    }
}
