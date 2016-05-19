package com.tlabs.android.jeeves.model.data.evanova.entities;

import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.character.CharacterSheet;
import com.tlabs.eve.api.character.CharacterSkill;
import com.tlabs.eve.api.character.SkillInTraining;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

public final class CharacterSkillsEntities {

    private CharacterSkillsEntities() {
    }

    public static List<CharacterSkillEntity> transform(final CharacterSheet sheet) {
        if (null == sheet || CollectionUtils.isEmpty(sheet.getSkills())) {
            return Collections.emptyList();
        }

        final List<CharacterSkillEntity> skills = new ArrayList<>(sheet.getSkills().size());
        for (CharacterSkill s: sheet.getSkills()) {
            final CharacterSkillEntity entity = transform(sheet.getCharacterID(), s);
            skills.add(entity);
        }
        return skills;
    }

    public static SkillInTraining transform(final Skill item, final CharacterSkillEntity entity) {
        final Calendar calendar = GregorianCalendar.getInstance();
        final SkillInTraining skill = new SkillInTraining(item, entity.getSkillLevel());

        skill.setStartSkillPoints(entity.getStartPoints());
        skill.setEndSkillPoints(entity.getEndPoints());
        skill.setEndTime(entity.getEndTime() + calendar.getTimeZone().getOffset(entity.getEndTime()));
        skill.setStartTime(entity.getStartTime() + calendar.getTimeZone().getOffset(entity.getStartTime()));
        skill.setRank(item.getRank());
        skill.setTrainingType(entity.getStatus());



        return skill;
    }

    public static List<CharacterSkillEntity> transform(final long ownerID, final List<SkillInTraining> trainings) {
        final List<CharacterSkillEntity> entities = new ArrayList<>(trainings.size());
        int position = 0;

        for (SkillInTraining t: trainings) {
            final CharacterSkillEntity entity = transform(ownerID, position, t);
            entities.add(entity);
            position = position + 1;
        }
        return entities;
    }

    private static CharacterSkillEntity transform(final long ownerID, final CharacterSkill skill) {
        if (null == skill) {
            return null;
        }

        final CharacterSkillEntity entity = new CharacterSkillEntity();
        entity.setOwnerID(ownerID);
        entity.setSkillID(skill.getSkillID());
        entity.setStartPoints(skill.getSkillPoints());
        entity.setEndPoints(skill.getSkillPoints());
        entity.setSkillLevel(skill.getSkillLevel());
        entity.setStatus(0);
        return entity;
    }

    private static CharacterSkillEntity transform(final long ownerID, final int position, final SkillInTraining skill) {
        if (null == skill) {
            return null;
        }

        final CharacterSkillEntity entity = new CharacterSkillEntity();
        entity.setSkillID(skill.getSkillID());
        entity.setOwnerID(ownerID);
        entity.setStartPoints(skill.getStartSkillPoints());
        entity.setEndPoints(skill.getEndSkillPoints());
        entity.setStartTime(skill.getStartTime());
        entity.setEndTime(skill.getEndTime());
        entity.setSkillLevel(skill.getSkillLevel());
        entity.setPosition(position);
        entity.setStatus(skill.getTrainingType());

        return entity;
    }

}