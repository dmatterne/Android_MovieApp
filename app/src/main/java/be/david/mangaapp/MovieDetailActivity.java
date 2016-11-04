package be.david.mangaapp;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Movie;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.omertron.themoviedbapi.model.credits.MediaCreditCast;
import com.omertron.themoviedbapi.model.movie.MovieInfo;

import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity implements AppController.OnMovieListChangedListener {

    private ImageView imageView;
    private TextView title;
    private TextView description;
    private MovieListAdapter movieListAdapter;
    private MovieInfo movie;
    private TextView release;
    private TextView origLang;
    private DBAdapter dbAdapter;
    private LinearLayout myGallery;
    private int score;
//    private MenuItem removeWatchlist;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabRemove;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        dbAdapter = new DBAdapter(this);
        dbAdapter.open();

        Intent i = getIntent();
        movie = (MovieInfo) i.getExtras().get("Movie");

        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabRemove = (FloatingActionButton) findViewById(R.id.fabRemove);



        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbAdapter.insertMovie(movie.getTitle(),movie.getId(),0 ,movie.getOverview(),true);
                Snackbar.make(view, "Added to watchlist!", Snackbar.LENGTH_LONG)
                        .setAction("Action",null).show();
                currentMovieIsOnWatchList();

            }
        });

        fabRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbAdapter.removeFromWatchlist(movie.getId());
                Snackbar.make(view, "Removed from watchlist!", Snackbar.LENGTH_LONG)
                        .setAction("Action",null).show();
                currentMovieIsOnWatchList();

            }
        });



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);





        imageView = (ImageView) findViewById(R.id.imageDetailView);
        title = (TextView) findViewById(R.id.detailTitle);
        description = (TextView) findViewById(R.id.detailDescription);
        release = (TextView) findViewById(R.id.detailRelease);
        origLang = (TextView) findViewById(R.id.detailOrigLang);
        myGallery = (LinearLayout)findViewById(R.id.mygallery);

        title.setText(movie.getTitle());
        description.setText(movie.getOverview());
        release.setText("Release: " + movie.getReleaseDate());
        origLang.setText("OG: " + movie.getOriginalLanguage());

        currentMovieIsOnWatchList();

        URL imageUrl = AppController.getInstance().createImageUrl(movie.getBackdropPath(), "w780");
        ImageLoader.getInstance().displayImage(imageUrl.toString(), imageView);

        AppController.getInstance().fetchCast(movie.getId());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        getMenuInflater().inflate(R.menu.detail_main, menu);

//        removeWatchlist = menu.findItem(R.id.removeWatchlist);
//
//        currentMovieIsOnWatchList();


        return super.onCreateOptionsMenu(menu);
    }

    private void currentMovieIsOnWatchList() {
        if(dbAdapter.isOnWatchList(movie.getId())) {

            fabAdd.hide();
            fabRemove.show();

        } else {


            fabAdd.show();
            fabRemove.hide();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
//            case R.id.removeWatchlist:
//                dbAdapter.removeFromWatchlist(movie.getId());
//                currentMovieIsOnWatchList();
//                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieListChanged() {
        for (MediaCreditCast cast : AppController.getInstance().getCast()){
            myGallery.addView(insertCastPicture(cast.getArtworkPath()));
        }
    }

    private View insertCastPicture(String artworkPath) {
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(364, 480));
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (artworkPath != null) {
            ImageLoader.getInstance().displayImage(AppController.getInstance().createImageUrl(artworkPath, "w780").toString(), imageView);
        } else {
            ImageLoader.getInstance().displayImage(null, imageView);
        }
        return imageView;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().addOnMovieListChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppController.getInstance().removeOnMovieListChangedListener(this);
    }
}
