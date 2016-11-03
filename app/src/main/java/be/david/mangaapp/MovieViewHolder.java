package be.david.mangaapp;

import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by David on 24/10/2016.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView title;
    private TextView description;
    private TextView releaseYear;
    private CardView cardView;
    private TextView score;

    public MovieViewHolder(View itemView) {
        super(itemView);

        cardView = (CardView) itemView.findViewById(R.id.cardView);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
        releaseYear = (TextView) itemView.findViewById(R.id.release);
        score = (TextView) itemView.findViewById(R.id.score);

    }



    public CardView getCardView() {
        return cardView;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getDescription() {
        return description;
    }

    public void setDescription(TextView description) {
        this.description = description;
    }

    public TextView getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(TextView releaseYear) {
        this.releaseYear = releaseYear;
    }

    public TextView getScore() {
        return score;
    }

    public void setScore(TextView score) {
        this.score = score;
    }
}
