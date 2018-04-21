package moera.ermais.google.com.popular_movies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Машенька on 01.04.2018.
 */

public class MovieContract {

    public static final String AUTHORITY = "moera.ermais.google.com.popular_movies";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_FAVORITE_MOVIES = "favorite_movies";


    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        /* Used internally as the name of our movie table. */
        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_IMAGE = "image";

    }
}
