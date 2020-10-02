package com.softech.client.View;

import android.content.Context;

import com.softech.client.Model.Movie;
import com.softech.client.Network.NetworkListner;
import com.softech.client.Network.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoviePresenter implements MoviePresenterViewContact.Presenter, NetworkListner {
        // to keep reference to view
        private MoviePresenterViewContact.View view;

        private Context mContext;

        public MoviePresenter(MoviePresenterViewContact.View view, Context context) {
            this.view = view;
            mContext =context;
        }

        @Override
        public void dropView() {
            view = null;
        }

        // would be triggered by MovieListActivity
        @Override
        public void loadMoviewList(String query, int currentPage) {
            SingletonRequestQueue.getInstance(mContext).POSTStringAndJSONRequest(
                    mContext,this,null,query,currentPage);
        }

    @Override
    public void getResponse(JSONObject jsonObject) {

        ArrayList<Movie> movies = new ArrayList<>();
        String itemocunt= jsonObject.optString("totalResults");
        int totalitems=0;
        if(itemocunt==null || itemocunt.isEmpty()){

        }else {
             totalitems = Integer.parseInt(jsonObject.optString("totalResults"));
        }
         if(jsonObject!=null){
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("Search");
                if (jsonArray!=null){
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setTitle(jsonObject1.getString("Title"));
                        movie.setPoster(jsonObject1.getString("Poster"));
                        movie.setImdbID(jsonObject1.getString("imdbID"));
                        movie.setType(jsonObject1.getString("Type"));
                        movie.setYear(jsonObject1.getString("Year"));
                        movies.add(movie);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        view.showMovieList(movies,totalitems);
    }
}
