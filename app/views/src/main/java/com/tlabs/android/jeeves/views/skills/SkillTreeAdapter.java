package com.tlabs.android.jeeves.views.skills;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.eve.api.EveAPI;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.SkillTree;
import com.tlabs.eve.api.character.SkillInTraining;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


class SkillTreeAdapter extends BaseExpandableListAdapter {

	public static final int SHOW_CURRENT = 0;
	public static final int SHOW_TRAINABLE = 1;
	public static final int SHOW_ALL = 2;

    static class SkillGroup {
        private long groupID;
        private String groupName;

        private int inPlanning;
        private int inTraining;
    }

	static final class SkillHolder {
		private static final int LAYOUT = R.layout.row_skills_child;

		private final TextView skillNameText;
        private final TextView skillPointsText;
        private final SkillLevelWidget skillLevelView;
		
		private SkillHolder(View view) {
			super();
			this.skillNameText = (TextView)view.findViewById(R.id.skillNameText);
            this.skillPointsText = (TextView)view.findViewById(R.id.skillPointsText);
            this.skillLevelView = (SkillLevelWidget)view.findViewById(R.id.skillLevelView);
		}
		
		public final void render(final Skill skill, final EveCharacter character) {
            if (null == character) {
                renderSkill(skill);
            }
            else {
                renderCharacter(skill, character);
            }
		}

        private void renderSkill(final Skill skill) {
            skillLevelView.setVisibility(View.GONE);
            skillNameText.setText(skill.getSkillName() + " x" + skill.getRank());
            skillNameText.setTextColor(Color.WHITE);

            skillPointsText.setText(Html.fromHtml(skill.getDescription()));
            skillPointsText.setTextColor(Color.WHITE);
        }

        private void renderCharacter(final Skill skill, final EveCharacter character) {
            skillLevelView.setVisibility(View.VISIBLE);
            skillNameText.setText(skill.getSkillName() + " x" + skill.getRank());

            final int training = character.getTraining().getTrainingLevel(skill.getSkillID());
            if (training != 0) {
                renderTraining(skill, character, training);
                return;
            }

            final int planning = character.getTraining().getPlanningLevel(skill.getSkillID());
            if (planning != 0) {
                renderPlanning(skill, character, planning);
                return;
            }

            final int level = character.getTraining().getSkillLevel(skill.getSkillID());
            if (level != 0) {
                renderTrained(skill, character, level);
                return;
            }

            renderNew(skill, character);
        }

        private void renderNew(final Skill skill, final EveCharacter character) {
            skillNameText.setTextColor(Color.WHITE);
            skillPointsText.setTextColor(Color.WHITE);
            if (character.getTraining().hasRequirements(skill)) {
                skillLevelView.setShowRequired(true);
                skillPointsText.setText(R.string.jeeeves_skills_requirements_matched);
            }
            else {
                skillLevelView.setShowRequired(false);
                skillPointsText.setText(R.string.jeeeves_skills_requirements_unmatched);
            }
        }

        private void renderTraining(final Skill skill, final EveCharacter character, final int training) {
            final SkillInTraining t = character.getTraining().getTrainingSkill(skill.getSkillID());
            if (t.getEndTime() < System.currentTimeMillis()) {
                renderTrained(skill, character, training);
                return;
            }

            skillNameText.setTextColor(Color.YELLOW);
            skillLevelView.setSkillLevel(character.getTraining().getSkillLevel(skill.getSkillID()), training);

            skillPointsText.setTextColor(Color.YELLOW);
            skillPointsText.setText(t.getStartSkillPoints() + " / " + t.getEndSkillPoints() + " SP");
        }

        private void renderPlanning(final Skill skill, final EveCharacter character, final int planning) {
            final int orange = skillNameText.getResources().getColor(R.color.orange);
            skillNameText.setTextColor(orange);

            skillLevelView.setSkillLevel(character.getTraining().getSkillLevel(skill.getSkillID()), planning);

            skillPointsText.setTextColor(orange);
            final SkillInTraining t = character.getTraining().getPlanningSkill(skill.getSkillID());
            skillPointsText.setText(t.getStartSkillPoints() + " / " + t.getEndSkillPoints() + " SP");
        }

        private void renderTrained(final Skill skill, final EveCharacter character, final int level) {
            skillNameText.setTextColor(Color.WHITE);
            skillPointsText.setTextColor(Color.WHITE);
            skillLevelView.setSkillLevel(level);

            if (level == 5) {
                final long sp = EveAPI.getRequiredSkillPoints(skill.getRank(), 5);
                skillPointsText.setText(sp + " / " + sp + " SP");
            }
            else {
                skillPointsText.setText(
                        EveAPI.getRequiredSkillPoints(skill.getRank(), level) + " / " + EveAPI.getRequiredSkillPoints(skill.getRank(), level + 1) + " SP");
            }
        }
	}

    static class GroupHolder {

        private final TextView skillGroupText;
        private final TextView skillGroupDescription1;
        private final TextView skillGroupDescription2;

        public GroupHolder(final View view) {
            this.skillGroupText = (TextView)view.findViewById(R.id.skillGroupText);
            this.skillGroupDescription1 = (TextView)view.findViewById(R.id.skillGroupDescription1);
            this.skillGroupDescription2 = (TextView)view.findViewById(R.id.skillGroupDescription2);
        }

        public void render(final SkillGroup group, final EveCharacter character) {
            if (null == character) {
                renderGroup(group);
            }
            else {
                renderGroup(group, character);
            }
        }

        private void renderGroup(final SkillGroup group) {
            skillGroupText.setTextColor(Color.WHITE);
            skillGroupText.setText(group.groupName);

            skillGroupDescription1.setVisibility(View.GONE);
            skillGroupDescription2.setVisibility(View.GONE);
        }

        private void renderGroup(final SkillGroup group, final EveCharacter character) {
            if ((group.inTraining == 0) && (group.inPlanning == 0)) {
                renderGroup(group);
                return;
            }
            skillGroupDescription1.setVisibility(View.VISIBLE);
            skillGroupDescription2.setVisibility(View.VISIBLE);

            skillGroupText.setTextColor(Color.WHITE);
            skillGroupText.setText(group.groupName);

            if (group.inTraining == 0) {
                final int orange = skillGroupText.getResources().getColor(R.color.orange);

                skillGroupDescription1.setTextColor(orange);
                //I18N
                if (group.inPlanning == 1) {
                    skillGroupDescription1.setText(group.inPlanning + " skill planned.");
                }
                else {
                    skillGroupDescription1.setText(group.inPlanning + " skills planned.");
                }
                skillGroupDescription2.setText("");
                return;
            }

            skillGroupDescription1.setTextColor(Color.YELLOW);
            //I18N
            if (group.inTraining == 1) {
                skillGroupDescription1.setText(group.inTraining + " skill in training.");
            }
            else {
                skillGroupDescription1.setText(group.inTraining + " skills in training.");
            }

            if (group.inPlanning == 0) {
                skillGroupDescription2.setText("");
                return;
            }

            final int orange = skillGroupText.getResources().getColor(R.color.orange);
            skillGroupDescription2.setTextColor(orange);
            //I18N
            if (group.inPlanning == 1) {
                skillGroupDescription2.setText(group.inPlanning + " skill planned.");
            }
            else {
                skillGroupDescription2.setText(group.inPlanning + " skills planned.");
            }
        }

        public static void render(final View view, final SkillGroup group, final EveCharacter character) {
            final GroupHolder h = (GroupHolder)view.getTag();
            h.render(group, character);
        }

        public static View createView(Context context, boolean isExpanded) {
            final View view = LayoutInflater.from(context).inflate(R.layout.row_skills, null, false);
            GroupHolder tag = new GroupHolder(view);
            view.setTag(tag);
            return view;
        }
    }

    private final Map<SkillGroup, List<Skill>> skills;
    private final Map<SkillGroup, List<Skill>> filtered;
    private final List<SkillGroup> groups;

    private int show = SHOW_ALL;
    private EveCharacter character;

    public SkillTreeAdapter(final SkillTree tree) {
        this(toLegacyParams(tree));
    }

    private static Map<SkillGroup, List<Skill>> toLegacyParams(final SkillTree tree) {
        final Map<SkillGroup, List<Skill>> skills = new HashMap<>();
        for (SkillTree.SkillGroup g: tree.getGroups()) {
            SkillGroup ng = new SkillGroup();
            ng.groupID = g.getGroupID();
            ng.groupName = g.getGroupName();

            skills.put(ng, g.getSkills());
        }
        return skills;
    }

	private SkillTreeAdapter(final Map<SkillGroup, List<Skill>> skills) {
        this.skills = skills;
        this.filtered = new LinkedHashMap<>();
        this.groups = new ArrayList<>();
        this.groups.addAll(skills.keySet());

        this.show = SHOW_ALL;
        this.character = null;
        reset();
	}

    public void setCharacter(final EveCharacter character) {
        this.character = character;
        reset();
        notifyDataSetChanged();
    }

    public int getShow() {
        return this.show;
    }

    public void setShow(final int show) {
        if (this.show == show) {
            return;
        }

        this.show = show;
        reset();
        notifyDataSetChanged();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = GroupHolder.createView(parent.getContext(), isExpanded);
        }
        GroupHolder.render(view, groups.get(groupPosition), this.character);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            view = LayoutInflater.from(parent.getContext()).inflate(SkillHolder.LAYOUT, parent, false);
            view.setTag(new SkillHolder(view));
        }
        final SkillHolder h = (SkillHolder)view.getTag();
        h.render((Skill)getChild(groupPosition, childPosition), character);
        return view;
    }

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
        final SkillGroup group = this.groups.get(groupPosition);
        return this.filtered.get(group).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
        final SkillGroup group = this.groups.get(groupPosition);
        return this.filtered.get(group).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groups.get(groupPosition).groupID;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
        final SkillGroup group = this.groups.get(groupPosition);
		return this.filtered.get(group).get(childPosition).getSkillID();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

    private void reset() {
        this.filtered.clear();

        for (Map.Entry<SkillGroup, List<Skill>> e: this.skills.entrySet()) {
            final List<Skill> skills = new ArrayList<>();
            skills.addAll(e.getValue());
            this.filtered.put(e.getKey(), skills);
        }

        if (null == this.character) {
            return;
        }

        train(this.filtered, character);
        filter(this.filtered, character, this.show);
    }

    private static void train(final Map<SkillGroup, List<Skill>> all, final EveCharacter character) {
        for (Map.Entry<SkillGroup, List<Skill>> e: all.entrySet()) {

            int training = 0;
            int planning = 0;
            for (Skill s: e.getValue()) {
                if (character.getTraining().getTrainingLevel(s.getSkillID()) > 0) {
                    training = training + 1;
                }
                if (character.getTraining().getPlanningLevel(s.getSkillID()) > 0) {
                    planning = planning + 1;
                }
            }
            e.getKey().inPlanning = planning;
            e.getKey().inTraining = training;
        }
    }

    private static void filter(final Map<SkillGroup, List<Skill>> all, final EveCharacter character, final int show) {
        for (List<Skill> skills: all.values()) {
            CollectionUtils.filter(skills, object -> {
                final Skill skill = object;
                final boolean hasSkill = (character.getTraining().getSkillLevel(skill.getSkillID()) > 0) ||
                        (character.getTraining().getTrainingLevel(skill.getSkillID()) > 0) ||
                        (character.getTraining().getPlanningLevel(skill.getSkillID()) > 0);
                switch (show) {
                    case SHOW_CURRENT:
                        return hasSkill;

                    case SHOW_TRAINABLE:
                        return hasSkill || character.getTraining().hasRequirements(skill);

                    case SHOW_ALL:
                    default:
                        return true;
                }
            });
        }
    }

}

