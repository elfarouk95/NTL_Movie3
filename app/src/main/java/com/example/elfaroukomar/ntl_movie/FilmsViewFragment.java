package com.example.elfaroukomar.ntl_movie;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;


public class FilmsViewFragment extends Fragment implements RecyclerViewAdpt.ItemClickListener {

   // Parcelable listState;
   // public final static String LIST_STATE_KEY ="KEYPostion";
    Context context;
    String Json;
    public FilmsViewFragment( ) {


    }

    String Type;
    public FilmsViewFragment(Context con,String json,String Type ) {
        context=con;
        Json=json;
        this.Type=Type;

        // Required empty public constructor
    }
    RecyclerView recyclerView;
    String json ="";
    boolean reach=true;
@Override
    public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    if (savedInstanceState!=null){
    Json= savedInstanceState.getString("JsonM");
    Type=savedInstanceState.getString("Type");
    //movieContnetProvider=new MovieContnetProvider();

    }


}

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState!=null)
        positionIndex=savedInstanceState.getInt("pos");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       // outState.putSerializable("con", (Serializable) context);
        outState.putString("JsonM",Json);
        outState.putString("Type",Type);
        outState.putInt("pos",positionIndex);
        //outState.putParcelable(LIST_STATE_KEY, staggeredGridLayoutManager.onSaveInstanceState());
    }

    ArrayList<Film_Model>movies;
    MovieContnetProvider movieContnetProvider;
    View v ;
    RecyclerViewAdpt adpter;
    DB_helper db;
     GridLayoutManager GridLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
         v= inflater.inflate(R.layout.fragment_films_view, container, false);
        context=container.getContext();
        adpter= new RecyclerViewAdpt(context);
        db = new DB_helper(context,1);


        try {

           if (Json!=""){movies = ParseMovieJson.parse(Json);
               try {


                   getContext().getContentResolver().insert(MovieContnetProvider.Content_uri, Convert(movies, Type));
                   db.Insert(movies, Type);
               }catch (Exception e){
                   Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
               }
           }
            movies= Cr(getContext().getContentResolver().query(MovieContnetProvider.Content_uri,null,Type,null,null,null));

                      recyclerView = (RecyclerView)v.findViewById(R.id.Recyclmovie);

                     adpter= new RecyclerViewAdpt(context,movies);

            GridLayoutManager= new GridLayoutManager(getContext(),2);

                    //final  GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
                    recyclerView.setLayoutManager(GridLayoutManager);

                     recyclerView.setAdapter(adpter);
            recyclerView.scrollToPosition(6);
                     adpter.setClickListener(this);
                      } catch (JSONException e) {
                             e.printStackTrace();
        }






        return v;
    }

    private passData p;

        public interface passData
        {
          public   void onDataPass(Film_Model f);
        }

    public void setClickListener2(FilmsViewFragment.passData itemClickListener) {
        this.p =  itemClickListener;
    }
    @Override
    public void onItemClick(View view, int position) {

      if (p!=null) p.onDataPass(movies.get(position));
        Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();

       // MainActivity m = new MainActivity();
       // m.next(movies.get(position),context);

      //  Toast.makeText(context, movies.get(position).getOriginal_title()+":"+movies.get(position).getID(), Toast.LENGTH_SHORT).show();
      /*  Intent n = new Intent(context,Details.class);
          n.putExtra("item", (Serializable) movies.get(position));
        startActivity(n);*/
    }

    private  int positionIndex ;
    private int topView;
    @Override
    public void onPause() {
        super.onPause();
        positionIndex= GridLayoutManager.findFirstVisibleItemPosition();
       View startView = recyclerView.getChildAt(0);
       topView = (startView == null) ? 0 : (startView.getTop() - recyclerView.getPaddingTop());
    }

    @Override
    public void onResume() {
        super.onResume();
       // Toast.makeText(context, "here", Toast.LENGTH_SHORT).show();
       // recyclerView.scrollToPosition(16);
       // recyclerView.smoothScrollToPosition(15);
        if (positionIndex!= -1) {
            GridLayoutManager.scrollToPositionWithOffset(positionIndex, 0);
       }
    }

    private ContentValues Convert(ArrayList<Film_Model>film_models, String T)
    {
        ContentValues values = new ContentValues();
        for (int i=0;i<movies.size();i++) {
            Film_Model film = movies.get(i);
            values.put("ID", film.getID());
            values.put("original_title", film.getOriginal_title());
            values.put("overview", film.getOverview());
            values.put("poster_path", film.getPoster_path());
            values.put("backdrop_path", film.getBackdrop_path());
            values.put("vote_average", film.getVote_average());
            values.put("release_date", film.getRelease_date());
            values.put("IsFav","0");
            values.put("Type",T);}
        return values;
    }

private ArrayList <Film_Model> Cr(Cursor cursor)
{
    ArrayList<Film_Model>film_models = new ArrayList<>();
    if (cursor != null)
    cursor.moveToFirst();
    while (!cursor.isAfterLast())
    { /*
         sqLiteDatabase.execSQL("CREATE TABLE Movie ( ID TEXT PRIMARY KEY,original_title TEXT" +
                ",overview TEXT,poster_path TEXT,backdrop_path TEXT,vote_average TEXT, release_date TEXT ,IsFav TEXT,Type TEXT" );
    }*/
        Film_Model f ;
        String original_title = cursor.getString(1);
        String poster_path = cursor.getString(3);
        String backdrop_path = cursor.getString(4);
        String overview = cursor.getString(2);
        String vote_average= cursor.getString(5);
        String release_date = cursor.getString(6);
        String ID =cursor.getString(0);
        f= new Film_Model(original_title,poster_path,backdrop_path,overview,vote_average,release_date,ID);
        f.setIsFav(cursor.getString(7));
        f.setType(cursor.getString(8));

        film_models.add(f);

        cursor.moveToNext();
    }
    return  film_models;


}


}
