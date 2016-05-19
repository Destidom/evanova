package com.tlabs.android.jeeves.views.ui.list;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.Collections;
import java.util.List;

public abstract class SelectableExpandableRecyclerAdapter<PH extends ParentViewHolder, CH extends ChildViewHolder> extends ExpandableRecyclerAdapter<PH, CH> {

    private final SelectableRecyclerAdapterSupport support;
    private final List<? extends ParentListItem> parents;

    public SelectableExpandableRecyclerAdapter(List<? extends ParentListItem> list) {
        super(list);
        this.parents = Collections.unmodifiableList(list);
        this.support = new SelectableRecyclerAdapterSupport() {
            @Override
            protected boolean onItemClicked(Object item) {
                return SelectableExpandableRecyclerAdapter.this.onItemClicked(item);
            }

            @Override
            protected boolean onItemSelected(Object item, boolean selected) {
                return SelectableExpandableRecyclerAdapter.this.onItemSelected(item, selected);
            }
        };
        setHasStableIds(false);
    }

    public abstract void onBindChildViewHolder(CH holder, int position, Object child, boolean selected, boolean updating);

    @Override
    public final void onBindChildViewHolder(CH h, int position, Object child) {
        final boolean selection = this.support.getSelected(child);
        final boolean updating = this.support.getUpdating(child);
        support.onBindHolder(h, child);
        onBindChildViewHolder(h, position, child, selection, updating);
    }

    protected final List<Object> getSelected() {
        return this.support.getSelected();
    }

    public final void clearSelected() {
        this.support.clearSelected();
        notifyItemsChanged();
    }

    protected boolean onItemClicked(final Object item) {return false;}

    protected boolean onItemSelected(final Object item, final boolean selected){return false;}

    protected final void notifyItemsChanged() {
        for (int i = 0; i < this.getItemCount(); i++) {
            notifyItemChanged(i);
        }
    }

    protected final List<? extends ParentListItem> getParents() {
        return this.parents;
    }
}