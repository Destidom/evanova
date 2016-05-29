package com.tlabs.android.evanova.app.characters.calendar;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.characters.CharacterModule;
import com.tlabs.android.evanova.app.characters.DaggerCharacterComponent;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.views.character.CharacterCalendarWidget;
import com.tlabs.eve.api.character.CharacterCalendar;

import java.util.List;

import javax.inject.Inject;

public class CharacterCalendarActivity extends BaseActivity implements CharacterCalendarView {

    @Inject
    @Presenter
    CharacterCalendarPresenter presenter;

    private CharacterCalendarWidget wCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCharacterComponent.builder().
                applicationComponent(Application.getAppComponent())
                .characterModule(new CharacterModule())
                .build()
                .inject(this);

        this.wCalendar = new CharacterCalendarWidget(this);
        this.wCalendar.setListener(entries -> {

        });
        setView(this.wCalendar);
    }

    @Override
    public void setCalendar(final CharacterCalendar calendar) {
        this.wCalendar.setCalendar(calendar);
    }

}
