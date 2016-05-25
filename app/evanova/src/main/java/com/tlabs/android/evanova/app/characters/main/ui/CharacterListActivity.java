package com.tlabs.android.evanova.app.characters.main.ui;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.characters.CharacterComponent;
import com.tlabs.android.evanova.app.characters.CharacterListView;
import com.tlabs.android.evanova.app.characters.CharacterModule;
import com.tlabs.android.evanova.app.characters.DaggerCharacterComponent;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.BaseFragment;
import com.tlabs.android.jeeves.model.EveCharacter;

import java.util.List;

public class CharacterListActivity extends BaseActivity implements CharacterListView {

    private CharacterComponent component;

    private CharacterListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.component =
                DaggerCharacterComponent
                .builder()
                .applicationComponent(Application.getAppComponent())
                .characterModule(new CharacterModule())
                .build();

        this.fragment = CharacterListFragment.newInstance();
        this.fragment.setRetainInstance(true);
        setFragment(this.fragment);
    }

    @Override
    protected void inject(BaseFragment fragment) {
        component.inject((CharacterListFragment)fragment);
    }

    @Override
    public void showCharacters(List<EveCharacter> characters) {
        this.fragment.showCharacters(characters);
    }
}
