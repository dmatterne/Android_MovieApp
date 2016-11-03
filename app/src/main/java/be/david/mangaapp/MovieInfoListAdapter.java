package be.david.mangaapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.omertron.themoviedbapi.model.movie.MovieBasic;
import com.omertron.themoviedbapi.model.movie.MovieInfo;

import java.net.URL;
import java.util.List;

/**
 * Created by David on 24/10/2016.
 */

public class MovieInfoListAdapter extends RecyclerView.Adapter<MovieViewHolder> {



    private Context context;
    private List<MovieInfo> movieInfoList;

    public MovieInfoListAdapter(Context c, List<MovieInfo> movieList) {
        this.context = c;
        this.movieInfoList = movieList;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {

        final MovieInfo vl = movieInfoList.get(position);
//        ImageLoader.getInstance().displayImage(imageUrl.toString());
//        holder.getImageView().setImageResource(v.getPosterPath());
        holder.getTitle().setText(vl.getTitle());
        holder.getDescription().setText(vl.getOverview());
        holder.getReleaseYear().setText(vl.getReleaseDate());
        holder.getScore().setText(vl.getVoteCount() + "");

        Log.v("ERROOOOOOOOOOR", "" + vl.getId());

        holder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("ERROOOOOOOOOOR", "" + vl.getId());
            AppController.getInstance().movieInfoResult(vl.getId());
            }
        });

        URL imageUrl = AppController.getInstance().createImageUrl(vl.getPosterPath(), "w342");
        ImageLoader.getInstance().displayImage(imageUrl.toString(), holder.getImageView());


    }


    public Context getContext() {
        return context;
    }

    @Override
    public int getItemCount() {
        return movieInfoList.size();
    }



}
