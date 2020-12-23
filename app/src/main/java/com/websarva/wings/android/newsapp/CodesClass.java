package com.websarva.wings.android.newsapp;

public class CodesClass {
    CodesClass(){

    }
    String[] Code(String outputLang, String area, String lang) {
        String[] codes = new String[2];
        if (outputLang.equals("ニュースアプリ")) {
            switch (area) {
                case "アメリカ":
                    codes[0] = "US";
                    break;
                case "中国":
                    codes[1] = "CN";
                    break;
                case "韓国":
                    codes[0] = "KR";
                    break;
                case "ロシア":
                    codes[0] = "RU";
                    break;
                default:
                    codes[0] = "JP";
            }
            switch (lang) {
                case "英語":
                    codes[1] = "en";
                    break;
                case "中国語":
                    codes[1] = "zh";
                    break;
                case "韓国語":
                    codes[1] = "ko";
                    break;
                case "ロシア語":
                    codes[1] = "ru";
                    break;
                default:
                    codes[1] = "ja";
            }
            // 英語
        } else if (outputLang.equals("NewsApp")) {
            switch (area) {
                case "Japan":
                    codes[0] = "JP";
                    break;
                case "China":
                    codes[0] = "CN";
                    break;
                case "Korea":
                    codes[0] = "KR";
                    break;
                case "Russia":
                    codes[0] = "RU";
                    break;
                default:
                    codes[0] = "US";
            }
            switch (lang) {
                case "Japanese":
                    codes[1] = "ja";
                    break;
                case "Chinese":
                    codes[1] = "zh";
                    break;
                case "Korean":
                    codes[1] = "ko";
                    break;
                case "Russian":
                    codes[1] = "ru";
                    break;
                default:
                    codes[1] = "en";
            }
            // 中国語
        } else if (outputLang.equals("新闻应用")) {
            switch (area) {
                case "日本":
                    codes[0] = "JP";
                    break;
                case "美国":
                    codes[0] = "US";
                    break;
                case "韩国":
                    codes[0] = "KR";
                    break;
                case "俄国":
                    codes[0] = "RU";
                    break;
                default:
                    codes[0] = "CN";
            }
            switch (lang) {
                case "日本":
                    codes[1] = "ja";
                    break;
                case "英语":
                    codes[1] = "en";
                    break;
                case "韩语":
                    codes[1] = "ko";
                    break;
                case "俄语":
                    codes[1] = "ru";
                    break;
                default:
                    codes[1] = "zh";
            }
            // 韓国語
        } else if (outputLang.equals("뉴스 애플 리케이션")) {
            switch (area) {
                case "일본":
                    codes[0] = "JP";
                    break;
                case "중국":
                    codes[0] = "CN";
                    break;
                case "미국":
                    codes[0] = "US";
                    break;
                case "러시아":
                    codes[0] = "RU";
                    break;
                default:
                    codes[0] = "KR";
            }
            switch (lang) {
                case "일본어":
                    codes[1] = "ja";
                    break;
                case "중국":
                    codes[1] = "zh";
                    break;
                case "영어":
                    codes[1] = "en";
                    break;
                case "러시아어":
                    codes[1] = "ru";
                    break;
                default:
                    codes[1] = "ko";
            }
            // ロシア語
        } else {
            switch (area) {
                case "Япония":
                    codes[0] = "JP";
                    break;
                case "Китай":
                    codes[0] = "CN";
                    break;
                case "Корея":
                    codes[0] = "KR";
                    break;
                case "Америка":
                    codes[0] = "US";
                    break;
                default:
                    codes[0] = "RU";
            }
            switch (lang) {
                case "Японский":
                    codes[1] = "ja";
                    break;
                case "китайский язык":
                    codes[1] = "zh";
                    break;
                case "корейский язык":
                    codes[1] = "ko";
                    break;
                case "английский":
                    codes[1] = "en";
                    break;
                default:
                    codes[1] = "ru";
            }
        }
        return codes;
    }

    String NoWordError(String outputLang) {
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
        return show;
    }
}
