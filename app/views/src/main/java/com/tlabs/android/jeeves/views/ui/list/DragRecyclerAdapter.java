package com.tlabs.android.jeeves.views.ui.list;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public abstract class DragRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends SelectableRecyclerAdapter<VH> implements ItemTouchHelperAdapter {
    private final ItemTouchHelper itemTouchHelper;

    private final boolean allowDrag;
    private final boolean allowSwipe;

    public DragRecyclerAdapter() {
        this(true, true);
    }

    public DragRecyclerAdapter(final boolean allowDrag, boolean allowSwipe) {
        this.allowDrag = allowDrag;
        this.allowSwipe = allowSwipe;
        this.itemTouchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(this, allowSwipe, allowDrag) {
            @Override
            public boolean canDrag(RecyclerView.ViewHolder holder) {
                return DragRecyclerAdapter.this.canDrag(holder.getAdapterPosition());
            }

            @Override
            public boolean canSwipe(RecyclerView.ViewHolder holder) {
                return DragRecyclerAdapter.this.canSwipe(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.itemTouchHelper.attachToRecyclerView(null);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public boolean canDrag(int position) {
        return this.allowDrag;
    }

    public boolean canSwipe(int position) {
        return this.allowSwipe;
    }

    @Override
    public boolean canMove(int fromPosition, int toPosition) {
        return this.allowDrag;
    }

    @Override
    public boolean onItemMoved(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismissed(int position) {

    }
}
