package com.tlabs.android.evanova.app.characters.main.ui;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.characters.CharacterListView;
import com.tlabs.android.evanova.app.characters.CharacterModule;
import com.tlabs.android.evanova.app.characters.DaggerCharacterComponent;
import com.tlabs.android.evanova.app.characters.presenter.CharacterListPresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.character.CharacterListWidget;

import java.util.List;

import javax.inject.Inject;

public class CharacterListActivity extends BaseActivity implements CharacterListView {

    private CharacterListWidget listView;

    @Inject
    @Presenter
    CharacterListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCharacterComponent
                .builder()
                .applicationComponent(Application.getAppComponent())
                .characterModule(new CharacterModule())
                .build()
                .inject(this);

        this.listView = new CharacterListWidget(this);
        this.listView.setListener(new CharacterListWidget.Listener() {
            @Override
            public void onItemClicked(EveCharacter character) {
                presenter.onCharacterSelected(character.getID());
            }
        });
        setView(this.listView);
    }

    @Override
    public void showCharacters(List<EveCharacter> characters) {
        this.listView.setItems(characters);
    }
}
