package com.tlabs.android.evanova.app.character.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tlabs.android.evanova.app.character.presenter.CharacterPresenter;
import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.evanova.mvp.BaseFragment;
import com.tlabs.android.jeeves.model.EveCharacter;

public abstract class CharacterFragment extends BaseFragment {

    protected CharacterPresenter presenter;

    protected EveCharacter character;

    public void setPresenter(CharacterPresenter presenter) {
        this.presenter = presenter;
    }

    public final void setCharacter(final EveCharacter character) {
        this.character = character;
        if ((null != character) && (null != getView())) {
            onCharacterChanged(character);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        onCharacterChanged(character);
    }

    protected void onCharacterChanged(final EveCharacter character) {}

}
