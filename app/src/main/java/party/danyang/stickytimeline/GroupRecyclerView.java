package party.danyang.stickytimeline;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import party.danyang.stickytimeline.data.DataObserver;

/**
 * Created by dream on 16-11-29.
 */

public class GroupRecyclerView extends FrameLayout {
    private static final String TAG = "GroupRecyclerView";

    private Context mContext;

    private RecyclerView mRecy;
    private LinearLayoutManager mLayoutManager;
    private GroupAdapter mAdapter;
    private RecyclerView.ViewHolder mStickyViewHolder;
    private GroupEntity mStickyGroup;
    private boolean mStickyEnable;

    private DataObserver mDataSetObserver;

    public GroupRecyclerView(Context context) {
        this(context, null);
    }

    public GroupRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GroupRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

        if (mContext instanceof Activity) {
            ((Activity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }

        mRecy = new RecyclerView(context);
        mRecy.setVerticalScrollBarEnabled(false);
        addView(mRecy, new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        mLayoutManager = new LinearLayoutManager(context);
        mRecy.setLayoutManager(mLayoutManager);
        mRecy.setHasFixedSize(true);
        initListener();
    }

    public void initListener() {
        mRecy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!mStickyEnable) return;
                int firstItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                if (mStickyViewHolder != null && mAdapter.getItemCount() > firstItemPosition) {
                    boolean isGroupType = mAdapter.isGroupType(firstItemPosition);
                    int groupPosition = mAdapter.getGroupPosition(firstItemPosition);
                    GroupEntity group = mAdapter.getGroup(groupPosition);

                    if (!group.shouldShow() && mStickyViewHolder.itemView.getVisibility() == VISIBLE) {
                        mStickyGroup = null;
                        mStickyViewHolder.itemView.setVisibility(INVISIBLE);
                    }

                    if (groupPosition + 1 < mAdapter.getGroupCount()) {
                        int nextFirstVisibleGroupPosition = mAdapter.getGroupPosition(firstItemPosition + 1);
                        //不一样的group---> change
                        if (nextFirstVisibleGroupPosition != groupPosition) {
                            View nextFirstVisibleGroupView = mLayoutManager.findViewByPosition(firstItemPosition + 1);
                            if (nextFirstVisibleGroupView.getTop() <= mStickyViewHolder.itemView.getHeight() && group.shouldShow()) {
                                mStickyViewHolder.itemView.setTranslationY(
                                        nextFirstVisibleGroupView.getTop() - mStickyViewHolder.itemView.getHeight());
                            }
                            stickyNewViewHolder(mAdapter, group);
                        } else if (mStickyViewHolder.itemView.getTranslationY() != 0) {
                            mStickyViewHolder.itemView.setTranslationY(0);
                        }
                    }

                    if (isGroupType) {
                        stickyNewViewHolder(mAdapter, group);
                    } else if (dy < 0 && mStickyViewHolder.itemView.getVisibility() != VISIBLE) {
                        View nextGroupView = mLayoutManager.findViewByPosition(firstItemPosition);
                        if (nextGroupView.getBottom() >= mStickyViewHolder.itemView.getHeight()) {
                            stickyNewViewHolder(mAdapter, group);
                        }
                    }
                }
            }
        });
    }

    private <TC, TG extends GroupEntity<TC>> void stickyNewViewHolder(GroupAdapter<TC, TG> adapter, TG group) {
        if (group.shouldShow() && !group.equals(mStickyGroup)) {
            if (mStickyViewHolder.itemView.getVisibility() != VISIBLE) {
                mStickyViewHolder.itemView.setVisibility(VISIBLE);
            }
            mStickyGroup = group;
            adapter.onBindGroupViewHolder(mStickyViewHolder, group);
        }
    }

    private <TC, TG extends GroupEntity<TC>> void initStickyView(final GroupAdapter<TC, TG> adapter) {
        mStickyViewHolder = adapter.onCreateGroupViewHolder(mRecy);

        mStickyViewHolder.itemView.setOnClickListener(null);////

        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) == mRecy) {
                mStickyViewHolder.itemView.setVisibility(INVISIBLE);
                addView(mStickyViewHolder.itemView, i + 1);
                return;
            }
        }
    }

    public <TC, TG extends GroupEntity<TC>> void setAdapter(GroupAdapter<TC, TG> adapter) {
        this.mAdapter = adapter;
        mRecy.setAdapter(adapter);
        if (mStickyEnable) {
            initStickyView(adapter);
        }

        mDataSetObserver = new DataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                int firstItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                GroupEntity ge = mAdapter.getGroup(mAdapter.getGroupPosition(firstItemPosition));
                if (!mStickyGroup.equals(ge)) {
                    stickyNewViewHolder(mAdapter, ge);
                }
            }

            @Override
            public void onInited() {
                super.onInited();
            }

            @Override
            public void onSetListener(int type) {
                super.onSetListener(type);
            }
        };
        mAdapter.registerDataSetObserver(mDataSetObserver);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAdapter.unregisterDataSetObserver(mDataSetObserver);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAdapter.registerDataSetObserver(mDataSetObserver);
    }

    public void setStickyEnable(boolean enable) {
        this.mStickyEnable = enable;
    }

    public RecyclerView getRecyclerView() {
        return mRecy;
    }
}
