package moera.ermais.google.com.popular_movies.utilits;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import moera.ermais.google.com.popular_movies.R;
import moera.ermais.google.com.popular_movies.model.Trailer;


public class TrailerUtils {

    public TrailerUtils() {
    }

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the movies.
     *
     * @param trailerJsonStr JSON response from server
     * @return Array of Strings describing movies data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static List<Trailer> getSimpleReviewStringsFromJson(Context context, String trailerJsonStr)
            throws JSONException {

        List<Trailer> parsedTrailersData;
        JSONObject jsonObject = new JSONObject(trailerJsonStr);
        JSONObject videosObject = jsonObject.getJSONObject(context.getResources().getString(R.string.videos));

        JSONArray trailersArray = videosObject.getJSONArray(context.getResources().getString(R.string.results_list));
        parsedTrailersData = new ArrayList<>();

        for (int i = 0; i < trailersArray.length(); i++) {
            /* Get the JSON object representing the day */
            JSONObject trailerJson = trailersArray.getJSONObject(i);

            Trailer trailer = new Trailer();
            /*
             * We ignore all the datetime values embedded in the JSON and assume that
             * the values are returned in-order by day (which is not guaranteed to be correct).
             */
            trailer.setId(trailerJson.getString(context.getResources().getString(R.string.id)));
            trailer.setKey(trailerJson.getString(context.getResources().getString(R.string.key)));
            trailer.setName(trailerJson.getString(context.getResources().getString(R.string.name)));
            trailer.setSite(trailerJson.getString(context.getResources().getString(R.string.site)));

            parsedTrailersData.add(trailer);
        }

        return parsedTrailersData;
    }
}
