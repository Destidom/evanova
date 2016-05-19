package com.tlabs.android.jeeves.notifications;

import android.content.Context;

import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.eve.api.character.CharacterSheet;
import com.tlabs.eve.api.character.SkillInTraining;

import java.util.List;

final class CharacterTrainingInspector extends CharacterInspector {

    private EvanovaFacade facade;

    public CharacterTrainingInspector(Context base, final EvanovaFacade facade, final CharacterSheet sheet) {
        super(base, sheet);
        this.facade = facade;
    }

    @Override
    Notification inspect(CharacterSheet sheet) {
        if (!getPreferences().getCharacterTrainingNotificationOption(sheet.getCharacterID())) {
            return null;
        }

        final List<SkillInTraining> queue = facade.getTrainingQueue(sheet.getCharacterID());
        if (queue.isEmpty()) {
            return null;
        }
        final SkillInTraining last = queue.get(queue.size() - 1);
        if (last.getStartTime() <= 0 || last.getEndTime() <= 0) {
            return null;
        }
        //I18N
        final long remaining = last.getEndTime() - System.currentTimeMillis();
        if (remaining <= 0) {
            final String message = last.getSkillName() + " " + last.getSkillLevel() + " completed.";
            return Notification.from(this, sheet).
                    setTitle(sheet.getCharacterName() + " Training").
                    setText(message);
        }
        if (remaining <= 1000L * 3600L * 24L) {
            final String message =
                    queue.size() == 1 ?
                    FormatHelper.Duration.MEDIUM(remaining) + " left on " + last.getSkillName() + " " + last.getSkillLevel() :
                    "Training queue expires in " + FormatHelper.Duration.MEDIUM(remaining);
            return Notification.from(this, sheet).
                    setTitle(sheet.getCharacterName() + " Training").
                    setWhen(last.getEndTime()).
                    setText(message);
        }
        return null;
    }
}
