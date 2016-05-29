package com.tlabs.android.jeeves.views.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.eve.api.ItemAttribute;
import com.tlabs.eve.dogma.extra.format.AttributeFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ItemAttributesAdapter extends BaseExpandableListAdapter {

    static final class GroupHolder {

        private final TextView groupNameView;

        private GroupHolder(final View view) {
            this.groupNameView = (TextView)view;
        }

        private void renderView(final String groupName) {
            this.groupNameView.setText(groupName);
        }

        public static void renderView(final View view, final String groupName) {
            final GroupHolder h = (GroupHolder)view.getTag();
            h.renderView(groupName);
        }

        public static View createView(final View parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_text_group, null);
            final GroupHolder h = new GroupHolder(view);
            view.setTag(h);
            return view;
        }
    }

    static final class AttributeHolder {

        private final TextView attributeNameView;
        private final TextView attributeTextView;
        private final ImageView attributeImageView;

        private AttributeHolder(final View view) {
            this.attributeImageView = (ImageView)view.findViewById(R.id.row_ship_attr_image);
            this.attributeNameView = (TextView)view.findViewById(R.id.row_ship_attr_name);
            this.attributeTextView = (TextView)view.findViewById(R.id.row_ship_attr_value);
        }

        private void renderView(final ItemAttribute attribute) {
            this.attributeImageView.setImageLevel(attribute.getID());

            /*if (Log.D) {
                this.attributeNameView.setText(attribute.getName() + ";attr=" + attribute.getID() + "; cat=" + attribute.getCategoryID());
            }
            else {*/
                this.attributeNameView.setText(attribute.getName());
          //  }

            this.attributeTextView.setText(AttributeFormat.format(attribute.getID(), attribute.getValue()));
        }

        public static void renderView(final View view, final ItemAttribute attribute) {
            final AttributeHolder h = (AttributeHolder)view.getTag();
            h.renderView(attribute);
        }

        public static View createView(final View parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_item_attribute, null);
            final AttributeHolder h = new AttributeHolder(view);
            view.setTag(h);
            return view;
        }
    }

    private final List<String> groups;
    private final Map<String, List<ItemAttribute>> attributes;

    public ItemAttributesAdapter() {
        this.attributes = new HashMap<>();
        this.groups = new ArrayList<>();
        this.groups.addAll(attributes.keySet());
    }

    public void setAttributes(final Map<String, List<ItemAttribute>> attributes) {
        this.attributes.clear();
        this.attributes.putAll(attributes);
        this.groups.clear();
        this.groups.addAll(attributes.keySet());
    }

    @Override
    public final Object getChild(int groupPosition, int childPosition) {
        return attributes.get(groups.get(groupPosition)).get(childPosition);
    }

    @Override
    public final long getChildId(int groupPosition, int childPosition) {
        return attributes.get(groups.get(groupPosition)).get(childPosition).getID();
    }

    @Override
    public final View getChildView(int groupPosition,
                             int childPosition,
                             boolean isLastChild,
                             View convertView,
                             ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = AttributeHolder.createView(parent);
        }
        
        final ItemAttribute attribute = (ItemAttribute)getChild(groupPosition, childPosition);
        AttributeHolder.renderView(view, attribute);
        return view;
    }

    @Override
    public final int getChildrenCount(int groupPosition) {
        return attributes.get(groups.get(groupPosition)).size();
    }

    @Override
    public final Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public final int getGroupCount() {
        return groups.size();
    }

    @Override
    public final long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = GroupHolder.createView(parent);          
        }
            
        GroupHolder.renderView(view, this.groups.get(groupPosition));
        return view;        
    }

    @Override
    public final boolean hasStableIds() {
        return true;
    }

    @Override
    public final boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
