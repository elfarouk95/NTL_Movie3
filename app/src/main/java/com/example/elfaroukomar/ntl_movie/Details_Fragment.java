package com.example.elfaroukomar.ntl_movie;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Details_Fragment extends Fragment {

    Film_Model item;
    String Json;
    public Details_Fragment(Film_Model film,String json) {
        item =film;
        Json=json;
        // Required empty public constructor
    }
    public Details_Fragment() {

        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("item", (Serializable) item);
        outState.putString("Json",Json);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null)
        {
            item=(Film_Model)savedInstanceState.getSerializable("item");
            Json =savedInstanceState.getString("Json");

        }
    }

    RatingBar ratingBar;
    ImageView backposter;
    ArrayAdapter arrayAdapter;
    ArrayList<Review_model> review_models;
    ListView listView;
    public ArrayList<String>con(ArrayList<Review_model>reviews_models)
    {
        ArrayList<String>arrayList = new ArrayList<>();
        for (int i=0;i<reviews_models.size();i++)
        {
            arrayList.add(reviews_models.get(i).gettotal());
        }

        return arrayList;
    }

    ImageButton LikeBTn;
    DB_helper db_helper;

    Button Trailer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_details_, container, false);
        db_helper=new DB_helper(getActivity().getApplication().getApplicationContext(),1);
        movieTrailer(item.getID());


        LikeBTn =(ImageButton)v.findViewById(R.id.imageButtonLike);


        ratingBar =(RatingBar)v.findViewById(R.id.ratingBar);
        listView =(ListView)v.findViewById(R.id.ReviewList);
        try {
            review_models=ParseMovieJson.ParseReview(Json);

            TextView textView = new TextView(getActivity().getApplicationContext());
            textView.setText("Reviews");
            textView.setTextSize(20);

            listView.addHeaderView(textView);
            arrayAdapter = new ArrayAdapter(getActivity().getApplication().getApplicationContext(),android.R.layout.simple_list_item_1,con(review_models));
            listView.setAdapter(arrayAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (item.getIsFav().equals("1")) {
            LikeBTn.setImageResource(R.drawable.fstar);
           // item.setIsFav("0");
        }
        else {
            LikeBTn.setImageResource(R.drawable.star);
          //  item.setIsFav("1");
        }



        ratingBar.setRating((Float.parseFloat(item.getVote_average())));
        backposter = (ImageView)v.findViewById(R.id.Backposter);
        Picasso.with(getActivity().getApplication().getApplicationContext()).load("http://image.tmdb.org/t/p/w1000"+item.backdrop_path).into(backposter);
        ((TextView)v.findViewById(R.id.orginaltitle)).setText(item.getOriginal_title());
        ((TextView)v.findViewById(R.id.overview)).setText(item.getOverview());

        LikeBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getIsFav().equals("1")) {
                    getContext().getContentResolver().update(MovieContnetProvider.Content_uri,replacefav(item,"0"),null,null);
                   // db_helper.replacefav(item, "0");
                    LikeBTn.setImageResource(R.drawable.star);
                    item.setIsFav("0");
                }
                else{
                    getContext().getContentResolver().update(MovieContnetProvider.Content_uri,replacefav(item,"1"),null,null);
                  //  db_helper.replacefav(item,"1");
                    LikeBTn.setImageResource(R.drawable.fstar);
                    item.setIsFav("1");}
            }
        });

        Videolistviw =(ListView)v.findViewById(R.id.videoList);
        return  v;

    }

    ListView Videolistviw;
    ArrayAdapter arrayAdaptervideo;
    ArrayList<String>VideoLink;

    void movieTrailer(String Id)
    {
        VideoLink = new ArrayList<>();
        String url ="https://api.themoviedb.org/3/movie/"+Id+"/videos?api_key=63103295acf92a181b68289f20ca0e98";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject mainJson = null;
                try {
                    mainJson = new JSONObject(response);
                    JSONArray ArrayMovie = mainJson.getJSONArray("results");

                    for (int i=0;i<ArrayMovie.length();i++) {
                        JSONObject film = ArrayMovie.getJSONObject(i);
                       // Toast.makeText(getActivity().getApplication().getApplicationContext(), film.getString("key"), Toast.LENGTH_SHORT).show();
                        VideoLink.add(film.getString("key").toString());
                    }
                    arrayAdapter=new ArrayAdapter(getActivity().getApplication().getApplicationContext(),android.R.layout.simple_list_item_1,VideoLink);
                    Videolistviw.setAdapter(arrayAdapter);
                    TextView textView = new TextView(getActivity().getApplicationContext());
                    textView.setText("videos");
                    textView.setTextSize(20);

                    Videolistviw.addHeaderView(textView);
                    Videolistviw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String YoutubeLink =  "https://www.youtube.com/watch?v="+VideoLink.get(i);
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YoutubeLink)));
                        }
                    });
                         /* String YoutubeLink =  "https://www.youtube.com/watch?v="+film.get("key").toString();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YoutubeLink)));*/


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "No Video", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        Volley.newRequestQueue(getActivity().getApplication().getApplicationContext()).add(stringRequest);
    }
    private ContentValues replacefav(Film_Model film, String IsFav) {


        ContentValues values = new ContentValues();
        values.put("ID", film.getID());
        values.put("original_title", film.getOriginal_title());
        values.put("overview", film.getOverview());
        values.put("poster_path", film.getPoster_path());
        values.put("backdrop_path", film.getBackdrop_path());
        values.put("vote_average", film.getVote_average());
        values.put("release_date", film.getRelease_date());
        values.put("IsFav", IsFav);
        values.put("Type", film.getType());
        return values;
    }
}
