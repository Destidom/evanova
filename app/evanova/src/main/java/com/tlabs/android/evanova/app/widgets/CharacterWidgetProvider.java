package com.tlabs.android.evanova.app.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.tlabs.android.evanova.R.id;
import com.tlabs.android.evanova.R.layout;
import com.tlabs.android.evanova.R.string;
import com.tlabs.android.evanova.app.characters.main.CharacterPresenter;
import com.tlabs.android.evanova.app.characters.main.CharacterViewActivity;
import com.tlabs.android.jeeves.data.EvanovaDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.EveDatabaseOpenHelper;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.model.EveTraining;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacadeImpl;
import com.tlabs.android.jeeves.model.data.sde.EveFacade;
import com.tlabs.android.jeeves.model.data.sde.EveFacadeImpl;
import com.tlabs.android.jeeves.service.events.CharacterUpdateEndEvent;
import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.util.Log;
import com.tlabs.eve.api.character.SkillInTraining;

import java.util.List;

//I18N
public class CharacterWidgetProvider extends AppWidgetProvider {
  
    private static final long H24 = 24 * 60 * 60 * 1000;
    
    protected static final String LOG = "EvanovaApplication.Widget";
            
    protected CharacterWidgetProvider() {
        super();
    }
    
    //used by CharacterWidgetConfigure; I wish we could do otherwise
    public static void updateWidget(final Context context, final AppWidgetManager appWidgetManager, final long charId, final int appWidgetId) {
        final EveFacade eve = new EveFacadeImpl(EveDatabaseOpenHelper.from(context).getDatabase());
        final EvanovaFacade facade = new EvanovaFacadeImpl(EvanovaDatabaseOpenHelper.from(context).getDatabase(), eve);

        final EveCharacter character = facade.getCharacter(charId, true);
        if (null == character) {
            return;
        }

        Intent intent = new Intent(context, CharacterViewActivity.class);
        intent.putExtra(CharacterPresenter.EXTRA_OWNER_ID, charId);

         // needed to clear the back stack and make sure we get back to the screen
         //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);  
         intent.addFlags(/*Intent.FLAG_ACTIVITY_NO_HISTORY |*/ Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
         RemoteViews views = new RemoteViews(context.getPackageName(), appWidgetManager.getAppWidgetInfo(appWidgetId).initialLayout);        
         views.setOnClickPendingIntent(id.widgetCharacter, PendingIntent.getActivity(context, (int)charId, intent, 0));
         views.setTextViewText(id.widgetCharacterName, character.getName());
         
         switch (views.getLayoutId()) {         
         case layout.widget_1x1:
             CharacterWidgetProvider11.updateCharacterWidget(context, views, appWidgetId, character);
             break;
         case layout.widget_4x1:
             CharacterWidgetProvider41.updateCharacterWidget(context, views, appWidgetId, character);
             break;
         case layout.widget_4x3:
             CharacterWidgetProvider43.updateCharacterWidget(context, views, appWidgetId, character);
             break;
         }
         appWidgetManager.updateAppWidget(appWidgetId, views);
    }    
    
    @Override
    public final void onReceive(Context context, Intent intent) {
        if (CharacterUpdateEndEvent.class.getName().equals(intent.getAction())) {
            onReceiveScreenDeviceUpdated(context);
            return;
        }
        if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
            onReceiveScreenDeviceUpdated(context);
            return;
        }
        super.onReceive(context, intent);
    }
    
    @Override
    public final void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        for (int appWidgetId: appWidgetIds) {
            final long charId = CharacterWidgetPreferences.getWidgetCharacterId(context, appWidgetId);
            if (charId == -1) {
                if (Log.D) Log.d(LOG, getClass().getSimpleName() + ".onUpdate: no charId for widget " + appWidgetId);
            }
            else {                          
                updateWidget(context, appWidgetManager, charId, appWidgetId);                
            }
        }
    }

    @Override
    public final void onDeleted(Context context, int[] appWidgetIds) {
        CharacterWidgetPreferences.deleteWidgetPreferences(context, appWidgetIds);
    }

    protected static void updateCharacterTraining(final Context context, final RemoteViews views, final EveCharacter character) {
        final EveTraining training = character.getTraining();
        final List<SkillInTraining> inTraining = training.getAll(SkillInTraining.TYPE_QUEUE);

        if (inTraining.isEmpty()) {
            views.setTextViewText(id.widgetTrainingText, context.getResources().getText(string.character_characterTrainingQueued_None));
            views.setTextViewText(id.widgetTrainingTimeText, "");
            views.setProgressBar(id.widgetProgressBar, 100, 0, false);
            return;
        }

        final SkillInTraining first = inTraining.get(0);
        final SkillInTraining last = inTraining.get(inTraining.size() - 1);
        final long now = System.currentTimeMillis();

        views.setTextViewText(id.widgetTrainingText, first.getSkillName() + " " + first.getSkillLevel());
        if (last.getEndTime() <= now) {
            views.setTextViewText(id.widgetTrainingTimeText, context.getResources().getText(string.character_characterTrainingQueued_Inactive));
            views.setTextColor(id.widgetTrainingTimeText, Color.GRAY);
            views.setProgressBar(id.widgetProgressBar, 100, 0, false);
        }
        else {
            final long totalRemaining = last.getEndTime() - now;
            views.setTextViewText(id.widgetTrainingTimeText, "Ends " + EveFormat.DateTime.MEDIUM(first.getEndTime()));
            views.setTextColor(id.widgetTrainingTimeText, EveFormat.getDurationColor(totalRemaining));
            if (totalRemaining <= 0) {
                views.setProgressBar(id.widgetProgressBar, 100, 0, false);
            }
            else if (totalRemaining >= H24) {
                views.setProgressBar(id.widgetProgressBar, 100, 100, false);
            }
            else {
                int percent = 100 - (int)((H24 - totalRemaining) * 100L / H24);
                views.setProgressBar(id.widgetProgressBar, 100, percent, false);
            }
        }
    }

    private void onReceiveScreenDeviceUpdated(Context context) {
        final ComponentName componentName = new ComponentName(context.getPackageName(), getClass().getName());            
        final AppWidgetManager appWidgetManager =  AppWidgetManager.getInstance(context);
        final int[] widgetIds = appWidgetManager.getAppWidgetIds(componentName);
        
        for (int widgetId: widgetIds) {
            final long widgetCharId = CharacterWidgetPreferences.getWidgetCharacterId(context, widgetId);
            if (widgetCharId != -1) {
                updateWidget(context, appWidgetManager, widgetCharId, widgetId);
            }
        }                  
    }    
}
