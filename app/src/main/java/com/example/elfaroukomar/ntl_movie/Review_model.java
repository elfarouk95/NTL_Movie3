package com.example.elfaroukomar.ntl_movie;

/**
 * Created by Elfarouk Omar on 31/08/2017.
 */

public class Review_model {

    public Review_model(String author, String content) {
        Author = author;
        this.content = content;
    }

    String Author;


    public String gettotal() {
        return Author+" : "+content;
    }



    String content;

}
