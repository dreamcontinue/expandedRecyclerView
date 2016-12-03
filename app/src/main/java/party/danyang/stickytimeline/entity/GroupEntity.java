package party.danyang.stickytimeline.entity;

import java.util.List;

/**
 * Created by dream on 16-11-30.
 */

public interface GroupEntity<T> {
    List<T> getChildrenList();

    boolean shouldShow();
}
