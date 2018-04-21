package moera.ermais.google.com.popular_movies;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Машенька on 17.04.2018.
 */

public class TrailerListView extends ListView {
    public TrailerListView(Context context) {
        super(context);
    }

    public TrailerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TrailerListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
