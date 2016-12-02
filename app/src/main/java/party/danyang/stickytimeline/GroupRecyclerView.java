package party.danyang.stickytimeline;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by dream on 16-11-29.
 */

public class GroupRecyclerView extends RecyclerView {
    private static final String TAG = "GroupRecyclerView";

    private Context context;

    private LinearLayoutManager mLayoutManager;
    private GroupAdapter mAdapter;
    private ViewHolder stickyHeaderViewHolder;
    private String stickyHeaderTag;
    private boolean mStickyEnable;

    public GroupRecyclerView(Context context) {
        this(context, null);
    }

    public GroupRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GroupRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
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

                if (!mStickyEnable) return;

            }
        });
    }

    public void setAdapter(GroupAdapter adapter) {
        super.setAdapter(adapter);
    }

    public void setmSticyEnable(boolean enable) {
        this.mStickyEnable = enable;
    }
}
