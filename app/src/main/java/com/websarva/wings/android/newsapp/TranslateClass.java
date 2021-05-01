package com.websarva.wings.android.newsapp;

import android.content.Context;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TranslateClass {
    private String text;
    TranslateClass(Context context){
    }

    String GetTranslateWord(String url){
                try {
                    // Okhttp3通信を行う
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .build();

                    Response response = client.newCall(request).execute();
                    String jsonStr = response.body().string();
                    JSONObject rootJSON = new JSONObject(jsonStr);

                    text = rootJSON.getString("text");
                }catch (Exception e){
                 //   Log.e("test",e.getMessage());
                }

        return text;
    }

    String GetShortenedUrl(String url, String words){
        try{
            // Okhttp3通信を行う
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("words",words)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            String jsonStr = response.body().string();
            JSONObject rootJSON = new JSONObject(jsonStr);

            text = rootJSON.getJSONObject("data").getString("url");
        }catch (Exception e){
          //  Log.e("test",e.getMessage());
        }
        return text;
    }
}
