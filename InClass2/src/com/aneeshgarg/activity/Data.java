/**
 * Assignment #: 2<br>
 * Filename: Data.java<br>
 * Students in Group: Aneesh Garg
 */
package com.aneeshgarg.activity;

import java.util.ArrayList;

public class Data {
    final static ArrayList<News> NEWS;

    static {
        ArrayList<News> news = new ArrayList<News>();
        news.add(new News("Twitter History", "http://rss.cnn.com/~r/rss/cnn_tech/~3/t_qEgE8ShIA/index.html"));
        news.add(new News("iPhone fingerprint security", "http://rss.cnn.com/~r/rss/cnn_tech/~3/VivhEmeEuQ0/index.html"));
        news.add(new News("The cassette turns 50", "http://rss.cnn.com/~r/rss/cnn_tech/~3/b_XW7KXXHHI/index.html"));
        news.add(new News("Rules of the Internet",
                "http://rss.cnn.com/~r/rss/edition_technology/~3/rNfJdU-VLUE/index.html"));
        news.add(new News("Smartphone habits to avoid",
                "http://rss.cnn.com/~r/rss/edition_technology/~3/zE3PpO0Od2U/index.html"));
        NEWS = news;
    }

    static class News {
        String title, url;

        public News(String title, String url) {
            this.title = title;
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }

        @Override
        public String toString() {
            return "News [title=" + title + ", url=" + url + "]";
        }
    }

}
