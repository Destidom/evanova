package com.tlabs.android.evanova.app.widgets;

import android.content.Context;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.tlabs.android.evanova.R.id;
import com.tlabs.android.jeeves.model.EveCharacter;

import com.tlabs.android.jeeves.model.EveTraining;
import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.eve.api.character.SkillInTraining;


public final class CharacterWidgetProvider11 extends CharacterWidgetProvider {

    static void updateCharacterWidget(final Context context, final RemoteViews views, final int appWidgetId, final EveCharacter character) {
        EveImages.load(
                EveImages.getCharacterIconURL(context, character.getID()),
                context,
                views,
                id.widgetCharacterPortrait,
                new int[]{appWidgetId});

        int textColor;
        final EveTraining training = character.getTraining();

        if (training.isEmpty()) {
            textColor = Color.GRAY;
        }
        else {
            final long now = System.currentTimeMillis();
            final long end = training.getEndTime(SkillInTraining.TYPE_QUEUE);
            textColor = end <= 0 ? Color.GRAY : EveFormat.getDurationColor(end - now);
        }
        views.setTextColor(id.widgetCharacterName, textColor);
    }

}
