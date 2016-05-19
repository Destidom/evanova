package com.tlabs.android.jeeves.views.ui.list;

public interface ItemTouchHelperAdapter {

    boolean canMove(int fromPosition, int toPosition);

    boolean onItemMoved(int fromPosition, int toPosition);

    void onItemDismissed(int position);

}