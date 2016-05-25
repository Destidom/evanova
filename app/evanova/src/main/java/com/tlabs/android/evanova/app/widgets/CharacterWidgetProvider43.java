package com.tlabs.android.evanova.app.widgets;

import android.content.Context;
import android.widget.RemoteViews;

import com.tlabs.android.evanova.R.id;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.model.EveTraining;
import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.eve.api.character.SkillInTraining;

import java.util.List;

public final class CharacterWidgetProvider43 extends CharacterWidgetProvider {

    static void updateCharacterWidget(final Context context, final RemoteViews views, final int appWidgetId, final EveCharacter character) {
        EveImages.load(
                EveImages.getCharacterImageURL(context, character.getID()),
                context, views,
                id.widgetCharacterPortrait,
                new int[]{appWidgetId});

        updateCharacterInfo(context, views, character);
        updateCharacterTraining(context, views, character);
        updateImpl(context, views, character);
    }
    
    private static void updateImpl(Context context, final RemoteViews views, final EveCharacter character) {
        final EveTraining training = character.getTraining();
        final List<SkillInTraining> inTraining = training.getAll(SkillInTraining.TYPE_QUEUE);

        if (inTraining.isEmpty()) {
            views.setTextViewText(id.widgetTrainingNextTitle, "");
            views.setTextViewText(id.widgetTrainingNextText, "");
            views.setTextViewText(id.widgetMoreInfoText, "");
            return;
        }
        if (inTraining.size() == 1) {
            views.setTextViewText(id.widgetTrainingNextTitle, "No other training in queue.");
            views.setTextViewText(id.widgetTrainingNextText, "");
            views.setTextViewText(id.widgetMoreInfoText, "");
            return;
        }
        
        final SkillInTraining next = inTraining.get(1);
        views.setTextViewText(id.widgetTrainingNextTitle, "Next Training");
        views.setTextViewText(id.widgetTrainingNextText, next.getSkillName() + " " + next.getSkillLevel());
        views.setTextViewText(id.widgetMoreInfoText, "");
    }
    
    private static void updateCharacterInfo(final Context context, final RemoteViews views, final EveCharacter character) {
        if (null == views) {
            return;
        }

        views.setTextViewText(id.widgetCharacterClone, "");//TODO clone
        views.setTextViewText(id.widgetCharacterCorporation, character.getCorporationName());
        
        if (character.getBalance() <= 0) {
            views.setTextViewText(id.widgetCharacterISK, "");
        }
        else {
            views.setTextViewText(id.widgetCharacterISK, EveFormat.Currency.LONG(character.getBalance(), true));
        }
        views.setTextViewText(id.widgetCharacterLocation, character.getLocation().getLocationName());
    }
}
