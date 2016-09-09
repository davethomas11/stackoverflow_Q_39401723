package com.daveanthonythomas.myapplication;

import android.widget.ImageView;

/**
 * Created by dave on 2016-09-08.
 */
public class Memo {

    public String header, bodyText;
    public ImageView imageView;

    public Memo(String header, String bodyText, ImageView imageView){
        this.header = header;
        this.bodyText =  bodyText;
        this.imageView = imageView;
    }
}
