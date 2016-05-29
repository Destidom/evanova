package com.tlabs.android.evanova.app.characters.calendar;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.characters.CharacterUseCase;

import javax.inject.Inject;

public class CharacterCalendarPresenter extends EvanovaActivityPresenter<CharacterCalendarView> {

    private final CharacterUseCase useCase;

    @Inject
    public CharacterCalendarPresenter(Context context, CharacterUseCase useCase) {
        super(context);
        this.useCase = useCase;
    }

    @Override
    public void startView(Intent intent) {
        final long ownerID = ownerOf(intent);
        if (-1l == ownerID) {
            return;
        }
        showLoading(true);
        subscribe(() -> useCase.loadCalendar(ownerID), c -> {
            getView().setCalendar(c);
            showLoading(false);
        });

    }
}
