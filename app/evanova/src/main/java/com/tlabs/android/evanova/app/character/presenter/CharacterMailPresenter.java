package com.tlabs.android.evanova.app.character.presenter;

import com.tlabs.android.evanova.app.character.CharacterMailView;
import com.tlabs.android.evanova.mvp.ViewPresenter;
import com.tlabs.android.jeeves.model.EveCharacter;

import javax.inject.Inject;

final class CharacterMailPresenter extends ViewPresenter<CharacterMailView> {

    private EveCharacter character;

    @Inject
    public CharacterMailPresenter() {
    }

    public void setCharacter(EveCharacter character) {
        this.character = character;
    }
}
