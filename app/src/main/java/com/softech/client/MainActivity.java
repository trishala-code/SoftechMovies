package com.softech.client;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.softech.client.Model.Movie;
import com.softech.client.Network.NetworkUtils;
import com.softech.client.View.MoviePresenter;
import com.softech.client.View.MoviePresenterViewContact;
import com.softech.client.View.MoviesAdapter;
import com.softech.client.View.PaginationListener;

import java.util.ArrayList;

import static com.softech.client.View.PaginationListener.PAGE_START;


public class MainActivity extends AppCompatActivity implements MoviePresenterViewContact.View {
    private RecyclerView recyclerView;
    private MoviesAdapter moviesAdapter;
    private ProgressBar  progressBar;
    private MoviePresenterViewContact.Presenter presenter;
    private TextView tv_empty_msg;
    private SearchView searchView;

    private String searchString;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalitems = 10;
    private boolean isLoading = false;
    int itemCount = 0;
    LinearLayoutManager layoutManager;
    private int TOTAL_PAGES=5;
    private boolean isFromFirstPage=false;
    private boolean isFromFLastPage=false;
    private RelativeLayout tasksContainer;

private ArrayList<Movie> movieArrayList =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews(){

        presenter = new MoviePresenter(this,this);
        tasksContainer = findViewById(R.id.tasksContainer);
        moviesAdapter = new MoviesAdapter(this,movieArrayList);
        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_list); // list
        tv_empty_msg = (TextView)findViewById(R.id.swipe_msg_tv); // empty message
        progressBar = findViewById(R.id.progressbar);
        recyclerView.setHasFixedSize(true);
        layoutManager  = new LinearLayoutManager(this);
        searchView =findViewById(R.id.serachView);
        final LinearLayoutManager layout = new LinearLayoutManager(this);
       recyclerView.setLayoutManager(layout);// for vertical liner list
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(moviesAdapter);


        final Context context=this;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                    searchString=query;
                    isFromFirstPage=true;
                    currentPage=1;

                    movieArrayList.clear();
                    searchView.clearFocus();
                    searchView.setFocusable(false);

                if(NetworkUtils.isNetworkConnected(context)) {
                        presenter.loadMoviewList(query, currentPage);
                    }else{
                        showSnackBar("Please connect to internet");
                    }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                                                  return false;
                                              }
                                          });

        recyclerView.addOnScrollListener(new PaginationListener(layoutManager) {

            @Override
            public void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadLastPage();
            }
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        loadFirstPage();
    }

    private void showORHideListView(boolean flag){

        if (flag){
            tv_empty_msg.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        }else {
            tv_empty_msg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        }

    }

    @Override
    public void showMovieList(ArrayList<Movie> movies, int totalitems) {

        if(movies.isEmpty()){
                showSnackBar("Movies/Series List is not founded");
           }
        this.totalitems =totalitems;
        if (!movies.isEmpty()){

            showORHideListView(true);
            moviesAdapter.addAll(movies);
            moviesAdapter.notifyDataSetChanged();
            TOTAL_PAGES = totalitems/10;
            if (isFromFirstPage){
                progressBar.setVisibility(View.GONE);
                if (currentPage <= TOTAL_PAGES){ /*moviesAdapter.addLoadingFooter();*/}
                else isLastPage = true;
                progressBar.setVisibility(View.GONE);
            }
            if (isFromFLastPage){
                //moviesAdapter.removeLoadingFooter();
                isLoading = false;
                if (currentPage != TOTAL_PAGES) {/*moviesAdapter.addLoadingFooter();*/}
                else isLastPage = true;
            }
        }


    }
    private void loadLastPage(){
        isFromFLastPage=true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (searchString == null) {
                    presenter.loadMoviewList("batman",currentPage);
                } else {
                    presenter.loadMoviewList(searchString,currentPage);
                }
            }
        }, 1500);
    }
    private void loadFirstPage() {

        isFromFirstPage=true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (searchString == null) {
                    presenter.loadMoviewList("batman",currentPage);
                } else {
                    presenter.loadMoviewList(searchString,currentPage);

                }
            }
        }, 1500);
    }

    @Override
    public void showLoadingError(String errMsg) {
        hideProgressAndShowErr(errMsg);
        showORHideListView(false);

    }

    private void hideProgressAndShowErr(String msg){
        tv_empty_msg.setVisibility(View.VISIBLE);
        showORHideListView(false);
        showSnackBar(msg);
    }


    public void showSnackBar(String msg){
        Snackbar snackbar = Snackbar
                .make(tasksContainer, msg,
                        Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.RED);
        snackbar.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dropView();
    }

}
