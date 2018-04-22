package moera.ermais.google.com.popular_movies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import moera.ermais.google.com.popular_movies.model.Movie;
import moera.ermais.google.com.popular_movies.utilits.MovieUtils;
import moera.ermais.google.com.popular_movies.utilits.NetworkUtils;

import static moera.ermais.google.com.popular_movies.data.MovieContract.MovieEntry.COLUMN_IMAGE;
import static moera.ermais.google.com.popular_movies.data.MovieContract.MovieEntry.COLUMN_MOVIE_ID;
import static moera.ermais.google.com.popular_movies.data.MovieContract.MovieEntry.COLUMN_OVERVIEW;
import static moera.ermais.google.com.popular_movies.data.MovieContract.MovieEntry.COLUMN_POPULARITY;
import static moera.ermais.google.com.popular_movies.data.MovieContract.MovieEntry.COLUMN_RELEASE_DATE;
import static moera.ermais.google.com.popular_movies.data.MovieContract.MovieEntry.COLUMN_TITLE;
import static moera.ermais.google.com.popular_movies.data.MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE;
import static moera.ermais.google.com.popular_movies.data.MovieContract.MovieEntry.COLUMN_VOTE_COUNT;
import static moera.ermais.google.com.popular_movies.data.MovieContract.MovieEntry.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        LoaderManager.LoaderCallbacks<List<Movie>> {
    private TextView mErrorMessageDisplay;
    private MoviesDataAdapter moviesDataAdapter;
    private GridView mGridView;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int page = 1;
    private static final int MOVIE_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mGridView = findViewById(R.id.recycler_view_movies);
        mGridView.setNumColumns(2);
        mGridView.setDrawingCacheEnabled(true);

        moviesDataAdapter = new MoviesDataAdapter(this);
        mGridView.setAdapter(moviesDataAdapter);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    menuItem.setChecked(true);
                    setTitle(menuItem);
                    resetMovies(1, menuItem.getItemId());
                    mDrawerLayout.closeDrawers();
                    return true;
                });

        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.post(() -> resetMovies(page));

        mGridView.setOnItemClickListener((parent, v, position, id) -> {
            Context context = getApplicationContext();
            Class destination = DetailActivity.class;
            Intent intent = new Intent(context, destination);
            intent.putExtra(Intent.EXTRA_INTENT, (Movie) moviesDataAdapter.getItem(position));
            startActivity(intent);
        });

        setTitle((MenuItem) null);
    }

    private void setTitle(MenuItem menuItem) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(
                    menuItem != null ? menuItem.getTitle() :
                            mNavigationView.getMenu().findItem(getSelectedMenuItemId()).getTitle());
    }

    private void loadMoviesData(int type, int page) {
        mSwipeRefreshLayout.setRefreshing(true);
        Bundle args = new Bundle();
        args.putInt(getResources().getString(R.string.type), type);
        args.putInt(getResources().getString(R.string.page), page);

        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, args, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.list_movies, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                resetMovies(page);
                return true;
            case R.id.next_page:
                loadMoviesData(getSelectedMenuItemId(), ++page);
                return true;
            case R.id.prev_page:
                page = page == 1 ? page : page - 1;
                loadMoviesData(getSelectedMenuItemId(), page);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Cleanup movies data and reload first page
     */
    private void resetMovies(int page) {
        this.page = page;
        loadMoviesData(getSelectedMenuItemId(), page);
    }

    /**
     * Cleanup movies data and reload first page with selected menu item id
     *
     * @param page       page number to load
     * @param menuItemId selected menu item id
     */
    private void resetMovies(int page, int menuItemId) {
        this.page = page;
        loadMoviesData(menuItemId, page);
    }

    /**
     * Get selected item id of {@link MainActivity#mNavigationView} menu
     *
     * @return selected item id of {@link MainActivity#mNavigationView} menu
     * <br/>If there is no selected item, returns first item id
     * <br/>If there is no items, returns -1
     */
    private int getSelectedMenuItemId() {
        int result = -1;

        if (mNavigationView != null && mNavigationView.getMenu() != null) {
            Menu menu = mNavigationView.getMenu();

            for (int i = 0; i < menu.size(); i++) {
                if (menu.getItem(i).isChecked()) {
                    result = menu.getItem(i).getItemId();
                    break;
                }
            }

            if (result == -1 && menu.size() > 0) {
                result = menu.getItem(0).getItemId();
            }
        }

        return result;
    }

    @Override
    public void onRefresh() {
        resetMovies(page);
    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {

            List<Movie> mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public List<Movie> loadInBackground() {
                int type = args.getInt(getResources().getString(R.string.type));

                List<Movie> movies = new ArrayList<>();
                if (type == R.id.favorite) {
                    Cursor mCursor = getAllFavoriteMovies();
                    while (mCursor.moveToNext()) {
                        Movie movie = new Movie(mCursor.getString(
                                mCursor.getColumnIndex(COLUMN_TITLE)),
                                mCursor.getInt(mCursor.getColumnIndex(COLUMN_VOTE_COUNT)),
                                mCursor.getDouble(mCursor.getColumnIndex(COLUMN_VOTE_AVERAGE)),
                                mCursor.getDouble(mCursor.getColumnIndex(COLUMN_POPULARITY)),
                                mCursor.getBlob(mCursor.getColumnIndex(COLUMN_IMAGE)),
                                mCursor.getString(mCursor.getColumnIndex(COLUMN_RELEASE_DATE)),
                                mCursor.getString(mCursor.getColumnIndex(COLUMN_OVERVIEW)),
                                mCursor.getInt(mCursor.getColumnIndex(COLUMN_MOVIE_ID)));
                        movies.add(movie);
                    }
                    mCursor.close();


                } else {
                    Map<String, Object> information = new TreeMap<>();
                    information.put(getResources().getString(R.string.type), type);
                    information.put(getResources().getString(R.string.page), args.getInt(getResources().getString(R.string.page)));
                    information.put("Context", getApplicationContext());
                    URL moviesRequestUrl = NetworkUtils.buildUrl(information);

                    try {
                        String jsonMoviesResponse = NetworkUtils
                                .getResponseFromHttpUrl(moviesRequestUrl);

                        movies = MovieUtils
                                .getSimpleMovieStringsFromJson(getApplicationContext(), jsonMoviesResponse);
                    } catch (Exception e) {
                        Log.e("Error fetching movies data", e.getMessage());
                    }
                }
                return movies;

            }

            public void deliverResult(List<Movie> data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        moviesDataAdapter.setMoviesData(data);
        if (data == null) {
            showErrorMessage();
        } else {
            showMoviesData();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {

    }


    private void showErrorMessage() {
        mGridView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showMoviesData() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mGridView.setVisibility(View.VISIBLE);
    }

    private Cursor getAllFavoriteMovies() {
        Uri uri = CONTENT_URI;
        uri = uri.buildUpon().build();
        return getContentResolver().query(uri, null, null, null, null);
    }
}