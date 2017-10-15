package com.example.elfaroukomar.ntl_movie;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Elfarouk Omar on 15/10/2017.
 */

public class MovieContnetProvider extends ContentProvider {

    static final String ProvideName ="com.example.name.MovieApp";
    static final String URL ="content://"+ProvideName+"/Movie";
    static final Uri Content_uri =Uri.parse(URL);


    static final int Movies =1;
    static final int Movie_id=2;
    static UriMatcher uriMatcher;
    static {

        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ProvideName,"Movie",Movies);
        uriMatcher.addURI(ProvideName,"Movie/*",Movie_id);
    }
    private SQLiteDatabase db;
    @Override
    public boolean onCreate() {
        Context context=getContext();
        DB_helper db_helper =new DB_helper(context,1);
        db=db_helper.getWritableDatabase();

        return (db==null)?false:true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteQueryBuilder qb =new SQLiteQueryBuilder();
        qb.setTables("Movie");

        Cursor cursor;
        if (s.equals("F")) cursor = db.rawQuery("select * from Movie where IsFav ='1'", null);
        else
            cursor = db.rawQuery("select * from Movie where Type ='" + s + "'", null);

        getContext().getContentResolver().notifyChange(Content_uri, null);


        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        try {
            long Row_ID = db.insertWithOnConflict("Movie", null, contentValues,SQLiteDatabase.CONFLICT_IGNORE);
            if (Row_ID > 0) {
                Uri _uri = Content_uri.withAppendedPath(Content_uri, String.valueOf(Row_ID));
                getContext().getContentResolver().notifyChange(_uri, null);
                return _uri;

            }
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), "nonoo", Toast.LENGTH_SHORT).show();
        }




        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        try {

            long Row_ID = db.replace("Movie",null,contentValues);
            if (Row_ID > 0) {
                Uri _uri = Content_uri.withAppendedPath(Content_uri, String.valueOf(Row_ID));
                getContext().getContentResolver().notifyChange(_uri, null);


            }
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), "nono2o", Toast.LENGTH_SHORT).show();
        }

return  0;
    }
}
