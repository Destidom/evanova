package com.tlabs.android.jeeves.model;

import com.tlabs.android.jeeves.model.data.sde.EveFacade;
import com.tlabs.eve.api.EveAPI;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.character.CharacterSkill;
import com.tlabs.eve.api.character.SkillInTraining;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

final class EveTrainingHelper {

    static boolean canRemove(final List<SkillInTraining> skills, final int position) {
        final SkillInTraining move = skills.get(position);
        for (int i = position + 1; i < skills.size(); i++) {
            if (skills.get(i).requires(move)) {
                return false;
            }
        }
        return true;
    }

    static boolean canMove(final List<SkillInTraining> skills, final int from, final int to) {
        if (from == to) {
            return false;
        }
        if (from < 0 || from >= skills.size()) {
            return false;
        }
        if (to < 0 || to >= skills.size()) {
            return false;
        }

        final SkillInTraining fromTraining = skills.get(from);
        if (fromTraining.getTrainingType() != SkillInTraining.TYPE_PLAN) {
            return false;
        }

        if (from < to) {
            //going down
            for (int i = from + 1; i <= to; i++) {
                if (skills.get(i).requires(fromTraining)) {
                    return false;
                }
            }
        }
        else {
            //going up
            for (int i = from - 1; i >= to; i--) {
                if (fromTraining.requires(skills.get(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    static long getTimeToLevel(final EveTraining training, final Skill skill, final int level) {
        SkillInTraining inTrainingNow = training.getActiveTraining();
        if (
                null != inTrainingNow &&
                        inTrainingNow.getSkillID() == skill.getSkillID() &&
                        inTrainingNow.getSkillLevel() == level) {
            return inTrainingNow.getEndTime() - System.currentTimeMillis();
        }

        SkillInTraining inTrainingQueue = training.getTrainingWithType(skill.getSkillID(), level, SkillInTraining.TYPE_QUEUE);
        if (null != inTrainingQueue) {
            return inTrainingQueue.getEndTime() - inTrainingQueue.getStartTime();
        }
        return getAbsoluteTimeToLevel(training, skill, level);
    }

    static List<SkillInTraining> plan(final EveTraining training, final Skill skill, final int level, final EveFacade eve) {
        final List<SkillInTraining> plan = planImpl(training, skill, level, eve);

        long last = training.getEndTime();
        for (SkillInTraining p: plan) {
            p.setStartTime(last);
            p.setEndTime(last + getTimeToLevel(training, p, p.getSkillLevel()));
            p.setTrainingType(SkillInTraining.TYPE_PLAN);

            last = p.getEndTime();
        }
        return plan;
    }

    static void revalidate(final EveTraining eveTraining, final List<SkillInTraining> training, boolean filter) {
        if (filter) {
            filterExisting(eveTraining.getSkills(), training);
            filterCompleted(training);
        }

        Collections.sort(training, new Comparator<SkillInTraining>() {
            @Override
            public int compare(SkillInTraining lhs, SkillInTraining rhs) {
                if (lhs.getTrainingType() == SkillInTraining.TYPE_QUEUE && rhs.getTrainingType() == SkillInTraining.TYPE_QUEUE) {
                    return Long.compare(lhs.getStartTime(), rhs.getStartTime());
                }
                if (lhs.getTrainingType() == SkillInTraining.TYPE_QUEUE) {
                    return -1;
                }
                if (rhs.getTrainingType() == SkillInTraining.TYPE_QUEUE) {
                    return 1;
                }
                return 0;
            }
        });

        if (training.isEmpty()) {
            return;
        }

        final Calendar calendar = GregorianCalendar.getInstance();
        long last = 0;
        for (SkillInTraining t: training) {
            if (t.getTrainingType() == SkillInTraining.TYPE_PLAN) {
                t.setStartTime(last == 0 ? System.currentTimeMillis() + calendar.getTimeZone().getOffset(System.currentTimeMillis()) : last);
                long duration = getAbsoluteTimeToLevel(eveTraining, t, t.getSkillLevel());
                t.setEndTime(t.getStartTime() + duration);
            }
            last = t.getEndTime();
        }
    }

    private static long getAbsoluteTimeToLevel(final EveTraining training, final Skill skill, final int level) {
        CharacterSkill charSkill = training.getSkill(skill.getSkillID());
        long neededSP = 0;
        if (null == charSkill) {
            neededSP = EveAPI.getRequiredSkillPoints(skill.getRank(), level);
        }
        else if (charSkill.getSkillLevel() >= level) {
            neededSP = 0;
        }
        else {
            neededSP = EveAPI.getRequiredSkillPoints(skill.getRank(), level) - charSkill.getSkillPoints();
        }
        return
            (long)(neededSP / getSkillPointsPerMillis(training, skill));
    }

    private static void filterExisting(final List<CharacterSkill> skills, final List<SkillInTraining> candidates) {
        CollectionUtils.filter(candidates, new Predicate() {
            @Override
            public boolean evaluate(Object input) {
                final SkillInTraining t = (SkillInTraining)input;
                for (CharacterSkill s: skills) {
                    if (s.getSkillID() == t.getSkillID() && s.getSkillLevel() >= t.getSkillLevel()) {
                        return false;
                    }
                }
                return true;
            }
        });
    }

    private static void filterCompleted(final List<SkillInTraining> candidates) {
        CollectionUtils.filter(candidates, new Predicate() {
            @Override
            public boolean evaluate(Object input) {
                final SkillInTraining t = (SkillInTraining)input;
                if (t.getTrainingType() == SkillInTraining.TYPE_QUEUE && t.getEndTime() < System.currentTimeMillis()) {
                    return false;
                }
                return true;
            }
        });
    }

    private static List<SkillInTraining> planImpl(final EveTraining training, final Skill skill, final int level, final EveFacade eve) {
        final int existing = training.getCompoundSkillLevel(skill.getSkillID());

        if (existing >= level) {
            return Collections.emptyList();
        }

        final List<SkillInTraining> plan = new ArrayList<>();
        for (Map.Entry<Long, Integer> req: skill.getRequiredSkills().entrySet()) {
            final Skill r = eve.getSkill(req.getKey());
            r.setRank(req.getValue());
            plan.addAll(planImpl(training, r, req.getValue(), eve));
        }
        for (int i = existing + 1; i <= level; i++) {
            final SkillInTraining t = new SkillInTraining(skill, i);
            plan.add(t);
        }

        return plan;
    }

    private static float getSkillPointsPerMillis(final EveTraining training, final Skill skill) {
        EveCharacter.Attribute baseAttr =  training.getAttributes().get(skill.getPrimaryAttribute());
        if (null == baseAttr) {
            throw new IllegalArgumentException("Unknown ItemAttribute '" + skill.getPrimaryAttribute() + "'");
        }

        EveCharacter.Attribute secondAttr =  training.getAttributes().get(skill.getSecondaryAttribute());
        if (null == secondAttr) {
            throw new IllegalArgumentException("Unknown ItemAttribute '" + "'" + skill.getSecondaryAttribute());
        }
        return (baseAttr.getValue() + secondAttr.getValue() / 2.0f) / (60f * 1000f);
    }

}
