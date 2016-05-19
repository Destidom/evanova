package com.tlabs.android.jeeves.model;

import com.tlabs.android.jeeves.model.data.sde.EveFacade;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.character.CharacterInfo;
import com.tlabs.eve.api.character.CharacterSheet;
import com.tlabs.eve.api.character.CharacterSkill;
import com.tlabs.eve.api.character.SkillInTraining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EveTraining {

    private final List<SkillInTraining> training = new ArrayList<>();

    private final CharacterSheet sheet;
    private final CharacterInfo info;

    private final Map<String, EveCharacter.Attribute> attributes;

    public EveTraining(final EveTraining from) {
        this(from.sheet, from.info, from.attributes);
        for (SkillInTraining t : from.training) {
            this.training.add(new SkillInTraining(t));
        }
    }

    protected EveTraining(final CharacterSheet sheet, final CharacterInfo info, final Map<String, EveCharacter.Attribute> attributes) {
        this.sheet = sheet;
        this.info = info;
        this.attributes = attributes;
    }

    public long getOwnerID() {
        return this.sheet.getCharacterID();
    }

    public boolean canRemoveTraining(final int position) {
        return EveTrainingHelper.canRemove(this.training, position);
    }

    public void removeTraining(final int position) {
        this.training.remove(position);
        EveTrainingHelper.revalidate(this, training, false);
    }

    public boolean canMoveTraining(final int from, final int to) {
        return EveTrainingHelper.canMove(this.training, from, to);
    }

    public void moveTraining(final int from, final int to) {
        final SkillInTraining t = this.training.remove(from);
        this.training.add(to, t);
        EveTrainingHelper.revalidate(this, training, false);
    }

	public void setTraining(final List<SkillInTraining> training) {
        this.training.clear();
        this.training.addAll(training);
        EveTrainingHelper.revalidate(this, this.training, true);
	}

    //does NOT change anything
    public List<SkillInTraining> plan(final Skill skill, final int level, final EveFacade facade) {
        return EveTrainingHelper.plan(this, skill, level, facade);
    }

    //does NOT check anything
    public void add(final List<SkillInTraining> training) {
        this.training.addAll(training);
        EveTrainingHelper.revalidate(this, this.training, true);
    }

    public List<CharacterSkill> getSkills() {
        return sheet.getSkills();
    }

    public List<SkillInTraining> getAll() {
        return Collections.unmodifiableList(this.training);
    }

    public boolean isEmpty() {
        return this.training.isEmpty();
    }

    public SkillInTraining getTraining(final int index) {
        return this.training.get(index);
    }

    public List<SkillInTraining> getAll(final int type) {
        final List<SkillInTraining> list = new ArrayList<>();
        for (SkillInTraining t: this.training) {
            if (t.getTrainingType() == type) {
                list.add(t);
            }
        }
        return Collections.unmodifiableList(list);
    }

    public int getCount() {
        return this.training.size();
    }

    public int getCount(final int type) {
        int count = 0;
        for (SkillInTraining t: this.training) {
            if (t.getTrainingType() == type) {
                count = count + 1;
            }
        }
        return count;
    }

    public long getEndTime() {
        if (this.training.isEmpty()) {
            return 0;
        }
        return this.training.get(this.training.size() - 1).getEndTime();
    }

    public long getEndTime(final int type) {
        return getEndTimeWithType(type);
    }

    public long getSkillPoints() {
        return this.info.getSkillPoints();
    }

    public CharacterSkill getSkill(long skillID) {
        return sheet.getSkill(skillID);
    }

	public SkillInTraining getActiveTraining() {
        final long now = System.currentTimeMillis();
        for (SkillInTraining t: this.training) {
            if (t.getTrainingType() == SkillInTraining.TYPE_QUEUE && t.getEndTime() > now) {
                return t;
            }
        }
        return null;
	}

    public int getSkillLevel(long skillID) {
        return sheet.getSkillLevel(skillID);
    }

	public final int getTrainingLevel(final long skillID) {
		return getLevelWithType(skillID, SkillInTraining.TYPE_QUEUE);
	}

	public final int getPlanningLevel(final long skillID) {
        return getLevelWithType(skillID, SkillInTraining.TYPE_PLAN);
	}

    public final int getMinPlanningLevel(final long skillID) {
        int queued = 0;
        for (int i = this.training.size() - 1; i >= 0; i--) {
            final SkillInTraining t = this.training.get(i);
            if (t.getSkillID() == skillID) {
                queued = t.getSkillLevel();
                break;
            }
        }
        return Math.max(getSkillLevel(skillID), queued);
    }

	public final SkillInTraining getTrainingSkill(final long skillID) {
		return getTrainingWithType(skillID, SkillInTraining.TYPE_QUEUE);
	}

	public final SkillInTraining getPlanningSkill(final long skillID) {
        return getTrainingWithType(skillID, SkillInTraining.TYPE_PLAN);
	}

    public long getTimeToLevel(final Skill skill, final int level) {
        return EveTrainingHelper.getTimeToLevel(this, skill, level);
    }

    public final boolean hasRequirements(final Skill skill) {
        return hasRequirements(skill, false);
    }

    public final boolean hasRequirements(final Skill skill, final boolean compounded) {
        final Map<Long, Integer> requirements = skill.getRequiredSkills();

        for (Long required: requirements.keySet()) {
            final int requiredLevel = requirements.get(required);
            final int currentLevel = compounded ? getCompoundSkillLevel(skill.getSkillID()) : getSkillLevel(required);

            if (currentLevel < requiredLevel) {
                return false;
            }
        }
        return true;
    }

    public int getCompoundSkillLevel(final long skillID) {
        return Math.max(getSkillLevel(skillID),
                Math.max(getTrainingLevel(skillID), getPlanningLevel(skillID)));
    }

    int getLevelWithType(final long skillID, final int type) {
        for (int i = this.training.size() - 1; i >= 0; i--) {
            final SkillInTraining t = this.training.get(i);
            if (t.getTrainingType() == type && t.getSkillID() == skillID) {
                return t.getSkillLevel();
            }
        }
        return 0;
    }

    SkillInTraining getTrainingWithType(final long skillID, final int type) {
        for (int i = this.training.size() - 1; i >= 0; i--) {
            final SkillInTraining t = this.training.get(i);
            if (t.getTrainingType() == type && t.getSkillID() == skillID) {
                return t;
            }
        }
        return null;
    }

    SkillInTraining getTrainingWithType(final long skillID, final int skillLevel, final int type) {
        for (int i = this.training.size() - 1; i >= 0; i--) {
            final SkillInTraining t = this.training.get(i);
            if (t.getTrainingType() == type && t.getSkillID() == skillID && t.getSkillLevel() == skillLevel) {
                return t;
            }
        }
        return null;
    }

    long getEndTimeWithType(final int type) {
        if (this.training.isEmpty()) {
            return 0;
        }
        for (int i = this.training.size() - 1; i >= 0; i--) {
            final SkillInTraining t = this.training.get(i);
            if (t.getTrainingType() == type) {
                return t.getEndTime();
            }
        }
        return 0;
    }

    Map<String, EveCharacter.Attribute> getAttributes() {
        return attributes;
    }
}
