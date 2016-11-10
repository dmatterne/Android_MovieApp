package be.david.mangaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.omertron.themoviedbapi.model.movie.MovieBasic;

import java.util.ArrayList;

import be.david.mangaapp.lab.ConnectivityDetector;

/**
 * Created by David on 3/11/2016.
 */

public class DBAdapter {


    private Context context;

    private MyDBHelper myDBHelper;

    private SQLiteDatabase db;

    private String DATABASE_NAME = "data";
    private final static String WATCHED_MOVIE_TABLE = "WATCHED_MOVIES";
    private final static String WATCHED_MOVIE_CLM_ID = "ID";
    private final static String WATCHED_MOVIE_CLM_TMDB_ID = "TMDB_ID";
    private final static String WATCHED_MOVIE_CLM_NAME = "NAME";
    private final static String WATCHED_MOVIE_CLM_SCORE = "SCORE";
    private final static String WATCHED_MOVIE_CLM_WATCHED = "WATCHED";
    private final static String WATCHED_MOVIE_CLM_OVERVIEW = "OVERVIEW";
    private final static int WATCHED = 1;
    private final static int NOT_WATCHED = 0;


    private int DATABSE_VERSION = 1;

    public DBAdapter(Context context) {
        this.context = context;

        myDBHelper = new MyDBHelper(context, DATABASE_NAME, null, DATABSE_VERSION);

    }

    public void open() {
        db = myDBHelper.getWritableDatabase();
    }


    public class MyDBHelper extends SQLiteOpenHelper {

        public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String query = "CREATE TABLE " + WATCHED_MOVIE_TABLE +
                    "( " + WATCHED_MOVIE_CLM_ID + " integer primary key autoincrement," +
                    WATCHED_MOVIE_CLM_TMDB_ID + " integer," +
                    WATCHED_MOVIE_CLM_NAME + " text," +
                    WATCHED_MOVIE_CLM_SCORE + " integer," +
                    WATCHED_MOVIE_CLM_OVERVIEW + " text," +
                    WATCHED_MOVIE_CLM_WATCHED + " integer);";
            db.execSQL(query);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            String query = "DROP TABLE IF EXISTS " + WATCHED_MOVIE_TABLE + ";";

            db.execSQL(query);

            onCreate(db);

        }


    }


    public void insertMovie(String name, int tmdb_id, int score, String overview, boolean watched) {

        ContentValues cv = new ContentValues();
        cv.put(WATCHED_MOVIE_CLM_NAME, name);
        cv.put(WATCHED_MOVIE_CLM_TMDB_ID, tmdb_id);
        cv.put(WATCHED_MOVIE_CLM_SCORE, score);
        cv.put(WATCHED_MOVIE_CLM_OVERVIEW, overview);
        if (watched) {
            cv.put(WATCHED_MOVIE_CLM_WATCHED, WATCHED);
        } else {
            cv.put(WATCHED_MOVIE_CLM_WATCHED, NOT_WATCHED);
        }

        db.insert(WATCHED_MOVIE_TABLE, null, cv);


    }

    public ArrayList<String> selectAllMovies() {

        ArrayList<String> moviesList = new ArrayList<>();

        Cursor cursor = db.query(WATCHED_MOVIE_TABLE, null, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {

            do {

                moviesList.add(cursor.getString(1));

            } while (cursor.moveToNext());

        }

        return moviesList;

    }

    public void removeFromWatchlist(int tmdb_id) {

        db.delete(WATCHED_MOVIE_TABLE, WATCHED_MOVIE_CLM_TMDB_ID + " = " + tmdb_id, null);

    }

    private void deleteAllStudents() {

        db.delete(WATCHED_MOVIE_TABLE, "", null);

    }

    public boolean isOnWatchList(int tmdb_id) {

        ArrayList<String> moviesList = new ArrayList<>();

        Cursor cursor = db.query(WATCHED_MOVIE_TABLE, null, WATCHED_MOVIE_CLM_TMDB_ID + " = " + tmdb_id, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {

            return true;

        } else {

            return false;

        }
    }

    public ArrayList<MovieBasic> getAllMovies() {

        ArrayList<MovieBasic> movies = new ArrayList<>();



        Cursor cursor = db.query(WATCHED_MOVIE_TABLE, new String[] { WATCHED_MOVIE_CLM_TMDB_ID
                , WATCHED_MOVIE_CLM_NAME, WATCHED_MOVIE_CLM_OVERVIEW}, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {

            do {
                MovieBasic basicMovie;
                basicMovie = new MovieBasic();
                basicMovie.setTitle(cursor.getString(1));
                basicMovie.setId(cursor.getInt(0));
                basicMovie.setOverview(cursor.getString(2));

                movies.add(basicMovie);

//                    moviesList.add(cursor.getString(1));

            } while (cursor.moveToNext());

        }

        return movies;

    }


}
