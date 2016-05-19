package com.tlabs.android.jeeves.views.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class ListRecyclerViewAdapter<T> extends DragRecyclerAdapter<ListRecyclerViewAdapter.ViewHolder<T>> {

    public static class ViewHolder<T> extends RecyclerView.ViewHolder {
        protected boolean selected;
        protected boolean updating;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void render(final T t) {}
    }

    private final List<T> items;

    public ListRecyclerViewAdapter(boolean allowDrag, boolean allowSwipe) {
        super(allowDrag, allowSwipe);
        this.items = new ArrayList<>();
    }

    public ListRecyclerViewAdapter() {
        this(false, false);
    }

    public void setItems(final List<T> items) {
        this.items.clear();
        this.items.addAll(items);

        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder<T> holder, int position, boolean selected, boolean updating) {
        holder.selected = selected;
        holder.updating = updating;
        holder.render(this.items.get(position));
    }

    @Override
    public final int getItemCount() {
        return this.items.size();
    }

    public final T getItem(final long itemId) {
        return getItemAt(getItemPosition(itemId));
    }

    public final T getItemAt(final int position) {
        if (position < 0 || position >= this.items.size()) {
            return null;
        }
        return this.items.get(position);
    }

    public final List<T> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    public void addItem(final T item) {
        this.items.add(item);
        notifyItemInserted(this.items.size() - 1);
    }

    public void setItem(final T item, final int position) {
        this.items.remove(position);
        this.items.add(position, item);
        notifyItemChanged(position);
    }

    public void removeItem(final int position) {
        this.items.remove(position);
        notifyItemRemoved(position);
    }

    public void swapItems(final int from, final int to) {
        Collections.swap(this.items, from, to);
        notifyItemMoved(from, to);
        notifyItemChanged(to);
    }

    public final void clear() {
        this.items.clear();
        notifyDataSetChanged(true);
    }
}
