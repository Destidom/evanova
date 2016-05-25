package com.tlabs.android.evanova.app.characters.main.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.app.characters.CharacterListView;
import com.tlabs.android.evanova.app.characters.presenter.CharacterListPresenter;
import com.tlabs.android.evanova.mvp.BaseFragment;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.character.CharacterListWidget;

import java.util.List;

import javax.inject.Inject;

public class CharacterListFragment extends BaseFragment implements CharacterListView {

    public static CharacterListFragment newInstance() {
        final CharacterListFragment f = new CharacterListFragment();
        return f;
    }

    private CharacterListWidget listView;

    @Inject
    @Presenter
    CharacterListPresenter presenter;

    @Override
    public View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
        this.listView = new CharacterListWidget(getContext());
        this.listView.setListener(new CharacterListWidget.Listener() {
            @Override
            public void onItemClicked(EveCharacter character) {
                presenter.onCharacterSelected(character.getID());
            }
        });
        return this.listView;
    }

    @Override
    public void showCharacters(List<EveCharacter> characters) {
        this.listView.setItems(characters);
    }
}
