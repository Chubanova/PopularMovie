package moera.ermais.google.com.popular_movies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import moera.ermais.google.com.popular_movies.model.Movie;
import moera.ermais.google.com.popular_movies.utilits.MovieUtils;


class MoviesDataAdapter extends BaseAdapter {
    private Context localContext;

    private List<Movie> mMoviesData;

    MoviesDataAdapter(Context ct) {
        this.localContext = ct;
    }

    @Override
    public int getCount() {
        return null == mMoviesData ? 0 : mMoviesData.size();
    }

    @Override
    public Object getItem(int i) {
        return mMoviesData != null && mMoviesData.size() > i ? mMoviesData.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(localContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(1, 8, 1, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        Movie movie = mMoviesData.get(i);
        if (movie.getPoster() != null) {
            Picasso.with(imageView.getContext())
                    .load(movie.getPoster())
                    .into(imageView);
        } else if (movie.getPosterByte() != null) {
            imageView.setImageBitmap(MovieUtils.getImage(movie.getPosterByte()));
        }
        return imageView;
    }

    public void setMoviesData(List<Movie> moviesData) {
        if (moviesData != null) {
            mMoviesData = moviesData;
        } else {
            mMoviesData = new ArrayList<>();
        }
        notifyDataSetChanged();
    }
}