package com.tlabs.android.jeeves.views.ui.list;

import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.tlabs.android.jeeves.views.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class SelectableRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private final List<Long> selected;
    private final Map<Long, Boolean> updating;

    private int backgroundResourceSelected = R.drawable.jeeves_panel_no_border_light;
    private int backgroundResourceNotSelected = R.drawable.jeeves_panel_no_border;

    public SelectableRecyclerAdapter() {
        super();
        this.selected = new ArrayList<>();
        this.updating = new ArrayMap<>();
        setHasStableIds(true);
    }

    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(VH holder, int position, boolean selected, boolean updating);

    @Override
    public abstract long getItemId(int position);

    @Override
    public final void onBindViewHolder(VH h, int position) {
        final View itemView = h instanceof DragRecyclerHolder ? ((DragRecyclerHolder)h).getItemView() : h.itemView;
        final Long itemId = getItemId(position);
        final boolean selection = this.selected.contains(itemId);
        final boolean updating = this.updating.containsKey(itemId) ? this.updating.get(itemId) : false;
        itemView.setSelected(selection);
        itemView.setOnClickListener(v -> {
            if (selected.isEmpty()) {
                if (onItemClicked(itemId)) {
                    selected.add(itemId);
                    itemView.setBackgroundResource(backgroundResourceSelected);
                }
                return;
            }

            if (selected.contains(itemId)) {
                selected.remove(itemId);
                if (onItemSelected(itemId, false)) {
                    itemView.setBackgroundResource(backgroundResourceNotSelected);
                } else {
                    selected.add(itemId);
                }
            }
            else {
                selected.add(itemId);
                if (onItemSelected(itemId, true)) {
                    itemView.setBackgroundResource(backgroundResourceSelected);
                } else {
                    selected.remove(itemId);
                }
            }
        });
        itemView.setOnLongClickListener(v -> {
            if (selected.contains(itemId)) {
                selected.remove(itemId);
                if (onItemSelected(h.getItemId(), false)) {
                    itemView.setBackgroundResource(backgroundResourceNotSelected);
                } else {
                    selected.add(itemId);
                }
            } else {
                selected.add(itemId);
                if (onItemSelected(h.getItemId(), true)) {
                    itemView.setBackgroundResource(backgroundResourceSelected);
                } else {
                    selected.remove(itemId);
                }
            }
            return true;
        });
        if (selected.contains(itemId)) {
            itemView.setBackgroundResource(backgroundResourceSelected);
        }
        else {
            itemView.setBackgroundResource(backgroundResourceNotSelected);
        }
        onBindViewHolder(h, position, selection, updating);
    }

    public final int getItemPosition(final long itemId) {
        for (int i = 0; i < getItemCount(); i++) {
            if (itemId == getItemId(i)) {
                return i;
            }
        }
        return -1;
    }

    public final List<Long> getSelected() {
        return this.selected;
    }

    public final boolean getSelected(int position) {
        final long id = getItemId(position);
        return  this.selected.contains(id);
    }

    public final void setSelected(final boolean all) {
        this.selected.clear();
        if (all) {
            for (int i = 0; i < getItemCount(); i++) {
                this.selected.add(getItemId(i));
            }
        }
        notifyItemsChanged();
    }

    public final void setSelected(final long id, final boolean selected) {
        if (selected && !this.selected.contains(id)) {
            this.selected.add(id);
            notifyItemsChanged();
        }
        else if (!selected && this.selected.contains(id)) {
            this.selected.remove(id);
            notifyItemsChanged();
        }
    }

    public final void setSelected(final List<Long> selected) {
        this.selected.clear();
        this.selected.addAll(selected);
        notifyItemsChanged();
    }

    public final void setUpdating(final long itemId, final boolean updating) {
        this.updating.remove(itemId);
        if (updating) {
            this.updating.put(itemId, true);
        }
        notifyItemChanged(getItemPosition(itemId));
    }

    public final void clearUpdating() {
        this.updating.clear();
    }

    public final void clearSelected() {
        this.selected.clear();
    }

    public final void notifyItemsChanged() {
        for (int i = 0; i < this.getItemCount(); i++) {
            notifyItemChanged(i);
        }
    }

    public final void notifyDataSetChanged(final boolean clear) {
        if (clear) {
            this.clearSelected();
            this.clearUpdating();
        }
        notifyDataSetChanged();
    }

    protected boolean onItemClicked(final long id) {return false;}

    protected boolean onItemSelected(final long id, final boolean selected){return false;}

}
