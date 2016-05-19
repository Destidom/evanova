package com.tlabs.android.jeeves.views.ui;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.Collections;
import java.util.List;

public class AutoCompleteAdapter<T> extends ArrayAdapter<T> implements Filterable {

    public interface AutoComplete<T> {
        List<T> autoComplete(final String constraint);
    }

    private List<T> results;
    private final AutoComplete<T> autoComplete;

    public AutoCompleteAdapter(
            final Context context,
            final AutoComplete<T> autoComplete,
            final int layoutResource) {
        super(context, layoutResource);
        this.results = Collections.emptyList();
        this.autoComplete = autoComplete;
    }

    @Override
    public final int getCount() {
        return results.size();
    }

    @Override
    public final T getItem(int index) {
        return results.get(index);
    }
    
    @Override
    public final Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    results = autoComplete.autoComplete(constraint.toString());
                    filterResults.values = results;
                    filterResults.count = results.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }};
    }
}
