package moera.ermais.google.com.popular_movies.utilits;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import moera.ermais.google.com.popular_movies.R;
import moera.ermais.google.com.popular_movies.model.Movie;


public class MovieUtils {

    private MovieUtils() {
        // Hide the implicit public one
    }

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the movies.
     *
     * @param forecastJsonStr JSON response from server
     * @return Array of Strings describing movies data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static List<Movie> getSimpleMovieStringsFromJson(Context context, String forecastJsonStr)
            throws JSONException {

        List<Movie> parsedMoviesData;
        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray movieArray = forecastJson.getJSONArray(context.getResources().getString(R.string.results_list));
        parsedMoviesData = new ArrayList<>();

        for (int i = 0; i < movieArray.length(); i++) {
            /* Get the JSON object representing the day */
            JSONObject movieJson = movieArray.getJSONObject(i);
            Movie movie = new Movie();

            /*
             * We ignore all the datetime values embedded in the JSON and assume that
             * the values are returned in-order by day (which is not guaranteed to be correct).
             */
            movie.setTitle(movieJson.getString(context.getResources().getString(R.string.title)));
            movie.setOverview(movieJson.getString(context.getResources().getString(R.string.overview)));
            movie.setVoteAverage(movieJson.getDouble(context.getResources().getString(R.string.vote_average_json)));
            movie.setPoster(context, movieJson.getString(context.getResources().getString(R.string.poster_path)));
            movie.setReleaseDate(movieJson.getString(context.getResources().getString(R.string.release_date_json)));
            movie.setId(movieJson.getInt(context.getResources().getString(R.string.id)));

            parsedMoviesData.add(movie);
        }

        return parsedMoviesData;
    }

    /**
     * Compress {@link Bitmap} and convert it to byte array
     *
     * @param bitmap Some {@link Bitmap}
     * @return byte array with compressed {@link Bitmap} data
     */
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * Decode byte array to {@link Bitmap}
     *
     * @param imageData byte array with {@link Bitmap} data
     * @return decoded {@link Bitmap}
     */
    public static Bitmap getImage(byte[] imageData) {
        return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
    }
}
