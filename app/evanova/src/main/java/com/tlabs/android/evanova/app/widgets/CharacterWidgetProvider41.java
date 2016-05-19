package com.tlabs.android.evanova.app.widgets;

import android.content.Context;
import android.widget.RemoteViews;

import com.tlabs.android.evanova.R.id;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.EveImages;


public final class CharacterWidgetProvider41 extends CharacterWidgetProvider {

    static void updateCharacterWidget(final Context context, final RemoteViews views, final int appWidgetId, final EveCharacter character) {
        EveImages.load(
                EveImages.getCharacterIconURL(context, character.getID()),
                context,
                views,
                id.widgetCharacterPortrait,
                new int[]{appWidgetId});

        updateCharacterTraining(context, views, character);
    }    
}
