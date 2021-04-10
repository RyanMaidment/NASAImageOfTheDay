package com.example.nasaimageoftheday;

import android.graphics.Bitmap;

public class NASAObj {

    private int id;
    private String title;
    private String date;
    private String explanation;
    private Bitmap photo;

    public NASAObj(int id, String date, String explanation, String title, Bitmap photo) {
        this.id = id;
        this.date = date;
        this.explanation = explanation;
        this.title = title;
        this.photo = photo;
    }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public String getExplanation(){return explanation;}

        public String getTitle() {
            return title;
        }

        public Bitmap getPhoto() {
            return photo;
        }

    }
