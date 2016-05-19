package com.tlabs.android.jeeves.model.data.evanova;

import com.tlabs.android.jeeves.model.data.evanova.entities.CharacterCloneEntity;
import com.tlabs.eve.api.character.CharacterSheet;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public final class CharacterClonesEntities {

    private CharacterClonesEntities() {
    }

    public static List<CharacterCloneEntity> transform(final CharacterSheet sheet) {
        if (null == sheet || CollectionUtils.isEmpty(sheet.getJumpClones())) {
            return Collections.emptyList();
        }

        final List<CharacterCloneEntity> clones = new ArrayList<>(sheet.getJumpClones().size());
        for (CharacterSheet.JumpClone c: sheet.getJumpClones()) {
            final CharacterCloneEntity entity = new CharacterCloneEntity();
            entity.setCloneID(c.getCloneID());
            entity.setCloneName(c.getName());
            entity.setOwnerID(sheet.getCharacterID());
            final List<Long> implants = new ArrayList<>();
            for (CharacterSheet.Implant i: sheet.getImplants()) {
                implants.add(i.getTypeID());
            }
            entity.setImplants(implants);
            clones.add(entity);
        }
        return clones;
    }

    public static CharacterSheet.JumpClone transform(final CharacterCloneEntity entity) {
        final CharacterSheet.JumpClone clone = new CharacterSheet.JumpClone();
        clone.setCloneID(entity.getCloneID());
        clone.setLocationID(entity.getLocationID());
        clone.setName(entity.getCloneName());

        for (Long imp: entity.getImplants()) {
            CharacterSheet.Implant implant = new CharacterSheet.Implant();
            implant.setTypeID(imp);
            clone.addImplant(implant);
        }
        return clone;
    }

}