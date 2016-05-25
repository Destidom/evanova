package com.tlabs.android.evanova.app.characters;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.jeeves.model.EveCharacter;

import java.util.List;

public interface CharacterListView extends ActivityView {

    void showCharacters(final List<EveCharacter> characters);
}
