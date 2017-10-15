package com.example.elfaroukomar.ntl_movie;

import java.io.Serializable;

/**
 * Created by Elfarouk Omar on 28/08/2017.
 */

public class Film_Model  implements Serializable{



    String original_title;
    String poster_path;
    String backdrop_path;
    String overview;
    String vote_average;
    String release_date;
    String ID;

    public String getIsFav() {
        return IsFav;
    }

    public void setIsFav(String isFav) {
        IsFav = isFav;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    String IsFav="0";
    String Type;






    public Film_Model(String original_title, String poster_path, String backdrop_path, String overview, String vote_average, String release_date, String ID) {
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }







    public String getOriginal_title() {
        return original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

}
