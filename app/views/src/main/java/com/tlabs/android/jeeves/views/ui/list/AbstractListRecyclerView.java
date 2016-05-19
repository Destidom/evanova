package com.tlabs.android.jeeves.views.ui.list;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public abstract class AbstractListRecyclerView<T> extends FrameLayout {

    public interface Draggable {
        boolean canMove(int from, int to);
    }

    public interface Removable {
        boolean canRemove(int position);
    }

    public interface Timed {
        int getTimerInterval();
    }

    public interface Listener<T> {

        void onItemClicked(final T t);

        void onItemSelected(final T t, final boolean selected);

        void onItemMoved(final T t, final int from, final int to);

    }

    private ListRecyclerViewAdapter<T> adapter;
    private AbstractListRecyclerView.Listener<T> listener;

    private Subscription timer = null;

    public AbstractListRecyclerView(Context context) {
        super(context);
        init();
    }

    public AbstractListRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AbstractListRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected abstract ListRecyclerViewAdapter.ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType);

    protected abstract long getItemId(final T item);

    public void setLoading(final boolean loading) {
        //FIXME implement
    }

    public void setLoading(final long itemId, final boolean loading) {
        this.adapter.setUpdating(itemId, loading);
    }

    public void setItems(final List<T> items) {
        this.adapter.setItems(items);
    }

    public void setListener(final AbstractListRecyclerView.Listener<T> listener) {
        this.listener = listener;
    }

    public void mergeItem(final T item) {
        final int position = this.adapter.getItemPosition(getItemId(item));
        if (position == -1) {
            this.adapter.addItem(item);
        }
        else {
            this.adapter.setItem(item, position);
        }
    }

    public List<T> getItems() {
        return this.adapter.getItems();
    }

    public void removeItem(final long id) {
        this.adapter.removeItem(this.adapter.getItemPosition(id));
    }

    public List<Long> getSelectedItems() {
        return this.adapter.getSelected();
    }

    protected void notifyDataSetChanged() {
        this.adapter.notifyDataSetChanged(true);
    }

    protected ListRecyclerViewAdapter<T> getAdapter() {
        return this.adapter;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != this.timer) {
            this.timer.unsubscribe();
            this.timer = null;
        }

        this.listener = null;
        this.adapter.clear();
    }

    private void init() {
        final RecyclerView listView = new RecyclerView(getContext());
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        addView(listView);

        this.adapter = new ListRecyclerViewAdapter<T>(this instanceof Draggable, this instanceof Removable) {

            @Override
            public boolean canMove(int fromPosition, int toPosition) {
                return super.canMove(fromPosition, toPosition) &&
                        ((Draggable)AbstractListRecyclerView.this).canMove(fromPosition, toPosition);
            }

            @Override
            public boolean canSwipe(int position) {
                return super.canSwipe(position) && ((Removable)AbstractListRecyclerView.this).canRemove(position);
            }

            @Override
            public ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
                return AbstractListRecyclerView.this.onCreateViewHolder(parent, viewType);
            }

            @Override
            public long getItemId(int position) {
                return AbstractListRecyclerView.this.getItemId(getItemAt(position));
            }
            @Override
            protected boolean onItemClicked(long id) {
                if (null != listener) {
                    listener.onItemClicked(getItem(id));
                }
                return false;
            }

            @Override
            public boolean onItemMoved(int fromPosition, int toPosition) {
                if (null != listener) {
                    listener.onItemMoved(getItemAt(toPosition), fromPosition, toPosition);
                }
                return true;
            }
            @Override
            protected boolean onItemSelected(long id, boolean selected) {
                if (null != listener) {
                    listener.onItemSelected(getItem(id), selected);
                }
                return true;
            }
        };
        listView.setAdapter(this.adapter);
        int timed = this instanceof Timed ? ((Timed)this).getTimerInterval() : 0;
        if (timed > 0) {
            this.timer =
                    Observable.
                    interval(timed, TimeUnit.SECONDS).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(t -> adapter.notifyItemsChanged());
        }
    }
}
