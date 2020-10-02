package com.softech.client.View;




import com.softech.client.Model.Movie;

import java.util.ArrayList;

public class MoviePresenterViewContact {

    public interface View {

        void showMovieList(ArrayList<Movie> movies, int totalitems); // receive response to display
        void showLoadingError(String errMsg); // display error
    }

    // implemented by MoviesPresenter to handle user event
    public interface Presenter{
        void loadMoviewList(String query, int currentPage);
        void dropView();
    }


}
