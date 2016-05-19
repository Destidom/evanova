package com.tlabs.android.evanova.app.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tlabs.android.evanova.R.id;
import com.tlabs.android.evanova.R.layout;
import com.tlabs.android.jeeves.data.EvanovaDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.EveDatabaseOpenHelper;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacadeImpl;
import com.tlabs.android.jeeves.model.data.sde.EveFacade;
import com.tlabs.android.jeeves.model.data.sde.EveFacadeImpl;
import com.tlabs.android.jeeves.views.character.CharacterListWidget;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CharacterWidgetConfigure extends AppCompatActivity {

    private int appWidgetId;

    private EvanovaFacade evanova;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setResult(RESULT_CANCELED);

        final EveFacade eve = new EveFacadeImpl(EveDatabaseOpenHelper.from(this).getDatabase());
        this.evanova = new EvanovaFacadeImpl(EvanovaDatabaseOpenHelper.from(this).getDatabase(), eve);

        final Intent startIntent = getIntent();
        final Bundle extra = startIntent.getExtras();        
        this.appWidgetId = null == extra ?
                AppWidgetManager.INVALID_APPWIDGET_ID : 
                extra.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);   
        if (this.appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        setContentView(layout.widget_configure);

        final CharacterListWidget listView = (CharacterListWidget)findViewById(id.widgetConfigureView);
        listView.setListener(new CharacterListWidget.Listener() {

            @Override
            public void onItemClicked(EveCharacter character) {
                CharacterWidgetConfigure.this.onCharacterSelected(character.getID());
            }

            @Override
            public void onItemSelected(EveCharacter character, boolean selected) {

            }

            @Override
            public void onItemMoved(EveCharacter character, int from, int to) {

            }
        });

        Observable
                .defer(() -> Observable.from(evanova.getCharacters()))
                .map(id -> evanova.getCharacter(id, false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(c -> listView.mergeItem(c));
    }

    private void onCharacterSelected(final long charId) {
        CharacterWidgetPreferences.saveWidgetPreferences(this, this.appWidgetId, charId);
        CharacterWidgetProvider.updateWidget(this, AppWidgetManager.getInstance(this), charId, this.appWidgetId);
        final Intent result = new Intent();
        result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, this.appWidgetId);
        setResult(RESULT_OK, result);
        finish();        
    }

}
