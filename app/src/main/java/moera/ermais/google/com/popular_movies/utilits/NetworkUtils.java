package moera.ermais.google.com.popular_movies.utilits;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

import moera.ermais.google.com.popular_movies.R;
import moera.ermais.google.com.popular_movies.model.Movie;


public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static java.net.URL buildUrl(Map<String, Object> params) {
        String typeOfSort;

        Context context = (Context) params.get("Context");
        Uri builtUri = Uri.EMPTY;
        Integer type = (Integer) params.get(context.getResources().getString(R.string.type));

        Integer page = (Integer) params.get(context.getResources().getString(R.string.page));

        Movie movie = (Movie) params.get(context.getResources().getString(R.string.movie));

        switch (type) {
            case R.id.highest_rated:
                builtUri = Uri.parse(context.getResources().getString(R.string.url_movie_top_rated)).buildUpon()
                        .appendQueryParameter(context.getResources().getString(R.string.api_key), context.getResources().getString(R.string.api_key_version_3))
                        .appendQueryParameter(context.getResources().getString(R.string.page), String.valueOf(page))
                        .build();
                break;
            case R.id.most_popular:
                builtUri = Uri.parse(context.getResources().getString(R.string.url_movie_popular)).buildUpon()
                        .appendQueryParameter(context.getResources().getString(R.string.api_key), context.getResources().getString(R.string.api_key_version_3))
                        .appendQueryParameter(context.getResources().getString(R.string.page), String.valueOf(page))
                        .build();
                break;
            case R.id.reviews:
                builtUri = Uri.parse(context.getResources().getString(R.string.url_movie) + movie.getId() + context.getResources().getString(R.string.reviews_url)).buildUpon()
                        .appendQueryParameter(context.getResources().getString(R.string.api_key), context.getResources().getString(R.string.api_key_version_3))
                        .build();
                Log.d(TAG, builtUri.toString());

                break;
            case R.id.name_trailer_tv:
                builtUri = Uri.parse(context.getResources().getString(R.string.url_movie) + movie.getId()).buildUpon()
                        .appendQueryParameter(context.getResources().getString(R.string.api_key), context.getResources().getString(R.string.api_key_version_3))
                        .appendQueryParameter(context.getResources().getString(R.string.append_to_response), context.getResources().getString(R.string.videos))
                        .build();
                break;
//                case R.id.favorite
            default:
                typeOfSort = context.getResources().getString(R.string.vote_average_desc);
                builtUri = Uri.parse(context.getResources().getString(R.string.url_discover_movie)).buildUpon()
                        .appendQueryParameter(context.getResources().getString(R.string.sort_by), typeOfSort)
                        .appendQueryParameter(context.getResources().getString(R.string.api_key), context.getResources().getString(R.string.api_key_version_3))
                        .appendQueryParameter(context.getResources().getString(R.string.page), String.valueOf(page))
                        .build();
                break;
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
        }
        Log.v(TAG, context.getResources().getString(R.string.built_URI) + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
