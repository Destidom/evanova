package com.tlabs.android.evanova.app.characters.list;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.jeeves.model.EveCharacter;

import java.util.List;

interface CharacterListView extends ActivityView {

    void showCharacters(final List<EveCharacter> characters);
}
