package com.tlabs.android.jeeves.views.skills;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.eve.api.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CertificateRequirementAdapter extends BaseExpandableListAdapter {

    private static final class GroupHolder {
        private static final int LAYOUT = R.layout.row_text_group;

        private final TextView textView;

        private GroupHolder(final View view) {
            this.textView = (TextView)view;
        }

        private void render(final String label) {
            this.textView.setText(label);
        }

        public static View create(final Context context) {
            View view = LayoutInflater.from(context).inflate(LAYOUT, null);
            view.setTag(new GroupHolder(view));
            return view;
        }

        public static void render(final View view, final String label) {
            final GroupHolder h = (GroupHolder)view.getTag();
            h.render(label);
        }
    }

    private static final class ItemHolder {
        private static final int LAYOUT = R.layout.row_item;

        private final TextView itemText;
        private final ImageView itemIcon;

        private ItemHolder(final View view) {
            this.itemText = (TextView)view.findViewById(R.id.itemCategoryName);
            this.itemIcon = (ImageView)view.findViewById(R.id.itemCategoryIcon);
        }

        private void render(final Item item) {
            this.itemText.setText(item.getName());
            EveImages.loadItemIcon(item.getItemID(), this.itemIcon);
        }

        public static View create(final Context context) {
            View view = LayoutInflater.from(context).inflate(LAYOUT, null);
            view.setTag(new ItemHolder(view));
            return view;
        }

        public static void render(final View view, final Item item) {
            final ItemHolder h = (ItemHolder)view.getTag();
            h.render(item);
        }
    }

    private final Map<Long, List<Item>> itemGroups;
    private final List<Long> groups;

    private Map<Long, String> groupLabels;

    public CertificateRequirementAdapter() {
        super();
        this.itemGroups = new HashMap<>();
        this.groups = new ArrayList<>();
    }

    public void setItems(final Map<Long, String> categories, final Map<Long, Item> items) {
        this.itemGroups.clear();
        this.groups.clear();
        this.groupLabels = categories;//TODO berk
        for (Item item: items.values()) {
            final Long groupID = item.getGroupID();
            List<Item> group = this.itemGroups.get(groupID);
            if (null == group) {
                group = new ArrayList<>();
                this.groups.add(groupID);
                this.itemGroups.put(groupID, group);
            }
            group.add(item);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getChildView(int groupPosition,
                             int childPosition,
                             boolean isLastChild,
                             View convertView,
                             ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = ItemHolder.create(parent.getContext());
        }
        ItemHolder.render(view, (Item)getChild(groupPosition, childPosition));
        return view;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = GroupHolder.create(parent.getContext());
        }
        final Long groupId = getGroupId(groupPosition);
        String label = (null == this.groupLabels) ? null : this.groupLabels.get(groupId);
        if (null == label) {
            label = "" + groupId;
        }
        label = label.trim() + " (" + getChildrenCount(groupPosition) + ")";
        GroupHolder.render(view, label);
        return view;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        final long groupId = getGroupId(groupPosition);
        return this.itemGroups.get(groupId).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        final Item item = (Item)getChild(groupPosition, childPosition);
        return item.getItemID();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        final long groupId = getGroupId(groupPosition);
        return this.itemGroups.get(groupId).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.itemGroups.get(getGroupId(groupPosition));
    }

    @Override
    public int getGroupCount() {
        return this.itemGroups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return this.groups.get(groupPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
