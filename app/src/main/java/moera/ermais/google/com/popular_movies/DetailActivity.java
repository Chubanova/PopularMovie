package moera.ermais.google.com.popular_movies;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import moera.ermais.google.com.popular_movies.data.MovieContract;
import moera.ermais.google.com.popular_movies.model.Movie;
import moera.ermais.google.com.popular_movies.model.Trailer;
import moera.ermais.google.com.popular_movies.utilits.MovieUtils;
import moera.ermais.google.com.popular_movies.utilits.NetworkUtils;
import moera.ermais.google.com.popular_movies.utilits.TrailerUtils;

import static moera.ermais.google.com.popular_movies.data.MovieContract.MovieEntry.CONTENT_URI;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Trailer>> {
    private Movie movie;

    private TextView overviewTV;
//    private TextView titleTV;
    private TextView voteAverageTV;
    private TextView releaseDateTV;
    private ImageView poster;

    private ListView mListView;


    private Button makeFavorite;
    private Button readReview;
    TrailersDataAdapter trailersDataAdapter;

    private static final int TRAILERS_LOADER_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        overviewTV = findViewById(R.id.tv_movie_overview);
//        titleTV = findViewById(R.id.tv_title);
        voteAverageTV = findViewById(R.id.tv_vote_average);
        releaseDateTV = findViewById(R.id.tv_release_date);
        poster = findViewById(R.id.image_iv);
        makeFavorite = findViewById(R.id.add_to_waitlist_button);
        readReview = findViewById(R.id.reviews);

        Intent movieData = getIntent();
        mListView = findViewById(R.id.list_view_trailers);


        trailersDataAdapter = new TrailersDataAdapter(this);
        mListView.setAdapter(trailersDataAdapter);

        if (isDeviceOnline()) {
            readReview.setVisibility(View.VISIBLE);
        } else {
            readReview.setVisibility(View.INVISIBLE);
        }

        if (movieData != null && movieData.getParcelableExtra(Intent.EXTRA_INTENT) != null) {
            movie = movieData.getParcelableExtra(Intent.EXTRA_INTENT);
            getSupportActionBar().setTitle(movie.getTitle());
            overviewTV.setText(movie.getOverview());
//            titleTV.setText(movie.getTitle());
            voteAverageTV.setText(String.valueOf(movie.getVoteAverage()));
            releaseDateTV.setText(movie.getReleaseDate());
            if (movie.getPoster() != null) {
                Picasso.with(poster.getContext())
                        .load(movie.getPoster())
                        .into(poster);
            } else if (movie.getPosterByte() != null) {
                poster.setImageBitmap(MovieUtils.getImage(movie.getPosterByte()));
            }
            styleFavoriteButton();

            Bundle args = new Bundle();
            args.putInt(getResources().getString(R.string.type), R.id.name_trailer_tv);
            args.putParcelable(getResources().getString(R.string.movie), movie);

            getSupportLoaderManager().restartLoader(TRAILERS_LOADER_ID, args, this);
        }

        mListView.setOnItemClickListener((parent, v, position, id) -> {
            Trailer trailer = (Trailer) trailersDataAdapter.getItem(position);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.url_youtube)).buildUpon()
                    .appendQueryParameter(getResources().getString(R.string.v), trailer.getKey())
                    .build());
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });
    }


    private void styleFavoriteButton() {
        String textFavoriteButton;
        int colorFavoriteButton;
        if (isFavoriteMovie(movie)) {
            textFavoriteButton = getResources().getString(R.string.remove_button_movie);
            colorFavoriteButton = getColor(R.color.colorAccent);
        } else {
            textFavoriteButton = getResources().getString(R.string.add_button_movie);
            colorFavoriteButton = getColor(R.color.colorPrimary);

        }
        makeFavorite.setText(textFavoriteButton);
        makeFavorite.setBackgroundColor(colorFavoriteButton);
    }

    private Intent createShareForecastIntent() {
        return ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(movie + String.valueOf(R.string.movies_app))
                .getIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareForecastIntent());
        return true;
    }


    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    public void addToFavoriteMovie(View view) {
        Boolean favoriteMovie = isFavoriteMovie(movie);
        if (favoriteMovie) {
            String stringId = Integer.toString(movie.getId());
            Uri uri = CONTENT_URI;
            uri = uri.buildUpon().appendPath(stringId).build();
            ContentResolver contentResolver = getContentResolver();
            new AsyncQueryHandler(contentResolver) {
            }.startDelete(1, null, uri, null, null);
        } else {
            addMovie(movie);
        }
        styleFavoriteButton();

    }

    private void addMovie(Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        cv.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        cv.put(MovieContract.MovieEntry.COLUMN_IMAGE, MovieUtils.getBytes(extractBitmapFromIV(poster)));
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        ContentResolver contentResolver = getContentResolver();
        new AsyncQueryHandler(contentResolver) {
        }.startInsert(1, null, CONTENT_URI, cv);
    }

    private Boolean isFavoriteMovie(Movie movie) {
        String stringId = Integer.toString(movie.getId());
        Uri uri = CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        Cursor c = getContentResolver().query(uri, null, null, null, null);
        boolean moveToFirst = c.moveToFirst();
        if (c != null) {
            c.close();
        }
        return moveToFirst;
    }

    private Bitmap extractBitmapFromIV(ImageView imageView) {
        Bitmap result;
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView.getDrawable());
        if (bitmapDrawable == null) {
            imageView.buildDrawingCache();
            result = imageView.getDrawingCache();
            imageView.buildDrawingCache(false);
        } else {
            result = bitmapDrawable.getBitmap();
        }
        return result;
    }


    public void readReviews(View view) {
        Context context = getApplicationContext();
        Class destination = ReviewActivity.class;
        Intent intent = new Intent(context, destination);
        intent.putExtra(Intent.EXTRA_INTENT, movie);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<List<Trailer>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<List<Trailer>>(this) {

            List<Trailer> mTrailerData = null;

            @Override
            protected void onStartLoading() {
                if (mTrailerData != null) {
                    deliverResult(mTrailerData);
                } else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public List<Trailer> loadInBackground() {
                Map<String, Object> information = new TreeMap<>();
                information.put(getResources().getString(R.string.type), R.id.name_trailer_tv);
                information.put(getResources().getString(R.string.movie), movie);
                information.put("Context", getApplicationContext());
                URL trailersRequestUrl = NetworkUtils.buildUrl(information);

                try {
                    String jsonTrailersResponse = NetworkUtils
                            .getResponseFromHttpUrl(trailersRequestUrl);

                    List<Trailer> trailers = TrailerUtils
                            .getSimpleReviewStringsFromJson(getApplicationContext(), jsonTrailersResponse);
                    return trailers;
                } catch (Exception e) {
                    Log.e("Error fetching reviews data", e.getMessage());
                    return new ArrayList<>();
                }

            }

            public void deliverResult(List<Trailer> data) {
                mTrailerData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Trailer>> loader, List<Trailer> data) {
        trailersDataAdapter.setTrailersData(data);

        // Calc listView height
        ListAdapter listAdapter = mListView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, mListView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        mListView.getLayoutParams().height = totalHeight + (mListView.getDividerHeight() * (listAdapter.getCount() - 1));
        mListView.requestLayout();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Trailer>> loader) {

    }

}
