package com.example.elfaroukomar.ntl_movie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Elfarouk Omar on 27/08/2017.
 */

public class RecyclerViewAdpt extends RecyclerView.Adapter<RecyclerViewAdpt.VH> {

     Context context;
    ArrayList<Film_Model>movies;
    private ItemClickListener mClickListener;
    public RecyclerViewAdpt(Context con,ArrayList<Film_Model>Movies) {

        context=con;
        movies=Movies;
    }
    public RecyclerViewAdpt(Context con) {

        context=con;

    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.filmit,parent,false);
        VH vh = new VH(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(VH holder, int position) {

        Picasso.with(context).load("http://image.tmdb.org/t/p/w300"+movies.get(position).poster_path).into(holder.poster);
       holder.moviename.setText(movies.get(position).getOriginal_title());
     holder.rate.setText(movies.get(position).getVote_average());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }



    class VH extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView poster;
        TextView moviename;
        TextView rate;
        public VH(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.poste);
            moviename=(TextView)itemView.findViewById(R.id.movienam);
            rate=(TextView)itemView.findViewById(R.id.rat);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getPosition());
        }
    }
    // convenience method for getting data at click position
    public Film_Model getItem(int id) {
        return movies.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener =  itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
