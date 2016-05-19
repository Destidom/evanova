package com.tlabs.android.jeeves.views.ui.list;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.tlabs.android.jeeves.views.ui.animation.BlinkAnimation;


public class ItemTouchCallbackAdapter extends ItemTouchHelper.Callback {

    private RecyclerView.ViewHolder inDrag = null;

    private int from;
    private int to;

    public boolean canRemove(final int position) {
        return false;
    }

    public void doRemove(final int position) {}

    public boolean canMove(final int from, final int to) {
        return false;
    }

    public void doMove(final int from, final int to) {}

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int from = viewHolder.getAdapterPosition();

        if (canRemove(from)) {
            return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.END);
        }
        return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.ACTION_STATE_IDLE);
    }

    @Override
    public final boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
        from = current.getAdapterPosition();
        to = target.getAdapterPosition();
        if (from == to) {
            return false;
        }
        return canMove(from, to);
    }

    @Override
    public final void onSelectedChanged(RecyclerView.ViewHolder current, int actionState) {
        super.onSelectedChanged(current, actionState);
        if (null != inDrag && actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            doMove(from, to);
            inDrag.itemView.startAnimation(new BlinkAnimation(2, 300L));
            inDrag = null;
        }
        else if (null == inDrag && actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            inDrag = current;
        }
    }

    @Override
    public final void onSwiped(RecyclerView.ViewHolder current, int direction) {
        final int from = current.getAdapterPosition();
        doRemove(from);
    }

    @Override
    public final boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
        from = current.getAdapterPosition();
        to = target.getAdapterPosition();
        if (from == to) {
            return false;
        }
        return canMove(from, to);
    }

    @Override
    public final void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            onSwipeChildDraw(viewHolder, dX);
        }
        else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                    actionState, isCurrentlyActive);
        }
    }

    private void onSwipeChildDraw(RecyclerView.ViewHolder viewHolder, float dX) {
        float width = (float) viewHolder.itemView.getWidth();
        float alpha = 1.0f - Math.abs(dX) / width;
        viewHolder.itemView.setAlpha(alpha);
        viewHolder.itemView.setTranslationX(dX);
    }

}
