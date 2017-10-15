package com.example.elfaroukomar.ntl_movie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLData;
import java.util.ArrayList;

/**
 * Created by Elfarouk Omar on 13/10/2017.
 */

public class DB_helper extends SQLiteOpenHelper {


    public DB_helper(Context context, int version) {
        super(context, "MovieApp", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
/*
    String original_title;
    String poster_path;
    String backdrop_path;
    String overview;
    String vote_average;
    String release_date;
    String ID;*/
        sqLiteDatabase.execSQL("CREATE TABLE Movie ( ID TEXT PRIMARY KEY,original_title TEXT" +
                ",overview TEXT,poster_path TEXT,backdrop_path TEXT,vote_average TEXT, release_date TEXT ,IsFav TEXT,Type TEXT )" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void Insert (ArrayList<Film_Model> movies,String Type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
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
            values.put("Type",Type);
            db.insertWithOnConflict("Movie", null, values,SQLiteDatabase.CONFLICT_IGNORE);
        }


        // Inserting Row
          db.close();


    }


    public ArrayList<Film_Model> recive(String Type) {
        ArrayList<Film_Model> film_models = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if (Type.equals("F")) cursor = db.rawQuery("select * from Movie where IsFav ='1'", null);
        else
            cursor = db.rawQuery("select * from Movie where Type ='" + Type + "'", null);

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

public  void replacefav(Film_Model film,String IsFav){

    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("ID", film.getID());
    values.put("original_title", film.getOriginal_title());
    values.put("overview", film.getOverview());
    values.put("poster_path", film.getPoster_path());
    values.put("backdrop_path", film.getBackdrop_path());
    values.put("vote_average", film.getVote_average());
    values.put("release_date", film.getRelease_date());
    values.put("IsFav",IsFav);
    values.put("Type",film.getType());
    db.replace("Movie",null,values);
    db.close();

}


}

