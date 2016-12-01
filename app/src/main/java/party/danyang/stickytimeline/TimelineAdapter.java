package party.danyang.stickytimeline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import party.danyang.stickytimeline.entity.GroupEntity;

/**
 * Created by dream on 16-11-29.
 */

public abstract class TimelineAdapter<TC, TG extends GroupEntity<TC>> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "TimelineAdapter";

    public enum TYPE {
        GROUP, CHILD
    }

    private static final int TYPE_GROUP = -1;
    private static final int TYPE_CHILD = -2;

    private List<TG> groupList = new ArrayList<>();

    private List<PositionEntity> positionInfo = new ArrayList<>();

//    private boolean isGroupComparable = false;
//    private boolean isChildComparable = false;
//    private Comparator<TG> groupComparator;
//    private Comparator<TC> childComparator;

    @Override
    public int getItemCount() {
        int sum = getGroupCount();
        for (int i = 0; i < getGroupCount(); i++) {
            sum += getChildrenCount(i);
        }
        return sum;
    }

    protected int getGroupCount() {
        return groupList.size();
    }

    protected int getChildrenCount(int groupPosition) {
        if (groupPosition >= getGroupCount()) {
            Log.e(TAG, "index out of list size");
            return 0;
        }
        if (groupList.get(groupPosition).getChildrenList() == null) {
            return 0;
        }
        return groupList.get(groupPosition).getChildrenList().size();
    }

    public TG getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    public TC getChild(int groupPosition, int childPosition) {
        return groupList.get(groupPosition).getChildrenList().get(childPosition);
    }

    public int getGroupId(int groupPosition) {
        return groupPosition;
    }

    public int getChildId(int groupPosition, int childPosition) {
        int childId = 0;
        for (int i = 0; i < groupPosition - 1; i++) {
            childId += groupList.get(i).getChildrenList().size() + 1;
        }
        childId += childPosition + 1;
        return childId;
    }

    public abstract RecyclerView.ViewHolder onCreateGroupViewHolder(ViewGroup parent);

    public abstract RecyclerView.ViewHolder onCreateChildViewHolder(ViewGroup parent);

    public abstract void onBindGroupViewHolder(RecyclerView.ViewHolder holder, TG group);

    public abstract void onBindChildViewHolder(RecyclerView.ViewHolder holder, TC child);

    public void onBindGroupViewHolder(RecyclerView.ViewHolder holder, int groupPosition) {
        onBindGroupViewHolder(holder, groupList.get(groupPosition));
    }

    public void onBindChildViewHolder(RecyclerView.ViewHolder holder, int groupPosition, int childPosition) {
        Log.e(TAG, "position (" + groupPosition + ", " + childPosition + ")");
        onBindChildViewHolder(holder, groupList.get(groupPosition).getChildrenList().get(childPosition));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_GROUP:
                return onCreateGroupViewHolder(parent);
            case TYPE_CHILD:
            default:
                return onCreateChildViewHolder(parent);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_GROUP) {
            Log.e(TAG, "position = " + position + "  type = group");
        } else {
            Log.e(TAG, "position = " + position + "  type = child");
        }
        switch (getItemViewType(position)) {
            case TYPE_GROUP:
                Log.e(TAG, "group position = " + getGroupPosition(position));
                onBindGroupViewHolder(holder, getGroupPosition(position));
                break;
            case TYPE_CHILD:
            default:
                int groupPosition = getGroupPosition(position);
                int childPosition = getChildPosition(position, groupPosition);
                Log.e(TAG, "group position = " + groupPosition + "  childPosition = " + childPosition);
                onBindChildViewHolder(holder, groupPosition, childPosition);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getPositionType(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setNewDatas(List<TG> newData) {
        groupList.clear();
        groupList.addAll(newData);
        initPositionInfo();

        Log.e(TAG, "group has " + newData.size());
        for (int i = 0; i < newData.size(); i++) {
            Log.e(TAG, "children has " + newData.get(i).getChildrenList().size());
        }
    }

    public void addGroupDatas(List<TG> newData) {
        groupList.addAll(newData);
    }

    public void addGroupData(TG newGroup) {
        groupList.add(newGroup);
    }

    public void addChildData(int groupPosition, TC newChild) {
        groupList.get(groupPosition).getChildrenList().add(newChild);
    }

    public void addChildrenDatas(int groupPosition, List<TC> newChildren) {
        groupList.get(groupPosition).getChildrenList().addAll(newChildren);
    }

    public void setNewChildrenDatas(int groupPosition, List<TC> newChildren) {
        List<TC> l = groupList.get(groupPosition).getChildrenList();
        l.clear();
        l.addAll(newChildren);
    }

//    public void initPositionInfo(){}
//
//    public void updatePositionInfo(){}
//
//    public void updatePositionInfo(int groupPosition){
//
//    }
//
//    public void updatePositionInfo(int groupPosition,int childPosition){
//    }
//
//    private class PositionEntity {
//        public static final int GROUP = -1;
//        public static final int CHILD = -2;
//
//        private int type;
//        private int groupPosition;
//        private int childPosition;
//
//        public PositionEntity() {
//        }
//
//        public PositionEntity(int groupPosition) {
//            this.groupPosition = groupPosition;
//            this.type = GROUP;
//        }
//
//        public PositionEntity(int groupPosition, int childPosition) {
//            this.groupPosition = groupPosition;
//            this.childPosition = childPosition;
//            this.type = CHILD;
//        }
//
//        public int getChildPosition() {
//            return childPosition;
//        }
//
//        public void setChildPosition(int childPosition) {
//            this.childPosition = childPosition;
//        }
//
//        public int getGroupPosition() {
//            return groupPosition;
//        }
//
//        public void setGroupPosition(int groupPosition) {
//            this.groupPosition = groupPosition;
//        }
//
//        public int getType() {
//            return type;
//        }
//
//        public void setType(int type) {
//            this.type = type;
//        }
//    }

    public int getGroupPosition(int combinePosition) {
        for (PositionEntity pe : positionInfo) {
            Log.e(TAG, "start position = " + pe.startPosition + "  end position = " + pe.endPosition);
            if (combinePosition >= pe.startPosition && combinePosition < pe.endPosition) {
                return pe.groupPosition;
            }
        }
        return -1;
    }

    public int getChildPosition(int combinePosition, int groupPosition) {
        Log.e(TAG, "combine position = " + combinePosition + "  group position = " + groupPosition);
        if (groupPosition >= getGroupCount()) return -1;
        if (combinePosition > positionInfo.get(groupPosition).startPosition &&
                combinePosition < positionInfo.get(groupPosition).endPosition) {
            return combinePosition - positionInfo.get(groupPosition).startPosition - 1;
        }
        return -1;
    }

    public int getChildPosition(int combinePosition) {
        int groupPosition = getGroupPosition(combinePosition);
        return getChildPosition(combinePosition, groupPosition);
    }

    public int getCombinePosition(int groupPosition) {
        for (PositionEntity pe : positionInfo) {
            if (groupPosition == pe.groupPosition) {
                return pe.startPosition;
            }
        }
        return -1;
    }

    public int getCombinePosition(int groupPosition, int childPosition) {
        for (PositionEntity pe : positionInfo) {
            if (groupPosition == pe.groupPosition) {
                if (pe.startPosition + childPosition >= pe.endPosition)
                    return -1;
                return pe.startPosition + childPosition;
            }
        }
        return -1;
    }

    public int getPositionType(int combinePosition) {
        for (PositionEntity pe : positionInfo) {
            if (pe.startPosition == combinePosition)
                return TYPE_GROUP;
        }
        return TYPE_CHILD;
    }

    public void initPositionInfo() {
        positionInfo.clear();
        updatePositionInfo();
    }

    public void updatePositionInfo(int groupPosition) {
        for (int i = groupPosition; i < getGroupCount(); i++) {
            int preEndPosition = i > 0 ? positionInfo.get(i - 1).endPosition : 0;
            PositionEntity pe = new PositionEntity(i, preEndPosition, getChildrenCount(i));
            positionInfo.add(pe);
        }
    }

    public void updatePositionInfo() {
        updatePositionInfo(0);
    }

    private class PositionEntity {
        int groupPosition;
        int sizeOfThisGroup;
        int startPosition;
        int endPosition;

        public PositionEntity() {
        }

        public PositionEntity(int groupPosition, int startPosition, int groupSize) {
            this.groupPosition = groupPosition;
            this.sizeOfThisGroup = groupSize;
            this.startPosition = startPosition;
            this.endPosition = startPosition + groupSize + 1;
        }
    }
}