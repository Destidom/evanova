package com.tlabs.android.jeeves.views.character;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.eve.api.character.CharacterSheet.Implant;
import com.tlabs.eve.api.character.CharacterSheet.JumpClone;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

class CharacterClonesAdapter extends BaseExpandableListAdapter {

    private static final class RowItemHolder {

        private final ImageView imageView;
        private final TextView titleView;
        private final TextView descriptionView;

        private RowItemHolder(final View view) {
            this.imageView = (ImageView) view.findViewById(R.id.rowItemIcon);
            this.titleView = (TextView) view.findViewById(R.id.rowItemName);
            this.descriptionView = (TextView) view.findViewById(R.id.rowItemDescription);
        }

        private void renderView(final JumpClone clone) {
            //I18N
            final String name = (StringUtils.isBlank(clone.getName())) ? "Unnamed Clone" : clone.getName();
            this.titleView.setText(name + " (" + clone.getImplants().size() + " implants)");

            this.descriptionView.setVisibility(View.VISIBLE);
            this.descriptionView.setText(clone.getLocation());
        }

        private void renderView(final Implant implant) {
            EveImages.loadItemIcon(implant.getTypeID(), this.imageView);
            this.titleView.setText(implant.getTypeName());
            this.descriptionView.setText(implant.getDescription());
        }

        public static void renderView(final View view, final JumpClone clone) {
            final RowItemHolder h = (RowItemHolder) view.getTag();
            h.renderView(clone);
        }

        public static void renderView(final View view, final Implant implant) {
            final RowItemHolder h = (RowItemHolder) view.getTag();
            h.renderView(implant);
        }

        public static View createView(final View parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_small, null);
            final RowItemHolder h = new RowItemHolder(view);
            view.setTag(h);
            return view;
        }
    }

    private final List<JumpClone> jumpClones;

    public CharacterClonesAdapter() {
        super();
        this.jumpClones = new ArrayList<>();
    }

    public void setJumpClones(final List<JumpClone> jumpClones) {
        this.jumpClones.clear();
        this.jumpClones.addAll(jumpClones);
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return jumpClones.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.jumpClones.get(groupPosition).getImplants().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return jumpClones.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.jumpClones.get(groupPosition).getImplants().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return jumpClones.get(groupPosition).getCloneID();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return this.jumpClones.get(groupPosition).getImplants().get(childPosition).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = RowItemHolder.createView(parent);
        }

        RowItemHolder.renderView(view, this.jumpClones.get(groupPosition));
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = RowItemHolder.createView(parent);
        }
        RowItemHolder.renderView(view, this.jumpClones.get(groupPosition).getImplants().get(childPosition));
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
