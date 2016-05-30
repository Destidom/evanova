package com.tlabs.android.jeeves.views.items;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.api.Item;
import com.tlabs.eve.api.Skill;

public class ItemRequirementsWidget extends AbstractListRecyclerView<ItemRequirementsWidget.Requirement> implements ItemWidget {

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

    private static class RequirementHolder extends AbstractListRowHolder<ItemRequirementsWidget.Requirement> {

        private final ImageView imageView;
        private final TextView skillText;
        private final TextView tab;

        public RequirementHolder(final View view) {
            super(view);
            this.imageView = findView(R.id.skillRowImage);
            this.skillText = findView(R.id.skillRowText);
            this.tab = findView(R.id.skillRowTab);
        }

        @Override
        public void render(Requirement requirement) {

        }

        public void render(final Requirement requirement, EveCharacter character) {
            final StringBuilder b = new StringBuilder();
            for (int i = 0; i < requirement.getDepth(); i++) {
                b.append("     ");
            }
            tab.setText(b.toString());
            skillText.setText(requirement.skill.getSkillName() + " " + requirement.skill.getRank());
            renderCharacter(requirement, character);
        }

        private void renderCharacter(final Requirement requirement, EveCharacter character) {
            if (null == character) {
                imageView.setImageLevel(0);
                skillText.setTextColor(Color.WHITE);
                return;
            }

            int skill = character.getTraining().getSkillLevel(requirement.skill.getSkillID());
            if (skill >= requirement.skill.getRank()) {
                imageView.setImageLevel(1);
                skillText.setTextColor(Color.WHITE);
            }
            else if (character.getTraining().getTrainingLevel(requirement.skill.getSkillID()) >= requirement.skill.getRank()) {
                imageView.setImageLevel(3);
                skillText.setTextColor(Color.YELLOW);
            }
            else {
                imageView.setImageLevel(2);
                skillText.setTextColor(Color.GRAY);
            }
        }
    }

    public ItemRequirementsWidget(Context context) {
        super(context);
    }

    public ItemRequirementsWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemRequirementsWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setItem(Item item) {

    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<Requirement> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RequirementHolder(LayoutInflater.from(getContext()).inflate(R.layout.jeeves_row_skill_required, parent, false));
    }

    @Override
    protected long getItemId(Requirement item) {
        return item.hashCode();
    }
}
