package be.david.mangaapp;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Movie;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.omertron.themoviedbapi.model.movie.MovieInfo;

import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView title;
    private TextView description;
    private MovieListAdapter movieListAdapter;
    private MovieInfo movie;
    private TextView release;
    private TextView origLang;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent i = getIntent();

        movie = (MovieInfo) i.getExtras().get("Movie");

//        Log.v("ERROOOOOOOOOOR", "" + movie.toString());

        imageView = (ImageView) findViewById(R.id.imageDetailView);
        title = (TextView) findViewById(R.id.detailTitle);
        description = (TextView) findViewById(R.id.detailDescription);
        release = (TextView) findViewById(R.id.detailRelease);
        origLang = (TextView) findViewById(R.id.detailOrigLang);

        title.setText(movie.getTitle());
        description.setText(movie.getOverview());
        release.setText("Release: " + movie.getReleaseDate());
        origLang.setText("OG: " + movie.getOriginalLanguage());



        URL imageUrl = AppController.getInstance().createImageUrl(movie.getBackdropPath(), "w780");
        ImageLoader.getInstance().displayImage(imageUrl.toString(), imageView);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // Respond to the action bar's Up/Home button
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
