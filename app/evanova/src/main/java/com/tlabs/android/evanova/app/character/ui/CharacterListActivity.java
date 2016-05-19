package com.tlabs.android.evanova.app.character.ui;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.character.CharacterListView;
import com.tlabs.android.evanova.app.character.CharacterModule;
import com.tlabs.android.evanova.app.character.DaggerCharacterComponent;
import com.tlabs.android.evanova.app.character.presenter.CharacterListPresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.jeeves.model.EveCharacter;

import java.util.List;

import javax.inject.Inject;

public class CharacterListActivity extends BaseActivity implements CharacterListView {

    @Inject
    CharacterListPresenter presenter;

    private CharacterListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCharacterComponent
                .builder()
                .evanovaComponent(Application.getEveComponent())
                .characterModule(new CharacterModule())
                .build()
                .inject(this);

        this.fragment = new CharacterListFragment();
        this.fragment.setPresenter(presenter);

        setFragment(this.fragment);
        this.presenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.destroyView();
        this.presenter = null;
    }

    @Override
    public void showCharacters(List<EveCharacter> characters) {
        this.fragment.setCharacters(characters);
    }
}
