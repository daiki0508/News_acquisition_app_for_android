package com.websarva.wings.android.newsapp;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.util.Objects.requireNonNull;

public class HttpRequestClass extends NewsAppActivity{
    private final Context con;
    private String jsonStr;
    private byte[] iv_decode = null;
    private byte[] en2 = null;
    private SecretKeySpec key;
    private byte[] bytes =null;
    private byte[] keys = null;
    private String result;

    // APIキー
    //private final static String YOUR_API_KEY = "hogehoge";

    HttpRequestClass(Context context){
        this.con = context;
    }

    String httpRequest(String[] aes_data, String url,int api_flag) {
        // OkHttp3で通信を行う
        OkHttpClient client = new OkHttpClient();
        Request request;
        if (api_flag == 0) {
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
                Log.e("eroor_decode",e.getMessage());
            }

            EditText editText = (EditText) ((Activity)con).findViewById(R.id.search_conditions3_edit);
            String word = editText.getText().toString();
            Log.d("web_word",word);

            RequestBody formBody = new FormBody.Builder()
                    .add("word",word)
                    .build();

            try {
                //  gjnc = new GetJSONNewsClass(con);
                request = new Request.Builder()
                        .url(result)
                        .post(formBody)
                        //.get()
                       // .addHeader("x-rapidapi-host", "contextualwebsearch-websearch-v1.p.rapidapi.com")
                       // .addHeader("x-rapidapi-key", YOUR_API_KEY)
                        .build();

                //  progressBar = (ProgressBar) ((com.websarva.wings.android.newsapp.NewsAppActivity) con).findViewById(R.id.progressbar);

                Response response = client.newCall(request).execute();
                jsonStr = response.body().string();
                //     gjnc.GetJSONNews(_ResultList, jsonStr, progressBar);
            } catch (IOException e) {
                Log.e("eroor_news", e.getMessage());
            }

            Arrays.fill(bytes,(byte) 0);
            Arrays.fill(keys,(byte) 0);
            Arrays.fill(iv_decode,(byte) 0);
            Arrays.fill(en2,(byte) 0);
            result = "";

        } else {
            try {
                //    gjwc = new GetJSONWeatherClass(con);
                request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();

                //     progressBar = (ProgressBar) ((com.websarva.wings.android.newsapp.WeatherAppActivity) con).findViewById(R.id.progressbar);
                Response response = client.newCall(request).execute();
                jsonStr = response.body().string();
            } catch (IOException e) {
                Log.e("eroor_news", e.getMessage());
            }
        }


        // 非同期処理
        /*client.newCall(requireNonNull(request))
                .enqueue(new Callback() {
                    // 失敗時の処理
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("test", e.getMessage());
                    }

                    // 成功時の処理
                    // レスポンスデータを処理
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        // JSONデータを取得する
                        final String jsonStr = requireNonNull(response.body()).string();
                        if (api_flag == 0) {
                            gjnc.GetJSONNews(_ResultList, jsonStr,progressBar);
                        } else {
                            gjwc.GetJSONWeather(jsonStr,_ResultList,progressBar,selected_id2);
                        }
                    }
                });*/

        return jsonStr;
    }
}
