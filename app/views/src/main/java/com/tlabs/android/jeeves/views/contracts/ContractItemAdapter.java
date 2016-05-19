package com.tlabs.android.jeeves.views.contracts;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.R;
import com.tlabs.eve.api.ContractItem;

import java.util.ArrayList;
import java.util.List;

class ContractItemAdapter extends BaseExpandableListAdapter {

    private static final int GROUP_RECEIVED = 0;
    private static final int GROUP_SENT = 1;

    private static final class GroupHolder {
        private static final int LAYOUT = R.layout.jeeves_row_contract_items;

        private final TextView groupNameText;

        public GroupHolder(final View view) {
            this.groupNameText = (TextView)view.findViewById(R.id.j_contract_ItemGroupText);
        }

        public void render(final int group, final int itemCount, boolean isOwner) {
            if(isOwner) {
                switch (group) {
                    case GROUP_RECEIVED:
                        if (itemCount == 0) {
                            this.groupNameText.setTextColor(Color.GRAY);
                            this.groupNameText.setText(R.string.jeeves_contract_item_buyer_receive_none);
                        }
                        else if (itemCount == 1) {
                            this.groupNameText.setTextColor(Color.RED);
                            this.groupNameText.setText(R.string.jeeves_contract_item_buyer_receive_one);
                        }
                        else {
                            this.groupNameText.setTextColor(Color.RED);
                            this.groupNameText.setText(String.format(
                                    this.groupNameText.getResources().getString(R.string.jeeves_contract_item_buyer_receive),
                                    itemCount));
                        }
                        break;
                    case GROUP_SENT:
                        if (itemCount == 0) {
                            this.groupNameText.setTextColor(Color.GRAY);
                            this.groupNameText.setText(R.string.jeeves_contract_item_buyer_send_none);
                        }
                        else if (itemCount == 1) {
                            this.groupNameText.setTextColor(Color.GREEN);
                            this.groupNameText.setText(R.string.jeeves_contract_item_buyer_send_one);
                        }
                        else {
                            this.groupNameText.setTextColor(Color.GREEN);
                            this.groupNameText.setText(String.format(
                                    this.groupNameText.getResources().getString(R.string.jeeves_contract_item_buyer_send),
                                    itemCount));
                        }
                        break;
                    default:
                        this.groupNameText.setTextColor(Color.WHITE);
                        this.groupNameText.setText(R.string.jeeves_unknown);
                        break;
                }

            }
            else {
                switch (group) {
                    case GROUP_RECEIVED:
                        if (itemCount == 0) {
                            this.groupNameText.setTextColor(Color.GRAY);
                            this.groupNameText.setText(R.string.jeeves_contract_item_you_receive_none);
                        }
                        else if (itemCount == 1) {
                            this.groupNameText.setTextColor(Color.GREEN);
                            this.groupNameText.setText(R.string.jeeves_contract_item_you_receive_one);
                        }
                        else {
                            this.groupNameText.setTextColor(Color.GREEN);
                            this.groupNameText.setText(String.format(
                                    this.groupNameText.getResources().getString(R.string.jeeves_contract_item_you_receive),
                                    itemCount));
                        }
                        break;
                    case GROUP_SENT:
                        if (itemCount == 0) {
                            this.groupNameText.setTextColor(Color.GRAY);
                            this.groupNameText.setText(R.string.jeeves_contract_item_you_send_none);
                        }
                        else if (itemCount == 1) {
                            this.groupNameText.setTextColor(Color.RED);
                            this.groupNameText.setText(R.string.jeeves_contract_item_you_send_one);
                        }
                        else {
                            this.groupNameText.setTextColor(Color.RED);
                            this.groupNameText.setText(String.format(
                                    this.groupNameText.getResources().getString(R.string.jeeves_contract_item_you_send),
                                    itemCount));
                        }
                        break;
                    default:
                        this.groupNameText.setTextColor(Color.WHITE);
                        this.groupNameText.setText(R.string.jeeves_unknown);
                        break;
                }
            }
        }

        public static View createView(final Context context) {
            final View view = LayoutInflater.from(context).inflate(LAYOUT, null);
            view.setTag(new GroupHolder(view));
            return view;
        }

        public static void renderView(final View view, final int group, final int itemCount, boolean isOwner) {
            final GroupHolder h = (GroupHolder)view.getTag();
            h.render(group, itemCount, isOwner);
        }
    }

    private static final class ItemHolder {
        private static final int LAYOUT = R.layout.jeeves_row_contract_item;

        private final TextView itemNameView;
        private final TextView itemNameQuantityView;

        public ItemHolder(final View view) {
            this.itemNameView = (TextView)view.findViewById(R.id.j_contract_ListItemName);
            this.itemNameQuantityView = (TextView)view.findViewById(R.id.j_contract_ListItemQuantity);
        }

        public void render(final ContractItem item) {
            this.itemNameView.setText(item.getTypeName());
            itemNameQuantityView.setText(item.getQuantity() + "");
        }

        public static View createView(final Context context) {
            final View view = LayoutInflater.from(context).inflate(LAYOUT, null);
            view.setTag(new ItemHolder(view));
            return view;
        }

        public static void renderView(final View view, final ContractItem item) {
            final ItemHolder h = (ItemHolder)view.getTag();
            h.render(item);
        }
    }

    private final List<List<ContractItem>> groupedItems;
    private boolean isOwner;

    public ContractItemAdapter() {
        this.groupedItems = new ArrayList<>();
        this.isOwner = false;
    }

    public void setItems(final List<ContractItem> items, final boolean isOwner) {
        this.isOwner = isOwner;
        this.groupedItems.clear();
        final List<ContractItem> receivedItems = new ArrayList<>();
        final List<ContractItem> sentItems = new ArrayList<>();

        for (ContractItem item: items) {
            if (item.getIncluded()) {
                receivedItems.add(item);
            }
            else {
                sentItems.add(item);
            }
        }
        this.groupedItems.add(receivedItems);
        if (!sentItems.isEmpty()) {
            this.groupedItems.add(sentItems);
        }
        notifyDataSetChanged();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.groupedItems.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return this.groupedItems.get(groupPosition).get(childPosition).getRecordID();
    }

    @Override
    public View getChildView(int groupPosition,
                             int childPosition,
                             boolean isLastChild,
                             View convertView,
                             ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = ItemHolder.createView(parent.getContext());
        }
        ItemHolder.renderView(view, (ContractItem)getChild(groupPosition, childPosition));
        return view;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = GroupHolder.createView(parent.getContext());
        }
        GroupHolder.renderView(view, groupPosition, getChildrenCount(groupPosition), isOwner);
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.groupedItems.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.groupedItems.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.groupedItems.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
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
