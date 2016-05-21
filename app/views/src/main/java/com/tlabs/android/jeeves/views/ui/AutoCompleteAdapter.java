package com.tlabs.android.jeeves.views.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class AutoCompleteAdapter<T> extends BaseAdapter implements Filterable {

    public interface AutoComplete<T> {
        List<T> autoComplete(final String constraint);
    }

    private List<T> results;
    private final AutoComplete<T> autoComplete;

    public AutoCompleteAdapter(final AutoComplete<T> autoComplete) {
        super();
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView)convertView;
        if (null == textView) {
            textView = new TextView(parent.getContext());
        }
        textView.setText(this.results.get(position).toString());
        return textView;
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
