package com.tlabs.android.jeeves.views.ui.list;

import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;

import com.tlabs.android.jeeves.views.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class SelectableRecyclerAdapterSupport {

    private final List<Object> selected;
    private final Map<Object, Boolean> updating;

    private int backgroundResourceSelected = R.drawable.jeeves_panel_no_border_light;
    private int backgroundResourceNotSelected = R.drawable.jeeves_panel_no_border;

    public SelectableRecyclerAdapterSupport() {
        this.selected = new ArrayList<>();
        this.updating = new ArrayMap<>();
    }

    public boolean getSelected(final Object child) {
        return this.selected.contains(child);
    }

    public boolean getUpdating(final Object child) {
        final Boolean b = this.updating.get(child);
        return null == b ? false : b;
    }

    public final <T extends RecyclerView.ViewHolder> void onBindHolder(T h, Object child) {
        final boolean selection = this.selected.contains(child);
        final boolean updating = this.updating.containsKey(child) ? this.updating.get(child) : false;
        h.itemView.setSelected(selection);
        h.itemView.setOnClickListener(v -> {
            if (selected.isEmpty()) {
                if (onItemClicked(child)) {
                    selected.add(child);
                    h.itemView.setBackgroundResource(backgroundResourceSelected);
                }
                return;
            }

            if (selected.contains(child)) {
                selected.remove(child);
                if (onItemSelected(child, false)) {
                    h.itemView.setBackgroundResource(backgroundResourceNotSelected);
                } else {
                    selected.add(child);
                }
            } else {
                selected.add(child);
                if (onItemSelected(child, true)) {
                    h.itemView.setBackgroundResource(backgroundResourceSelected);
                } else {
                    selected.remove(child);
                }
            }
        });
        h.itemView.setOnLongClickListener(v -> {
            if (selected.contains(child)) {
                selected.remove(child);
                if (onItemSelected(child, false)) {
                    h.itemView.setBackgroundResource(backgroundResourceNotSelected);
                } else {
                    selected.add(child);
                }
            } else {
                selected.add(child);
                if (onItemSelected(child, true)) {
                    h.itemView.setBackgroundResource(backgroundResourceSelected);
                } else {
                    selected.remove(child);
                }
            }
            return true;
        });
        if (selected.contains(child)) {
            h.itemView.setBackgroundResource(backgroundResourceSelected);
        }
        else {
            h.itemView.setBackgroundResource(backgroundResourceNotSelected);
        }
    }

    public List<Object> getSelected() {
        return this.selected;
    }

    public void clearSelected() {
        this.selected.clear();
    }

    public void addSelected(final List<Object> objects) {
        this.selected.addAll(objects);
    }

    public void addSelected(final Object object) {
        this.selected.add(object);
    }

    public void addUpdating(final Object object) {
        this.updating.put(object, true);
    }

    public void addUpdating(final List<Object> objects) {
        for (Object o: objects) {
            this.updating.put(o, true);
        }
    }

    public void clearUpdating() {
        this.updating.clear();
    }

    public void removeUpdating(final Object object) {
        this.updating.remove(object);
    }

    public final void setBackgroundResourceSelected(int backgroundResourceSelected) {
        this.backgroundResourceSelected = backgroundResourceSelected;
    }

    public final void setBackgroundResourceNotSelected(int backgroundResourceNotSelected) {
        this.backgroundResourceNotSelected = backgroundResourceNotSelected;
    }

    protected boolean onItemClicked(final Object item) {return false;}

    protected boolean onItemSelected(final Object item, final boolean selected){return false;}

}