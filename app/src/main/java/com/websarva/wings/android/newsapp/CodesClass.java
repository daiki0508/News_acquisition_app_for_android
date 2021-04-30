package com.websarva.wings.android.newsapp;

import android.content.Context;
import android.widget.Toast;

public class CodesClass {
    private final Context con;
    CodesClass(Context context){
        this.con = context;
    }
    String Code(String outputLang, String lang) {
        String code;
        if (outputLang.equals("ニュースアプリ")) {
            switch (lang) {
                case "英語":
                    code = "en";
                    break;
                case "中国語":
                    code = "zh";
                    break;
                case "韓国語":
                    code = "ko";
                    break;
                case "ロシア語":
                    code = "ru";
                    break;
                default:
                    code= "ja";
            }
            // 英語
        } else if (outputLang.equals("NewsApp")) {
            switch (lang) {
                case "Japanese":
                    code = "ja";
                    break;
                case "Chinese":
                    code = "zh";
                    break;
                case "Korean":
                    code = "ko";
                    break;
                case "Russian":
                    code = "ru";
                    break;
                default:
                    code = "en";
            }
            // 中国語
        } else if (outputLang.equals("新闻应用")) {
            switch (lang) {
                case "日本":
                    code = "ja";
                    break;
                case "英语":
                    code = "en";
                    break;
                case "韩语":
                    code = "ko";
                    break;
                case "俄语":
                    code = "ru";
                    break;
                default:
                    code = "zh";
            }
            // 韓国語
        } else if (outputLang.equals("뉴스 애플 리케이션")) {
            switch (lang) {
                case "일본어":
                    code = "ja";
                    break;
                case "중국":
                    code = "zh";
                    break;
                case "영어":
                    code = "en";
                    break;
                case "러시아어":
                    code = "ru";
                    break;
                default:
                    code = "ko";
            }
            // ロシア語
        } else {
            switch (lang) {
                case "Японский":
                    code = "ja";
                    break;
                case "китайский язык":
                    code = "zh";
                    break;
                case "корейский язык":
                    code = "ko";
                    break;
                case "английский":
                    code = "en";
                    break;
                default:
                    code = "ru";
            }
        }
        return code;
    }

    void NoWordError(String outputLang) {
        String show = "";
        switch (outputLang) {
            case "NewsApp":
                show = "No search term entered";
                break;
            case "新闻应用":
                show = "没有输入搜索词";
                break;
            case "뉴스 애플 리케이션":
                show = "검색어가 입력되어 있지 않습니다";
                break;
            case "Приложение новостей":
                show = "Поисковый запрос не введен";
                break;
            default:
                show = "検索用語が入力されていません";
        }
        // Toastの表示
        Toast.makeText(con,show,Toast.LENGTH_LONG).show();
    }
    String cityCode(int selected_id){
        String ret_id = "";
        selected_id +=1;
        if (selected_id == 1){
            ret_id = "016010";
        }else if (selected_id > 1 && selected_id < 11){
            ret_id = "0" + selected_id + "0010";
        }else if (selected_id >11 && selected_id < 21){
            ret_id = "1" + selected_id + "0010";
        }else if (selected_id > 21 && selected_id < 31){
            ret_id = "2" + selected_id + "0010";
        }else if (selected_id > 31 && selected_id <41){
            ret_id ="3" + selected_id + "0010";
        }else if (selected_id > 41 && selected_id < 47){
            ret_id = "4"+ selected_id + "0010";
        }else if (selected_id == 47){
            ret_id = "471010";
        }
        return ret_id;
    }

    String langCode(int selected_id2){
        String ret_id = "";
        switch (selected_id2){
            case 0:
                ret_id = "ja";
                break;
            case 1:
                ret_id = "en";
                break;
            case 2:
                ret_id = "ko";
                break;
            case 3:
                ret_id = "ru";
                break;
            case 4:
                ret_id = "zh";
                break;
        }
        return ret_id;
    }
}
