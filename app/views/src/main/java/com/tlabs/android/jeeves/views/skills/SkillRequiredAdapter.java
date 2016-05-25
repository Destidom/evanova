package com.tlabs.android.jeeves.views.skills;

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

class SkillRequiredAdapter extends BaseExpandableListAdapter {

    static class ItemGroup {
        long groupId;
        String groupName;
    }

    static class GroupHolder  {
        private TextView textView;
        public GroupHolder(View itemView) {
            super();
            this.textView = (TextView)itemView;
        }

        void render(final ItemGroup group) {
            this.textView.setText(group.groupName);
        }
    }

    static class ItemHolder {

        private final ImageView itemImage;
        private final TextView itemName;
        private final TextView itemDescription;

        public ItemHolder(View view) {
            super();
            this.itemImage = (ImageView)view.findViewById(R.id.rowItemIcon);
            this.itemName = (TextView)view.findViewById(R.id.rowItemName);
            this.itemDescription = (TextView)view.findViewById(R.id.rowItemDescription);

        }

        void render(final Item item) {
            EveImages.loadItemIcon(item.getItemID(), this.itemImage);
            itemName.setText(item.getName());
            itemDescription.setText(item.getDescription());
        }
    }

    private final List<ItemGroup> groups;
    private final Map<Long, List<Item>> items;

    public SkillRequiredAdapter() {
        this.groups = new ArrayList<>();
        this.items = new HashMap<>();
    }

    public void clear() {
        this.groups.clear();
        this.items.clear();
        notifyDataSetChanged();
    }

    public void setItems(final List<Item> items) {
        this.groups.clear();
        this.items.clear();
        for (Item item: items) {
            addGroup(item);
            addItem(item);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return this.groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return this.groups.get(groupPosition).groupId;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return this.items.get(this.groups.get(groupPosition).groupId).get(childPosition).getItemID();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.items.get(this.groups.get(groupPosition).groupId).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.items.get(this.groups.get(groupPosition).groupId);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_text_group, parent, false);
            view.setTag(new GroupHolder(view));
        }

        final GroupHolder h = (GroupHolder)view.getTag();
        h.render(this.groups.get(groupPosition));
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_small, parent, false);
            view.setTag(new ItemHolder(view));
        }

        final ItemHolder h = (ItemHolder)view.getTag();
        h.render(this.items.get(this.groups.get(groupPosition).groupId).get(childPosition));
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void addGroup(final Item item) {
        for (ItemGroup g: this.groups) {
            if (g.groupId == item.getCategoryID()) {
                return;
            }
        }
        final ItemGroup g = new ItemGroup();
        g.groupId = item.getCategoryID();
        g.groupName = item.getCategoryName();
        this.groups.add(g);
    }

    private void addItem(final Item item) {
        List<Item> list = this.items.get(item.getCategoryID());
        if (null == list) {
            list = new ArrayList<>();
            this.items.put(item.getCategoryID(), list);
        }
        list.add(item);
    }
}
