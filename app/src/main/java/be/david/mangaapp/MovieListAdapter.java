package be.david.mangaapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.model.movie.MovieBasic;

import java.net.URL;
import java.util.List;

/**
 * Created by David on 24/10/2016.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieViewHolder>{

    private Context context;
    private List<MovieBasic> movieList;

    public MovieListAdapter(Context c, List<MovieBasic> movieList ) {
        this.context = c;
        this.movieList = movieList;


    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent,false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        MovieBasic v = movieList.get(position);


//        ImageLoader.getInstance().displayImage(imageUrl.toString());

//        holder.getImageView().setImageResource(v.getPosterPath());
        holder.getTitle().setText(v.getTitle());
        holder.getDescription().setText(v.getOverview());
        holder.getReleaseYear().setText(v.getReleaseDate());
        holder.getScore().setText(v.getVoteCount() + "");

        try {
            Log.v("ERROR", v.getPosterPath());

            //TODO: Create Create image in appcontroller
            URL imageUrl = AppController.getInstance().getConfiguration().createImageUrl(v.getPosterPath(),"w342");
            Log.v("ERROR", imageUrl.toString());
            ImageLoader.getInstance().displayImage(imageUrl.toString(),holder.getImageView());
        } catch (MovieDbException e) {
            e.printStackTrace();
        }




    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

}
