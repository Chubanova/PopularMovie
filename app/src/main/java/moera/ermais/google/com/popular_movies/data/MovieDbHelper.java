package moera.ermais.google.com.popular_movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static moera.ermais.google.com.popular_movies.data.MovieContract.MovieEntry.COLUMN_IMAGE;
import static moera.ermais.google.com.popular_movies.data.MovieContract.MovieEntry.COLUMN_MOVIE_ID;
import static moera.ermais.google.com.popular_movies.data.MovieContract.MovieEntry.COLUMN_OVERVIEW;
import static moera.ermais.google.com.popular_movies.data.MovieContract.MovieEntry.COLUMN_POPULARITY;
import static moera.ermais.google.com.popular_movies.data.MovieContract.MovieEntry.COLUMN_RELEASE_DATE;
import static moera.ermais.google.com.popular_movies.data.MovieContract.MovieEntry.COLUMN_TITLE;
import static moera.ermais.google.com.popular_movies.data.MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE;
import static moera.ermais.google.com.popular_movies.data.MovieContract.MovieEntry.COLUMN_VOTE_COUNT;

/**
 * Created by Машенька on 01.04.2018.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    /*
     * This is the name of our database. Database names should be descriptive and end with the
     * .db extension.
     */
    public static final String DATABASE_NAME = "movie.db";


    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     *
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our weather data.
         */
        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +

                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        COLUMN_RELEASE_DATE + " BLOB, " +
                        COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                        COLUMN_VOTE_COUNT + " REAL NOT NULL, " +
                        COLUMN_TITLE + " TEXT NOT NULL, " +
                        COLUMN_POPULARITY + " REAL NOT NULL, " +
                        COLUMN_OVERVIEW + " TEXT NOT NULL ," +
                        COLUMN_IMAGE + " BLOB NOT NULL " + ");";

        /*
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         */
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
