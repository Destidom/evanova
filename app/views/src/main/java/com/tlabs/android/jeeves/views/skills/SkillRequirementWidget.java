package com.tlabs.android.jeeves.views.skills;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.api.Skill;

public class SkillRequirementWidget extends AbstractListRecyclerView<SkillRequirementWidget.Requirement> {
    public static class Requirement {
        private Requirement parent;
        private Skill skill;

        public int getDepth() {
            if (null == parent) {
                return 0;
            }
            return parent.getDepth() + 1;
        }
    }

    private static class RequirementHolder extends AbstractListRowHolder<Requirement> {

        private final ImageView itemImage;
        private final TextView itemName;
        private final TextView itemDescription;

        public RequirementHolder(final View view) {
            super(view);
            this.itemImage = (ImageView)view.findViewById(R.id.rowItemIcon);
            this.itemImage.setImageResource(R.drawable.jeeves_level_list_required_skill);
            this.itemName = (TextView)view.findViewById(R.id.rowItemName);
            this.itemDescription = (TextView)view.findViewById(R.id.rowItemDescription);
        }

        @Override
        public void render(Requirement requirement) {

        }

        public void render(final Requirement requirement, EveCharacter character) {
            final StringBuilder b = new StringBuilder();
            for (int i = 0; i < requirement.getDepth(); i++) {
                b.append("     ");
            }
            itemDescription.setText(b.toString());
            itemName.setText(requirement.skill.getSkillName() + " " + requirement.skill.getRank());
            renderCharacter(requirement, character);
        }

        private void renderCharacter(final Requirement requirement, EveCharacter character) {
            if (null == character) {
                itemImage.setImageLevel(0);
                itemName.setTextColor(Color.WHITE);
                return;
            }

            int skill = character.getTraining().getSkillLevel(requirement.skill.getSkillID());
            if (skill >= requirement.skill.getRank()) {
                itemImage.setImageLevel(1);
                itemName.setTextColor(Color.WHITE);
            }
            else if (character.getTraining().getTrainingLevel(requirement.skill.getSkillID()) >= requirement.skill.getRank()) {
                itemImage.setImageLevel(3);
                itemName.setTextColor(Color.YELLOW);
            }
            else {
                itemImage.setImageLevel(2);
                itemName.setTextColor(Color.GRAY);
            }
        }
    }

    public SkillRequirementWidget(Context context) {
        super(context);
    }

    public SkillRequirementWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SkillRequirementWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSkill(final Skill skill, final EveCharacter character) {

    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<Requirement> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RequirementHolder(LayoutInflater.from(getContext()).inflate(R.layout.row_item_small, parent, false));
    }

    @Override
    protected long getItemId(Requirement item) {
        return item.skill.getGroupID() << 32 & item.skill.getSkillID();
    }
}
