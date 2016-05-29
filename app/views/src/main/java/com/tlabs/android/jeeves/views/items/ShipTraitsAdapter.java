package com.tlabs.android.jeeves.views.items;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.tlabs.eve.api.Item;
import com.tlabs.eve.api.ItemTrait;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class ShipTraitsAdapter extends BaseExpandableListAdapter {

    private static class TraitHolder {
        private final TextView textView;

        private TraitHolder(final View view) {
            this.textView = (TextView)view;
        }

        public void render(final ItemTrait trait) {
            final String text = (StringUtils.isBlank(trait.getText())) ? "" : android.text.Html.fromHtml(trait.getText().trim()).toString();
            if (trait.getBonus() == 0) {
                this.textView.setText(text);
                return;
            }
            if (StringUtils.isBlank(text)) {
                this.textView.setText("");
            }
            else {
                final String formatted = String.format(
                        this.textView.getResources().getString(R.string.jeeves_item_traits),
                        trait.getBonus(),
                        trait.getUnitName(),
                        android.text.Html.fromHtml(text).toString());
            this.textView.setText(formatted);
            }

        }

        public static void render(final View view, final ItemTrait trait) {
            final TraitHolder h = (TraitHolder)view.getTag();
            h.render(trait);
        }

        public static View create(final Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_text_group, null);
            view.setTag(new TraitHolder(view));
            return view;
        }
    }

    private static class TraitGroupHolder {

        private final TextView titleView;

        private TraitGroupHolder(final View view) {
            this.titleView = (TextView)view;
        }

        public void render(String title) {
            this.titleView.setText(title);
        }

        public static void render(final View view, final String title) {
            final TraitGroupHolder h = (TraitGroupHolder)view.getTag();
            h.render(title);
        }

        public static View create(final Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_text_group, null);
            view.setTag(new TraitGroupHolder(view));
            return view;
        }
    }
    private final List<List<ItemTrait>> children = new ArrayList<>();


    public void setItem(final Item item) {
        this.children.clear();
        final Map<Integer, List<ItemTrait>> groups = new ArrayMap<>();

        for (ItemTrait t : item.getTraits()) {
            List<ItemTrait> group = groups.get(t.getGroupID());
            if (null == group) {
                group = new ArrayList<>();
                groups.put(t.getGroupID(), group);
            }
            group.add(t);
        }

        this.children.addAll(groups.values());
        groups.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return children.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return children.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return children.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return children.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getChild(groupPosition, childPosition).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = TraitGroupHolder.create(parent.getContext());
        }

        final ItemTrait trait = children.get(groupPosition).get(0);
        TraitGroupHolder.render(view, trait.getGroupName());
        ExpandableListView listView = (ExpandableListView)parent;
        listView.expandGroup(groupPosition);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = TraitHolder.create(parent.getContext());
        }

        TraitHolder.render(view, children.get(groupPosition).get(childPosition));
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
