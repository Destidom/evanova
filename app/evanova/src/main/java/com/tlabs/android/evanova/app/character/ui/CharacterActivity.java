package com.tlabs.android.evanova.app.character.ui;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.character.CharacterModule;
import com.tlabs.android.evanova.app.character.CharacterView;
import com.tlabs.android.evanova.app.character.DaggerCharacterComponent;
import com.tlabs.android.evanova.app.character.presenter.CharacterPresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.jeeves.model.EveCharacter;

import javax.inject.Inject;

public class CharacterActivity extends BaseActivity implements CharacterView {

    public static final String EXTRA_CHAR_ID = CharacterActivity.class.getSimpleName() + ".charID";

    @Inject
    CharacterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCharacterComponent
            .builder()
            .evanovaComponent(Application.getEveComponent())
            .characterModule(new CharacterModule())
            .build()
            .inject(this);

        this.presenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.destroyView();
        this.presenter = null;
    }

    @Override
    public void showMainView(EveCharacter character) {
        showCharacterFragment(new CharacterViewFragment(), character, false);
    }

    @Override
    public void showDetails(EveCharacter character) {
        showCharacterFragment(new CharacterDetailsFragment(), character, true);
    }

    @Override
    public void showTraining(EveCharacter character) {
        showCharacterFragment(new CharacterTrainingFragment(), character, true);
    }

    private <T extends CharacterFragment> void showCharacterFragment(final T f, final EveCharacter character, final boolean stack) {
        f.setPresenter(this.presenter);
        f.setCharacter(character);
        if (stack) {
            stackFragment(f);
        }
        else {
            setFragment(f);
        }
    }
}
