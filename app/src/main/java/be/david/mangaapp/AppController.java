package be.david.mangaapp;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.config.Configuration;
import com.omertron.themoviedbapi.model.discover.Discover;
import com.omertron.themoviedbapi.model.movie.MovieBasic;
import com.omertron.themoviedbapi.results.ResultList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by David on 25/10/2016.
 */

public class AppController extends Application {

    private static AppController instance;
    private List<MovieBasic> movieBasicList = new ArrayList<>();
    private String API_KEY;
    private final static int DISCOVER_ID = 1;
    private final static int ANIME_ID = 2;
    private final static int DISNEY_ID = 3;
    private Configuration configuration;
    private List<OnMovieListChangedListener> listeners = new ArrayList<>();

    private TheMovieDbApi api;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        API_KEY = getString(R.string.api);

        try {
            api = new TheMovieDbApi(API_KEY);
        } catch (MovieDbException e) {
            e.printStackTrace();
        }

        executeFetchConfiguration();

        getMovieInfoResults(DISCOVER_ID);
    }

    public static  synchronized AppController getInstance() {
        return instance;
    }


    private class FetchMovieInfo extends AsyncTask<Void,Void,ResultList<MovieBasic>> {

        private int selection;

        @Override
        protected ResultList<MovieBasic> doInBackground(Void... params) {


            try {
                if (selection == DISCOVER_ID)

                    return api.getDiscoverMovies(new Discover());
                if (selection == DISNEY_ID) {
                    return api.getCompanyMovies(6421, "en-US",1);
                }

            } catch (MovieDbException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ResultList<MovieBasic> movieBasicResultList) {
            super.onPostExecute(movieBasicResultList);

            Log.v("Found", movieBasicResultList.toString());

            movieBasicList.clear();
            movieBasicList.addAll(movieBasicResultList.getResults());
            notifyAllMovieListeners();
//            movieListAdapter.notifyDataSetChanged();
        }

        public FetchMovieInfo(int selection) {
            this.selection = selection;
        }
    }

    private class FetchConfiguration extends AsyncTask<Void,Void, Configuration> {


        @Override
        protected Configuration doInBackground(Void... params) {

            try {
                return api.getConfiguration();
            } catch (MovieDbException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Configuration configuration) {

            AppController.this.configuration = configuration;
            notifyAllMovieListeners();

//            recyclerView.setAdapter(movieListAdapter);

        }



    }

    public TheMovieDbApi getApi() {
        return api;
    }

    public void setApi(TheMovieDbApi api) {
        this.api = api;
    }


    public void executeFetchConfiguration() {


        FetchConfiguration fetchConfiguration = new FetchConfiguration();
        fetchConfiguration.execute();

    }

    public void executeFetchMovie(int id) {

        FetchMovieInfo fetchMovieInfo = new FetchMovieInfo(id);

        fetchMovieInfo.execute();

    }

    public List<MovieBasic> getMovieBasicList() {



        return movieBasicList;
    }

    public void getMovieInfoResults(int id) {

         executeFetchMovie(id);

    }


    public Configuration getConfiguration() {
        return configuration;
    }

    public interface OnMovieListChangedListener {

        void onMovieListChanged();

    }

    private void notifyAllMovieListeners() {

        for (OnMovieListChangedListener listener: listeners) {

            listener.onMovieListChanged();

        }

    }

    public void addOnMovieListChangedListener(OnMovieListChangedListener onMovieListChangedListener) {

        listeners.add(onMovieListChangedListener);

    }

    public void removeOnMovieListChangedListener(OnMovieListChangedListener onMovieListChangedListener) {

        listeners.remove(onMovieListChangedListener);
    }



}
