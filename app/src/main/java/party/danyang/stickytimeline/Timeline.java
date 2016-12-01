package party.danyang.stickytimeline;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by dream on 16-11-29.
 */

public class Timeline extends RecyclerView {
    private static final String TAG = "Timeline";

    private Context context;

    private LinearLayoutManager mLayoutManager;
    private TimelineAdapter mAdapter;
    private ViewHolder stickyHeaderViewHolder;
    private String stickyHeaderTag;
    private boolean mSticyEnable;

    public Timeline(Context context) {
        this(context, null);
    }

    public Timeline(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Timeline(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mLayoutManager = new LinearLayoutManager(context);
        setLayoutManager(mLayoutManager);
        setHasFixedSize(true);
        setAdapter(mAdapter);
        initListener();
    }

    public void initListener() {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                if (!mSticyEnable) return;

            }
        });
    }

    public void setAdapter(TimelineAdapter adapter) {
        super.setAdapter(adapter);
    }

    public void setmSticyEnable(boolean enable) {
        this.mSticyEnable = enable;
    }
}
