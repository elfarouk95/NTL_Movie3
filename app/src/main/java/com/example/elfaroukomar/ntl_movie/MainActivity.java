package com.example.elfaroukomar.ntl_movie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity  {

    private FilmsViewFragment.passData sd;
    //String Json;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                url = "https://api.themoviedb.org/3/movie/popular?api_key=63103295acf92a181b68289f20ca0e98&language=en-US&page=1";

                Type ="F";
              //  Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                call(istab,Type);
                return true;
            case R.id.item2:
              url = "https://api.themoviedb.org/3/movie/popular?api_key=63103295acf92a181b68289f20ca0e98&language=en-US&page=1";
Type="P";
            //    Toast.makeText(getApplicationContext(),"Item 2 Selected",Toast.LENGTH_LONG).show();
                call(istab,Type);
                return true;
            case R.id.item3:
                url = "https://api.themoviedb.org/3/movie/top_rated?api_key=63103295acf92a181b68289f20ca0e98&language=en-US&page=1";

                Type="T";
               // Toast.makeText(getApplicationContext(),"Item 3 Selected", Toast.LENGTH_LONG).show();
                call(istab,Type);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    String url =null;

    private boolean istab;

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("Broadcast");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean value =  intent.getExtras().getBoolean("State");
                if (value){
                Toast.makeText(context, "Online Now", Toast.LENGTH_SHORT).show();}
                else {Toast.makeText(context, "Offline Now", Toast.LENGTH_SHORT).show();}
            }
        };
        registerReceiver(receiver, filter);
    }
    String Type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState!=null) {
            url = savedInstanceState.getString("Link");
            Type=savedInstanceState.getString("Type");
        }
        else {
            url = "https://api.themoviedb.org/3/movie/popular?api_key=63103295acf92a181b68289f20ca0e98&language=en-US&page=1";
       Type="P";
        }

        Toast.makeText(this, url, Toast.LENGTH_LONG).show();

        istab=!getResources().getBoolean(R.bool.istablet);
            call(istab,Type);


        sd = new FilmsViewFragment.passData() {
            @Override
            public void onDataPass(Film_Model f) {
                Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
                if (istab) {
                     Intent n = new Intent(MainActivity.this, Details.class);
                      n.putExtra("item",  f);
                      startActivity(n);
                     }
                     else {
                    call2(f);
                }
            }
        };


    }

    Details_Fragment d;
  void call2(final Film_Model film)
  {
      String url = "https://api.themoviedb.org/3/movie/"+film.getID()+"/reviews?api_key=63103295acf92a181b68289f20ca0e98";

      StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

          @Override
          public void onResponse(String response) {

            d=new Details_Fragment(film,response);

              getSupportFragmentManager()
                      .beginTransaction()
                      .replace(R.id.R,d)
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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getString("Link");
    }

    FilmsViewFragment f;
    int fragment;
    void call (final boolean istablet, final String Type)
    {
        if (istablet) {
            fragment = R.id.contentman;
        } else {
            fragment = R.id.L;
        }
        if (Type.equals("F"))
        {
            f = new FilmsViewFragment(this,"",Type);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(fragment, f)
                    .commit();
            f.setClickListener2(sd);

        }
            else
        {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    if (istablet) {
                        fragment = R.id.contentman;
                    } else {
                        fragment = R.id.L;
                    }
                    f = new FilmsViewFragment(getBaseContext(), response, Type);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(fragment, f)
                            .commit();

                    f.setClickListener2(sd);

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
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Link",url);
        outState.putString("Type",Type);
    }

/*
* @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        LayoutManager layoutManager = getLayoutManager();
        if(layoutManager != null && layoutManager instanceof LinearLayoutManager){
            mScrollPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
        SavedState newState = new SavedState(superState);
        newState.mScrollPosition = mScrollPosition;
        return newState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if(state != null && state instanceof SavedState){
            mScrollPosition = ((SavedState) state).mScrollPosition;
            LayoutManager layoutManager = getLayoutManager();
            if(layoutManager != null){
              int count = layoutManager.getChildCount();
              if(mScrollPosition != RecyclerView.NO_POSITION && mScrollPosition < count){
                  layoutManager.scrollToPosition(mScrollPosition);
              }
            }
        }
    }*/
}
