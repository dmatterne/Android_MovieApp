package be.david.mangaapp;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.enumeration.SearchType;
import com.omertron.themoviedbapi.model.config.Configuration;
import com.omertron.themoviedbapi.model.credits.MediaCreditCast;
import com.omertron.themoviedbapi.model.discover.Discover;
import com.omertron.themoviedbapi.model.media.MediaCreditList;
import com.omertron.themoviedbapi.model.movie.MovieBasic;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.results.ResultList;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import be.david.mangaapp.lab.ConnectivityDetector;

/**
 * Created by David on 25/10/2016.
 */

public class AppController extends Application {

    private static AppController instance;
    private List<MovieBasic> movieBasicList = new ArrayList<>();
    private List<MovieInfo> movieInfoList = new ArrayList<>();
    private final static boolean ALLOW_ADULT = true;
    private MovieInfo movieInfo = new MovieInfo();
    private String API_KEY;
    private final static int DISCOVER_ID = 1;
    private final static int ANIME_ID = 2;
    private final static int DISNEY_ID = 3;
    private Configuration configuration;
    private List<OnMovieListChangedListener> listeners = new ArrayList<>();
    private String query;
    private List<MediaCreditCast> cast = new ArrayList<>();
    private TheMovieDbApi api;
    private DBAdapter dbAdapter;



    private ConnectivityDetector cd;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        cd = new ConnectivityDetector(this);
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.ic_hourglass_empty_black_24dp)
                .showImageOnLoading(R.drawable.ic_hourglass_empty_black_24dp)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .denyCacheImageMultipleSizesInMemory()
                .build();

        ImageLoader.getInstance().init(config);

        API_KEY = getString(R.string.api);

        try {
            api = new TheMovieDbApi(API_KEY);
        } catch (MovieDbException e) {
            e.printStackTrace();
        }

        executeFetchConfiguration();

        movieBasicResults(DISCOVER_ID,true);


    }

    public static  synchronized AppController getInstance() {
        return instance;
    }

    public void executeFetchConfiguration() {


        FetchConfiguration fetchConfiguration = new FetchConfiguration();
        fetchConfiguration.execute();

    }

    public List<MovieBasic> getMovieBasicList() {

        return movieBasicList;
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

    public URL createImageUrl(String path, String size) {

        try {
            return this.configuration.createImageUrl(path, size);
        } catch (MovieDbException e) {
            e.printStackTrace();
        }

        return null;

    }



    public void movieInfoResult(int id) {

        FetchMovieDetails fetchMovieDetails = new FetchMovieDetails(id);

        fetchMovieDetails.execute();
    }

    public void movieBasicResults(int id, boolean clear) {

        FetchMovieBasic fetchMovieInfo = new FetchMovieBasic(clear,1,id);

        fetchMovieInfo.execute();

    }

    public void watchedMovieResults() {

        movieBasicList.clear();
        movieBasicList.addAll(dbAdapter.getAllMovies());
        notifyAllMovieListeners();

    }

    public void movieSearchResult( String text) {

        this.query = text;

        SearchMovieInfo searchMovieInfo = new SearchMovieInfo(text);

        searchMovieInfo.execute();


    }

    public void loadSearchMovieInfoByPage(int page) {

        FetchMovieBasic fetchMovieInfo = new FetchMovieBasic(false,page,DISCOVER_ID);

        fetchMovieInfo.execute();

    }

    public void fetchCast(int id){
        FetchCastFromMovie fetchItAgain = new FetchCastFromMovie();
        fetchItAgain.execute(id);
    }



    private class FetchMovieDetails extends AsyncTask<Void,Void,MovieInfo> {

        private int movieId;

        @Override
        protected void onPostExecute(MovieInfo movieInfos) {
            super.onPostExecute(movieInfos);



            Intent i = new Intent(getBaseContext(), MovieDetailActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("Movie", movieInfos);
//            Log.v("ERROOOOOOOOOOR", "" + movieInfos.getTitle());
            getBaseContext().startActivity(i);
        }

        @Override
        protected MovieInfo doInBackground(Void... params) {
            try {
//                Log.v("ERROOOOOOOOOOR", "" + movieId);
                return api.getMovieInfo(movieId,"en-US",null);
            } catch (MovieDbException e) {
                e.printStackTrace();
            }
            return null;
        }

        public FetchMovieDetails(int movieId) {
            this.movieId = movieId;
        }
    }

    private class SearchMovieInfo extends AsyncTask<Void,Void,ResultList<MovieInfo>> {

        private String queryText;

        @Override
        protected void onPostExecute(ResultList<MovieInfo> movieInfoResultList) {
            super.onPostExecute(movieInfoResultList);

            Log.v("Found", movieInfoResultList.toString());

            movieBasicList.clear();
            movieBasicList.addAll(movieInfoResultList.getResults());
            notifyAllMovieListeners();
        }

        @Override
        protected ResultList<MovieInfo> doInBackground(Void... params) {
            try {


                return api.searchMovie(queryText,1,"en-US",ALLOW_ADULT,null,null, SearchType.PHRASE);


            } catch (MovieDbException e) {
                e.printStackTrace();
            }

            return null;
        }

        public SearchMovieInfo( String text) {


            queryText = text;
        }
    }

    private class FetchMovieBasic extends AsyncTask<Void,Void,ResultList<MovieBasic>> {

        private boolean clear;
        private int selection;
        public int page;

        @Override
        protected ResultList<MovieBasic> doInBackground(Void... params) {


            try {
                if (selection == DISCOVER_ID) {

                    System.out.println("Fetching page:" + page);
                    Discover disc = new Discover().page(page);
                    return api.getDiscoverMovies(disc);
                }


                if (selection == DISNEY_ID) {
                    return api.getCompanyMovies(6421, "en-US",page);
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
            System.out.println(movieBasicResultList.getResults().size());
            System.out.println(movieBasicResultList);

            if (clear) {
                movieBasicList.clear();
            }
            movieBasicList.addAll(movieBasicResultList.getResults());
            notifyAllMovieListeners();
//            movieListAdapter.notifyDataSetChanged();
        }

        public FetchMovieBasic( boolean clear, int page, int selection) {
            this.clear = clear;
            this.page = page;
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

    private class FetchCastFromMovie extends AsyncTask<Integer, Void, MediaCreditList>{

        @Override
        protected MediaCreditList doInBackground(Integer... params) {
            try {

                return api.getMovieCredits(params[0].intValue());
            } catch (MovieDbException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MediaCreditList media) {
            super.onPostExecute(media);

            cast.clear();
            cast.addAll(media.getCast());
            notifyAllMovieListeners();

        }
    }


    public void saveMovie(String name, int tmdb_id, int score, String overview, boolean watched) {

        dbAdapter.insertMovie(name,tmdb_id,0 ,overview,watched);
    }

    public void removeMovie(int tmdb_id) {

        dbAdapter.removeFromWatchlist(tmdb_id);
    }

    public List<MediaCreditCast> getCast() {
        return cast;
    }

    public boolean isOnWatchList(int tmdb_id) {

       return dbAdapter.isOnWatchList(tmdb_id);

    }

    public ConnectivityDetector getConnectivityDetector() {
        return cd;
    }
}
