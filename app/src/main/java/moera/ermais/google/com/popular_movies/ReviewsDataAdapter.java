package moera.ermais.google.com.popular_movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import moera.ermais.google.com.popular_movies.model.Review;


public class ReviewsDataAdapter extends BaseAdapter {

    private Context localContext;

    private List<Review> mRevewsData;

    ReviewsDataAdapter(Context ct) {
        this.localContext = ct;
    }

    @Override
    public int getCount() {
        return null == mRevewsData ? 0 : mRevewsData.size();

    }

    @Override
    public Object getItem(int i) {
        return mRevewsData != null && mRevewsData.size() > i ? mRevewsData.get(i) : null;

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;
        if (convertView == null) {
            view = ((LayoutInflater) localContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                    inflate(R.layout.review_list_item, viewGroup, false);
        } else {
            view = convertView;
        }

        Review review = mRevewsData.get(i);


        ((TextView) view.findViewById(R.id.author)).setText(review.getAuthor());
        ((TextView) view.findViewById(R.id.content)).setText(review.getContent());

        return view;

    }

    public void setReviewsData(List<Review> reviewsData) {
        if (reviewsData != null) {
            mRevewsData = reviewsData;
        } else {

            mRevewsData = new ArrayList<>();
        }
        notifyDataSetChanged();
    }
}
