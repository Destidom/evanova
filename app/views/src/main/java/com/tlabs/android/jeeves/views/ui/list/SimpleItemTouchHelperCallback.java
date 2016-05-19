package com.tlabs.android.jeeves.views.ui.list;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    public static final float ALPHA_FULL = 1.0f;

    private final ItemTouchHelperAdapter adapter;
    private final boolean isItemSwipeEnabled;
    private final boolean isLongPressDragEnabled;

    public SimpleItemTouchHelperCallback(final ItemTouchHelperAdapter adapter) {
        this(adapter, true, true);
    }

    public SimpleItemTouchHelperCallback(
            final ItemTouchHelperAdapter adapter,
            final boolean isItemSwipeEnabled,
            final boolean isLongPressDragEnabled) {

        this.adapter = adapter;
        this.isItemSwipeEnabled = isItemSwipeEnabled;
        this.isLongPressDragEnabled = isLongPressDragEnabled;
    }

    public boolean canDrag(final RecyclerView.ViewHolder holder) {
        return this.isLongPressDragEnabled;
    }

    public boolean canSwipe(final RecyclerView.ViewHolder holder) {
        return this.isItemSwipeEnabled;
    }

    @Override
    public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
        return this.adapter.canMove(current.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isLongPressDragEnabled;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return isItemSwipeEnabled;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = canDrag(viewHolder) ? ItemTouchHelper.UP | ItemTouchHelper.DOWN : 0;
        final int swipeFlags = canSwipe(viewHolder) ? ItemTouchHelper.START | ItemTouchHelper.END : 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
        return 1.0f;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        if (!adapter.canMove(source.getAdapterPosition(), target.getAdapterPosition())) {
            return false;
        }
        return adapter.onItemMoved(source.getAdapterPosition(), target.getAdapterPosition());
        //return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof DragRecyclerHolder) {
            View dragView = ((DragRecyclerHolder)viewHolder).getDragView();
            if (null != dragView) {
                dragView.setAlpha(ALPHA_FULL);
                dragView.setX(0);
            }
        }
        adapter.onItemDismissed(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;
            View dragView = null;
            if (viewHolder instanceof DragRecyclerHolder) {
                final DragRecyclerHolder drh = (DragRecyclerHolder)viewHolder;
                itemView = drh.getItemView();
                dragView = drh.getDragView();
            }

            // Fade out the view as it is swiped out of the parent's bounds
            final float dAlpha = Math.abs(dX) / (float)itemView.getWidth();
            itemView.setAlpha(ALPHA_FULL - dAlpha);
            itemView.setTranslationX(dX);
            if (null != dragView) {
                dragView.setVisibility(View.VISIBLE);
                dragView.setAlpha(dAlpha);
                dragView.setTranslationX(dX - itemView.getWidth());
            }
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
       /* if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            adapter.onItemSelected(viewHolder.getAdapterPosition());
        }*/

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        View itemView = viewHolder.itemView;
        View dragView = null;
        if (viewHolder instanceof DragRecyclerHolder) {
            final DragRecyclerHolder drh = (DragRecyclerHolder)viewHolder;
            itemView = drh.getItemView();
            dragView = drh.getDragView();
        }

        itemView.setAlpha(ALPHA_FULL);
        if (null != dragView) {
            dragView.setVisibility(View.INVISIBLE);
            dragView.setX(-1 * viewHolder.itemView.getWidth());
        }
      //  adapter.onItemCleared(viewHolder.getAdapterPosition());
    }
}