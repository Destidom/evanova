package com.tlabs.android.jeeves.views.account;

import android.content.Context;
import android.graphics.Color;

import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.eve.api.EveAPI;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


final class AccessListAdapter extends BaseExpandableListAdapter {
    static class AccessGroup {

        private String groupName;
        private String groupDescription;

        private int enabledCount;
        private int totalCount;

        private List<AccessChild> children;

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getGroupDescription() {
            return groupDescription;
        }

        public void setGroupDescription(String groupDescription) {
            this.groupDescription = groupDescription;
        }

        public int getEnabledCount() {
            return enabledCount;
        }

        public void setEnabledCount(int enabledCount) {
            this.enabledCount = enabledCount;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<AccessChild> getChildren() {
            return children;
        }

        public void setChildren(List<AccessChild> children) {
            this.children = children;
        }
    }

    static class AccessChild {
        private int enabled;
        private String name;
        private String description;

        public int getEnabled() {
            return enabled;
        }

        public void setEnabled(int enabled) {
            this.enabled = enabled;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    static final class AccessChildHolder {
        private TextView accessName;
        private TextView accessDescription;

        public AccessChildHolder(final View v) {
            super();
            this.accessName = (TextView)v.findViewById(R.id.j_rowAccessName);
            this.accessDescription = (TextView)v.findViewById(R.id.j_rowAccessDescription);
        }

        private void render(final AccessChild c) {
            boolean enabled = c.enabled == 1;
            this.accessName.setText(c.name);
            this.accessName.setTextColor(enabled ? Color.WHITE : Color.GRAY);
            this.accessDescription.setText(c.description);
        }

        public static View create(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.jeeves_row_access_child, null);
            view.setTag(new AccessChildHolder(view));
            return view;
        }

        public static void render(final View view, final AccessChild c) {
            AccessChildHolder h = (AccessChildHolder)view.getTag();
            h.render(c);
        }
    }

    static final class AccessGroupHolder {
        private TextView accessGroupName;
        private TextView accessGroupCount;
        private TextView accessGroupDescription;

        public AccessGroupHolder(View v) {
            super();
            this.accessGroupName = (TextView)v.findViewById(R.id.j_rowAccessGroupName);
            this.accessGroupCount = (TextView)v.findViewById(R.id.j_rowAccessGroupCount);
            this.accessGroupDescription = (TextView)v.findViewById(R.id.j_rowAccessGroupDescription);
        }

        private void render(AccessGroup c) {
            this.accessGroupName.setText(c.groupName);
            this.accessGroupDescription.setText(c.groupDescription);

            if (c.enabledCount == 0) {
                this.accessGroupName.setTextColor(Color.GRAY);
            }
            else if (c.enabledCount == c.totalCount) {
                this.accessGroupName.setTextColor(Color.GREEN);
            }
            else {
                this.accessGroupName.setTextColor(Color.YELLOW);
            }
            this.accessGroupCount.setText(c.enabledCount + " / " + c.totalCount);
        }

        public static View create(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.jeeves_row_access_group, null);
            view.setTag(new AccessGroupHolder(view));
            return view;
        }

        public static void render(final View view, final AccessGroup c) {
            AccessGroupHolder h = (AccessGroupHolder)view.getTag();
            h.render(c);
        }
    }

    private final List<AccessGroup> groups;

    public AccessListAdapter() {
        this.groups = new ArrayList<>();
    }

    public void setAccount(final EveAccount account) {
        this.groups.clear();
        if (null != account) {
            this.groups.addAll(build(account.getType(), account.getAccessMask()));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return this.groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.groups.get(groupPosition).totalCount;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = AccessGroupHolder.create(parent.getContext());
        }
        AccessGroupHolder.render(view, this.groups.get(groupPosition));
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = AccessChildHolder.create(parent.getContext());
        }
        AccessChildHolder.render(view, this.groups.get(groupPosition).children.get(childPosition));
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    private static List<AccessGroup> build(final int type, final long mask) {
        final Map<Integer, List<AccessChild>> children = new HashMap<>();

        final Map<Integer, Integer> matches = new ArrayMap<>();
        EveAPI.AccessGroup[] groups =
                type == 1 ? EveAPI.corporationGroups : EveAPI.characterGroups;

        for (EveAPI.AccessGroup g: groups) {
            children.put(g.getID(), new ArrayList<>());
            matches.put(g.getID(), 0);
        }
        //new String[] {"_id", "name", "description", "enabled"};
        if (type == 1) {
            for (EveAPI.CorporationAccess a: EnumSet.allOf(EveAPI.CorporationAccess.class)) {
                List<AccessChild> kids = children.get(a.getGroupID());
                if (null == kids) {
                    //Log.w("Evanova", "APIAccessFragment: unknown groupID " + a.getGroupID());
                }
                else {
                    boolean match = mask != -1 && (mask & a.getAccessMask()) == a.getAccessMask();
                    if (match) {
                        int count = matches.get(a.getGroupID());
                        matches.put(a.getGroupID(), count + 1);
                    }
                    AccessListAdapter.AccessChild kid = new AccessListAdapter.AccessChild();
                    kid.setDescription(a.getDescription());
                    kid.setEnabled(match ? 1 : 0);
                    kid.setName(a.getName());
                    kids.add(kid);
                }
            }
        }
        else {
            //have to repeat the same code...
            for (EveAPI.CharacterAccess a: EnumSet.allOf(EveAPI.CharacterAccess.class)) {
                List<AccessChild> kids = children.get(a.getGroupID());
                if (null == kids) {
                   // Log.w("Evanova", "APIAccessFragment: unknown groupID " + a.getGroupID());
                }
                else {
                    boolean match = mask != -1 && (mask & a.getAccessMask()) == a.getAccessMask();
                    if (match) {
                        int count = matches.get(a.getGroupID());
                        matches.put(a.getGroupID(), count + 1);
                    }
                    AccessListAdapter.AccessChild kid = new AccessListAdapter.AccessChild();
                    kid.setDescription(a.getDescription());
                    kid.setEnabled(match ? 1 : 0);
                    kid.setName(a.getName());
                    kids.add(kid);
                }
            }
        }//if children type == ?
        final List<AccessGroup> returned = new ArrayList<>(groups.length);
        for (EveAPI.AccessGroup g: groups) {
            List<AccessChild> kids = children.get(g.getID());
            final AccessListAdapter.AccessGroup group = new AccessListAdapter.AccessGroup();
            group.setGroupName(g.getName());
            group.setGroupDescription(g.getDescription());
            group.setTotalCount(kids.size());
            group.setEnabledCount(matches.get(g.getID()));
            group.setChildren(kids);
            returned.add(group);

        }
        matches.clear();
        return returned;
    }
}
