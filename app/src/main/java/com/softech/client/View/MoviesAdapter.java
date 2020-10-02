package com.softech.client.View;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.softech.client.Model.Movie;
import com.softech.client.R;


import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter {



    private final Context context;

    private ArrayList<Movie> movStrings = new ArrayList<>();
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private boolean isLoadingAdded = false;

    private static final String TAG = "MoviesAdapter";

    public MoviesAdapter(Context context, ArrayList<Movie> moview) {

        this.context = context;
        this.movStrings= moview;

    }



    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_model,parent,false);

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new MovieHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_model, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.layout, parent, false));

        }
        return  null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Log.e(TAG, "onBindViewHolder: "+position);
        ((MovieHolder)holder).movieTitle.setText(movStrings.get(position).getTitle());
        ((MovieHolder)holder).date.setText(movStrings.get(position).getYear());
        Glide.with(context)
                .load(movStrings.get(position).getPoster())
                .placeholder(R.drawable.ic_launcher_foreground)
                .timeout(6000)
                .into( ((MovieHolder)holder).iv_movie);
    }


    public void addLoading() {
        isLoaderVisible = true;
        movStrings.add(new Movie());
        notifyItemInserted(movStrings.size() - 1);
    }
    public void removeLoading() {
        isLoaderVisible = false;
        int position = movStrings.size() - 1;
        Movie item = movStrings.get(position);
        if (item != null) {
            movStrings.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void clear() {
        movStrings.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == movStrings.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }






    @Override

    public int getItemCount() {
             return movStrings == null ? 0 : movStrings.size();

    }
    public void add(Movie movie) {
        movStrings.add(movie);
        notifyItemInserted(movStrings.size() - 1);
    }

    public void addAll(ArrayList<Movie> movies) {
        for (Movie result : movies) {
             add(result);
        }
    }


    public static class MovieHolder extends RecyclerView.ViewHolder {

        TextView movieTitle;

        TextView date;
        LinearLayout moviesLayout;

        CircularImageView iv_movie;

        public MovieHolder(View v) {

            super(v);
            moviesLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            movieTitle = (TextView) v.findViewById(R.id.title);
            date = (TextView) v.findViewById(R.id.year);
            iv_movie = (CircularImageView) v.findViewById(R.id.iv_movie);
        }
    }
    private class ProgressHolder extends BaseViewHolder {

        private ProgressBar progressBar;
        public ProgressHolder(View inflate) {
            super(inflate);
        }

        @Override
        protected void clear() {

        }
    }
}