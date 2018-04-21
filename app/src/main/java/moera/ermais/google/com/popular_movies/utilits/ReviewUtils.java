package moera.ermais.google.com.popular_movies.utilits;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import moera.ermais.google.com.popular_movies.R;
import moera.ermais.google.com.popular_movies.model.Review;

/**
 * Created by Машенька on 07.04.2018.
 */

public class ReviewUtils {
    private ReviewUtils() {
        // Hide the implicit public one
    }

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the movies.
     *
     * @param reviewJsonStr JSON response from server
     * @return Array of Strings describing movies data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static List<Review> getSimpleReviewStringsFromJson(Context context, String reviewJsonStr)
            throws JSONException {

        List<Review> parsedReviewData;
        JSONObject reviewsJson = new JSONObject(reviewJsonStr);
        JSONArray reviewArray = reviewsJson.getJSONArray(context.getResources().getString(R.string.results_list));
        parsedReviewData = new ArrayList<>();

        for (int i = 0; i < reviewArray.length(); i++) {
            /* Get the JSON object representing the day */
            JSONObject reviewJson = reviewArray.getJSONObject(i);

            Review review = new Review();
            /*
             * We ignore all the datetime values embedded in the JSON and assume that
             * the values are returned in-order by day (which is not guaranteed to be correct).
             */
            review.setAuthor(reviewJson.getString(context.getResources().getString(R.string.author)));
            review.setContent(reviewJson.getString(context.getResources().getString(R.string.content)));
            review.setId(reviewJson.getString(context.getResources().getString(R.string.id)));
            review.setUrl(reviewJson.getString(context.getResources().getString(R.string.url)));


            parsedReviewData.add(review);
        }

        return parsedReviewData;
    }
}
