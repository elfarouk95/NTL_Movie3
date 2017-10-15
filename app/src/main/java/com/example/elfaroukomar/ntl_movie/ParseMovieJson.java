package com.example.elfaroukomar.ntl_movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Elfarouk Omar on 28/08/2017.
 */

public class ParseMovieJson {

    public static ArrayList<Film_Model> parse (String MovieJson) throws JSONException {

        ArrayList<Film_Model> movie = new ArrayList<>();
        JSONObject mainJson = new JSONObject(MovieJson);
        JSONArray ArrayMovie = mainJson.getJSONArray("results");

        for (int i=0;i<ArrayMovie.length();i++)
        {
            JSONObject film = ArrayMovie.getJSONObject(i);

            Film_Model model = new Film_Model(film.getString("original_title"),
                    film.getString("poster_path"),
                    film.getString("backdrop_path"),
                    film.getString("overview"),
                    film.getString("vote_average"),
                    film.getString("release_date"),
                    film.getString("id")
                    );

            movie.add(model);

        }

        return movie;
    }


     public  static ArrayList<Review_model> ParseReview (String ReviewJson) throws JSONException
     {

         ArrayList<Review_model> Reviews = new ArrayList<>();
         JSONObject mainJson = new JSONObject(ReviewJson);
         JSONArray ArrayMovie = mainJson.getJSONArray("results");

         for (int i=0;i<ArrayMovie.length();i++) {
             JSONObject film = ArrayMovie.getJSONObject(i);
             Review_model review_model = new Review_model(film.getString("author"),film.getString("content"));
             Reviews.add(review_model);
            }
         return Reviews;
     }

}
