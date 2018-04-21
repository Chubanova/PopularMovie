package moera.ermais.google.com.popular_movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import moera.ermais.google.com.popular_movies.model.Trailer;


public class TrailersDataAdapter extends BaseAdapter {

    private Context localContext;

    private List<Trailer> mTrailerData;

    public TrailersDataAdapter(Context localContext) {
        this.localContext = localContext;
    }

    @Override
    public int getCount() {
        return null == mTrailerData ? 0 : mTrailerData.size();

    }

    @Override
    public Object getItem(int i) {
        return mTrailerData != null && mTrailerData.size() > i ? mTrailerData.get(i) : null;

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
                    inflate(R.layout.trailers_list_view, viewGroup, false);
        } else {
            view = convertView;
        }

        Trailer trailer = mTrailerData.get(i);


        ((TextView) view.findViewById(R.id.name_trailer_tv)).setText(trailer.getName());

        return view;
    }

    public void setTrailersData(List<Trailer> trailersData) {
        if (trailersData != null) {
            mTrailerData = trailersData;
        } else {

            mTrailerData = new ArrayList<>();
        }
        notifyDataSetChanged();
    }
}
