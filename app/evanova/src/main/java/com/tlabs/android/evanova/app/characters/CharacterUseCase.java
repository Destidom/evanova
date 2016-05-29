package com.tlabs.android.evanova.app.characters;

import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.eve.api.character.CharacterCalendar;

import java.util.List;

import rx.Observer;
import rx.Subscription;

public interface CharacterUseCase {

    List<EveCharacter> loadCharacters();

    EveCharacter loadCharacter(final long charID);

    CharacterCalendar loadCalendar(final long charID);

    Subscription subscribe(
            final EveCharacter character,
            final Observer<EveCharacter> observer);

}
