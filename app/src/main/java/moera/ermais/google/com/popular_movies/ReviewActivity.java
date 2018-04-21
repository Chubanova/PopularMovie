package moera.ermais.google.com.popular_movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import moera.ermais.google.com.popular_movies.model.Movie;
import moera.ermais.google.com.popular_movies.model.Review;
import moera.ermais.google.com.popular_movies.utilits.NetworkUtils;
import moera.ermais.google.com.popular_movies.utilits.ReviewUtils;

public class ReviewActivity extends AppCompatActivity implements   SwipeRefreshLayout.OnRefreshListener,LoaderManager.LoaderCallbacks<List<Review>> {
    private TextView mErrorMessageDisplay;
    private ReviewsDataAdapter reviewsDataAdapter;
    private ListView mListView;
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final int REVIEW_LOADER_ID = 1;

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display_review);

        mListView = findViewById(R.id.recycler_view_reviews);
        mListView.setDrawingCacheEnabled(true);

        reviewsDataAdapter = new ReviewsDataAdapter(this);
        mListView.setAdapter(reviewsDataAdapter);

        mDrawerLayout = findViewById(R.id.drawer_layout_review);

        mSwipeRefreshLayout = findViewById(R.id.swipe_container_review);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        Intent movieData = getIntent();


        if (movieData != null && movieData.getParcelableExtra(Intent.EXTRA_INTENT) != null) {
            movie = movieData.getParcelableExtra(Intent.EXTRA_INTENT);

            Bundle args = new Bundle();
            args.putInt(getResources().getString(R.string.type), R.id.reviews);
            args.putParcelable(getResources().getString(R.string.movie), movie);

            getSupportLoaderManager().restartLoader(REVIEW_LOADER_ID, args, this);
        }

    }

    @NonNull
    @Override
    public Loader<List<Review>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<List<Review>>(this) {

            List<Review> mReviewData = null;

            @Override
            protected void onStartLoading() {
                if (mReviewData != null) {
                    deliverResult(mReviewData);
                } else {
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public List<Review> loadInBackground() {
                Map<String, Object> information = new TreeMap<>();
                information.put(getResources().getString(R.string.type), R.id.reviews);
                information.put(getResources().getString(R.string.movie), movie);
                information.put("Context", getApplicationContext());
                URL reviewsRequestUrl = NetworkUtils.buildUrl(information);

                try {
                    String jsonReviewsResponse = NetworkUtils
                            .getResponseFromHttpUrl(reviewsRequestUrl);

                    List<Review> reviews = ReviewUtils
                            .getSimpleReviewStringsFromJson(getApplicationContext(), jsonReviewsResponse);
                    return reviews;
                } catch (Exception e) {
                    Log.e("Error fetching reviews data", e.getMessage());
                    return new ArrayList<>();
                }

            }

            public void deliverResult(List<Review> data) {
                mReviewData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Review>> loader, List<Review> data) {

        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        reviewsDataAdapter.setReviewsData(data);
        if (data == null) {
            showErrorMessage();
        } else {
            showReviewsData();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Review>> loader) {

    }

    @Override
    public void onRefresh() {
        if (movie != null) {
            Bundle args = new Bundle();
            args.putInt(getResources().getString(R.string.type), R.id.reviews);
            args.putParcelable(getResources().getString(R.string.movie), movie);

            getSupportLoaderManager().restartLoader(REVIEW_LOADER_ID, args, this);
        }
    }

    private void showErrorMessage() {
        mListView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showReviewsData() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mListView.setVisibility(View.VISIBLE);
    }
}
