package com.tlabs.android.jeeves.views.skills;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.eve.api.EveAPI;
import com.tlabs.eve.api.Skill;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class SkillViewWidget extends FrameLayout implements SkillWidget {

    //Icons map (@drawable/attribute_level_list)
    private static final Map<String, Integer> attributeLevelMap = new HashMap<>();
    static {
        attributeLevelMap.put(EveAPI.ATTR_INTELLIGENCE, 0);
        attributeLevelMap.put(EveAPI.ATTR_PERCEPTION, 1);
        attributeLevelMap.put(EveAPI.ATTR_CHARISMA, 2);
        attributeLevelMap.put(EveAPI.ATTR_WILLPOWER, 3);
        attributeLevelMap.put(EveAPI.ATTR_MEMORY, 4);
    }

    static class AttributeBoxHolder {
        private final ImageView skillPrimaryImage;
        private final ImageView skillSecondaryImage;
        private final TextView skillDescription;
        private final TextView skillPrimaryText;
        private final TextView skillSecondaryText;
        private final TextView skillTimeMultiplierTitle;
        private final TextView skillTimeMultiplierText;

        public AttributeBoxHolder(final View view) {
            this.skillPrimaryImage = (ImageView)view.findViewById(R.id.skillPrimaryImage);
            this.skillSecondaryImage = (ImageView)view.findViewById(R.id.skillSecondaryImage);
            this.skillDescription = (TextView)view.findViewById(R.id.skillDescription);
            this.skillPrimaryText = (TextView)view.findViewById(R.id.skillPrimaryText);
            this.skillSecondaryText = (TextView)view.findViewById(R.id.skillSecondaryText);
            this.skillTimeMultiplierTitle = (TextView)view.findViewById(R.id.skillTimeMultiplierTitle);
            this.skillTimeMultiplierText = (TextView)view.findViewById(R.id.skillTimeMultiplierText);

        }

        public void render(final Skill skill) {
            skillDescription.setText(Html.fromHtml(skill.getDescription()));

            String primaryAttr = skill.getPrimaryAttribute();
            if (StringUtils.isBlank(primaryAttr)) {

            }
            else {
                skillPrimaryText.setText(StringUtils.capitalize(primaryAttr));
                skillPrimaryImage.setImageLevel(attributeLevelMap.get(primaryAttr.toLowerCase()));
            }

            String secondaryAttr = skill.getSecondaryAttribute();
            if (StringUtils.isBlank(secondaryAttr)) {

            }
            else {
                skillSecondaryText.setText(StringUtils.capitalize(secondaryAttr));
                skillSecondaryImage.setImageLevel(attributeLevelMap.get(secondaryAttr.toLowerCase()));
            }

            //I18N
            skillTimeMultiplierText.setText("x" + skill.getRank());
        }
    }

    static class TrainingBoxHolder {
        private final TextView  skillTrainingTitle;
        private final ImageView skillTrainingDetailsImage1;
        private final TextView  skillTrainingDetailsText1;
        private final TextView  skillTrainingDetailsText2;
        private final ImageView skillTrainingDetailsImage2;
        private final ViewGroup skillTrainingBox;

        public TrainingBoxHolder(final View view) {
            this.skillTrainingTitle = (TextView)view.findViewById(R.id.skillTrainingTitle);
            this.skillTrainingDetailsImage1 = (ImageView)view.findViewById(R.id.skillTrainingDetailsImage1);
            this.skillTrainingDetailsText1 = (TextView)view.findViewById(R.id.skillTrainingDetailsText1);
            this.skillTrainingDetailsText2 = (TextView)view.findViewById(R.id.skillTrainingDetailsText2);
            this.skillTrainingDetailsImage2 = (ImageView)view.findViewById(R.id.skillTrainingDetailsImage2);
            this.skillTrainingBox = (ViewGroup)view.findViewById(R.id.skillTrainingBox);

        }
/*
        public void render(final ContentFacade content, final Skill skill, final EveCharacter character) {
            if (null == character) {
                this.skillTrainingBox.setVisibility(View.GONE);
                return;
            }
            this.skillTrainingBox.setVisibility(View.VISIBLE);

            final int level = character.getTraining().getSkillLevel(skill.getSkillID());
            if (level == 5) {
                renderCompleted();
                return;
            }

            final int training = character.getTraining().getTrainingLevel(skill.getSkillID());
            final int planning = character.getTraining().getPlanningLevel(skill.getSkillID());
            if ((training == 0) && (planning == 0)) {
                if (level == 0) {
                    renderNew(content, skill, character);
                }
                else {
                    renderTrained(skill, character, level);
                }
                return;
            }
            renderTraining(skill, character, level, training, planning);
        }

        private void renderNew(final ContentFacade content, final Skill skill, final EveCharacter character) {
            final Map<Long, Integer> requirements = skill.getRequiredSkills();
            final List<Skill> required = new ArrayList<>(requirements.size());
            for (Long id: requirements.keySet()) {
                final Skill req = content.getSkill(id);
                if (null != req) {
                    required.add(req);
                }
            }
            switch (required.size()) {
                case 0: {
                    skillTrainingDetailsImage1.setVisibility(View.INVISIBLE);
                    skillTrainingDetailsText1.setVisibility(View.INVISIBLE);

                    skillTrainingDetailsImage2.setVisibility(View.INVISIBLE);
                    skillTrainingDetailsText2.setVisibility(View.INVISIBLE);
                    break;
                }
                case 1: {
                    skillTrainingDetailsImage1.setVisibility(View.VISIBLE);
                    skillTrainingDetailsText1.setVisibility(View.VISIBLE);

                    skillTrainingDetailsImage1.setImageLevel(2);
                    skillTrainingDetailsText1.setTextColor(Color.WHITE);
                    skillTrainingDetailsText1.setText(required.get(0).getSkillName());

                    skillTrainingDetailsImage2.setVisibility(View.INVISIBLE);
                    skillTrainingDetailsText2.setVisibility(View.INVISIBLE);
                    break;
                }
                default:
                    skillTrainingDetailsImage1.setVisibility(View.VISIBLE);
                    skillTrainingDetailsImage1.setImageLevel(2);

                    skillTrainingDetailsText1.setVisibility(View.VISIBLE);
                    skillTrainingDetailsText1.setTextColor(Color.WHITE);
                    skillTrainingDetailsText1.setText(required.get(0).getSkillName());

                    skillTrainingDetailsImage2.setVisibility(View.VISIBLE);
                    skillTrainingDetailsImage2.setImageLevel(2);

                    skillTrainingDetailsText2.setVisibility(View.VISIBLE);
                    skillTrainingDetailsText2.setText(required.get(1).getSkillName());
                    break;

            }
        }
*/
        private void renderTrained(final Skill skill, final EveCharacter character, final int level) {
            skillTrainingDetailsImage1.setImageLevel(1);
            skillTrainingDetailsText1.setTextColor(Color.WHITE);
            skillTrainingDetailsText1.setText("Trained to level " + level);

            final long timeToLevelInMillis = character.getTraining().getTimeToLevel(skill, level + 1);
            skillTrainingDetailsImage2.setImageLevel(0);
            skillTrainingDetailsText2.setTextColor(Color.WHITE);
            skillTrainingDetailsText2.setText(EveFormat.Duration.MEDIUM(timeToLevelInMillis) + " to level " + (level + 1));
        }

        private void renderCompleted() {
            skillTrainingDetailsImage1.setImageLevel(1);
            skillTrainingDetailsText1.setTextColor(Color.WHITE);
            skillTrainingDetailsText1.setText("Trained to level 5");

            skillTrainingDetailsImage2.setImageLevel(1);
            skillTrainingDetailsText2.setTextColor(Color.WHITE);
            skillTrainingDetailsText2.setText("Training completed");
        }

        private void renderTraining(final Skill skill, final EveCharacter character, int level, int training, int planning) {
            if (level == 0) {
                skillTrainingDetailsImage1.setImageLevel(3);
                skillTrainingDetailsText1.setTextColor(Color.WHITE);
                skillTrainingDetailsText1.setText("No training");
            }
            else {
                skillTrainingDetailsImage1.setImageLevel(1);
                skillTrainingDetailsText1.setTextColor(Color.WHITE);
                skillTrainingDetailsText1.setText("Trained to level " + level);
            }

            if (planning == 0) {
                final long timeToLevelInMillis = character.getTraining().getTimeToLevel(skill, training);
                skillTrainingDetailsImage2.setImageLevel(3);
                skillTrainingDetailsText2.setTextColor(Color.YELLOW);
                skillTrainingDetailsText2.setText(EveFormat.Duration.MEDIUM(timeToLevelInMillis) + " to level " + training);
            }
            else if (training == 0) {
                final long timeToLevelInMillis = character.getTraining().getTimeToLevel(skill, planning);
                skillTrainingDetailsImage2.setImageLevel(3);
                skillTrainingDetailsText2.setTextColor(skillTrainingDetailsText2.getContext().getResources().getColor(R.color.orange));
                skillTrainingDetailsText2.setText(EveFormat.Duration.MEDIUM(timeToLevelInMillis) + " to level " + planning);
            }
            else {
                final long timeToLevelInMillis = character.getTraining().getTimeToLevel(skill, training);
                skillTrainingDetailsImage1.setImageLevel(3);
                skillTrainingDetailsText1.setTextColor(Color.YELLOW);
                skillTrainingDetailsText1.setText(EveFormat.Duration.MEDIUM(timeToLevelInMillis) + " to level " + training);

                final long timeToPlanningInMillis = character.getTraining().getTimeToLevel(skill, planning);
                skillTrainingDetailsImage2.setImageLevel(3);
                skillTrainingDetailsText2.setTextColor(skillTrainingDetailsText2.getContext().getResources().getColor(R.color.orange));
                skillTrainingDetailsText2.setText(EveFormat.Duration.MEDIUM(timeToPlanningInMillis) + " to level " + planning);
            }
        }
    }

    private AttributeBoxHolder attributesHolder;
    private TrainingBoxHolder trainingHolder;

    public SkillViewWidget(Context context) {
        super(context);
        init();
    }

    public SkillViewWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SkillViewWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setSkill(final Skill skill) {
        this.attributesHolder.render(skill);
    }

    private void init() {
        inflate(getContext(), R.layout.jeeves_view_skill, this);

        this.attributesHolder = new AttributeBoxHolder(this);
        this.trainingHolder = new TrainingBoxHolder(this);
    }

}
