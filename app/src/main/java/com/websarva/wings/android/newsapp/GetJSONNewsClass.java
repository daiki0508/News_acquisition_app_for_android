package com.websarva.wings.android.newsapp;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class GetJSONNewsClass extends NewsAppActivity {
    // コンテキストフィールドの定義
    private final Context con;
    // 他クラスのコンテキスト
    private TranslateClass tc;
    private CodesClass cc;
    // AES関連のフィールド
    private String[] aes_data;
    private byte[] iv_decode = null;
    private byte[] en2 = null;
    private SecretKeySpec key;
    private byte[] bytes =null;
    private byte[] keys = null;
    private String result;
    // その他
    private String titles;
    private String words;
    private String code;
    private SimpleAdapter adapter;

    GetJSONNewsClass(Context context) {
        this.con = context;
    }


    SimpleAdapter GetJSONNews(final String jsonStr) {
        // ループフラグ
        boolean flag = false;
        // 読み込む記事の数
        final int ListNum = 10;

        List<Map<String, String>> NewsList = new ArrayList<>();
        // コンテキストからProgressBarを取得
        ProgressBar progressBar = (ProgressBar) ((Activity)con).findViewById(R.id.progressbar);
       // Log.d("test", "jsonStr=" + jsonStr);
        final int[] val = {50};

        // AESデータのセット
        /*
        詳しくはNewsAppActivityを参照
         */
        aes_data = new String[3];
        aes_data[0] = "zJ9nWF3rN5JRix6S9+8D1Z39Z36ii1QosF1RxlcgoerghVyorvbjilX13IW6546CT/8nTjwhgod9xNxZyfEyew==";
        aes_data[1] = "RU43Q2FITG5lUHI0OGh2NQ==";
        aes_data[2] = "BWU2HjGaG0YHsJOMT6W5XA==";

        // AES復号開始
        bytes = new byte[256 / 8];
        keys = Base64.decode(aes_data[1],Base64.DEFAULT);

        for (int i = 0; i < new String(keys).length(); i++){
            if (i >= bytes.length){
                break;
            }
            bytes[i] = keys[i];
        }
        key = new SecretKeySpec(bytes, "AES");

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

        for (int i = 0; i < ListNum; i++) {
            try {
                JSONObject rootJSON = new JSONObject(jsonStr);
                // articlesというキー名の中のJSONを取得
                JSONArray articlesJSON = rootJSON.getJSONArray("value");
                // 一つ格納
                JSONObject article = articlesJSON.getJSONObject(i);

                // articlesの必要要素を変数に代入
                tc = new TranslateClass(con);
                titles = article.getString("title");
                words = article.getString("url");

             //   Log.d("url_words", result);
              //  Log.d("test_titles",titles);
                // 短縮URL関数に処理を飛ばす
                words = tc.GetShortenedUrl(result,words);
              //  Log.d("test_words",words);

                // コンテキストからそれぞれのフィールドを取得
                /*
                con2_list...スピナー
                lang...変換言語
                outputLang...アプリの言語を取得
                 */
                Spinner con2_list = (Spinner) ((Activity)con).findViewById(R.id.search_conditions2_list);
                String lang = (String) con2_list.getSelectedItem();
                String outputLang = (String)((Activity)con).getString(R.string.app_name);

                // codeを取得するための関数へ飛ばす
                cc = new CodesClass(con);
                code = cc.Code(outputLang,lang);

                // codeの戻り値がenじゃなければ翻訳する
                if (!code.equals("en")){
                    String url = "https://script.google.com/macros/s/AKfycbzZtvOvf14TaMdRIYzocRcf3mktzGgXvlFvyczo/exec?text=" + titles + "&source=en&target=" + code;
                    // Log.d("test_code",code);
                    // Log.d("url_titles",url);
                    // 翻訳開始
                    titles = tc.GetTranslateWord(url);
                   // Log.d("test_titles",titles);
                }

                val[0] += 50 / ListNum;
                progressBar.setProgress(val[0]);

                // ループ最後の処理のみflagをtrueにする
                if (i == ListNum - 1) {
                    flag = true;
                }
            } catch (JSONException e) {
              //  Log.e("eroor_getJSON", e.getMessage());
            }

            // UIスレッドを操作の準備
            boolean finalFlag = flag;
            Map<String, String> news = new HashMap<>();
            news.put("title", titles);
            news.put("url", words);
            NewsList.add(news);

            if (finalFlag){
                // AESのカギ情報をメモリから削除
                Arrays.fill(bytes, (byte) 0);
                Arrays.fill(keys, (byte) 0);
                Arrays.fill(iv_decode, (byte) 0);
                Arrays.fill(en2, (byte) 0);
                result = "";

                val[0] = 50 - val[0];
                progressBar.setProgress(val[0]);
                // SimpleAdapterの第４引数を定義
                String[] from = {"title", "url"};
                // SimpleAdapterの第５引数を定義
                int[] to = {android.R.id.text1, android.R.id.text2};
                // SimpleAdapterを生成
                adapter = new SimpleAdapter(con, NewsList, android.R.layout.simple_list_item_2, from, to);
            }
        }
        return adapter;
    }
}
