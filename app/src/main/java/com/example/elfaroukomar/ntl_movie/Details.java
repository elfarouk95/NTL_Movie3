package com.example.elfaroukomar.ntl_movie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Details extends AppCompatActivity {
    Intent n;
    Film_Model f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       n = getIntent();
       f=(Film_Model)n.getSerializableExtra("item");

        String url = "https://api.themoviedb.org/3/movie/"+f.getID()+"/reviews?api_key=63103295acf92a181b68289f20ca0e98";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                setContentView(R.layout.activity_details);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.Contentdetai,new Details_Fragment(f,response))
                        .commit();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        Volley.newRequestQueue(this).add(stringRequest);





    }
}
