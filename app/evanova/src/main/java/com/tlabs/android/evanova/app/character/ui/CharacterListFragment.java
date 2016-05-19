package com.tlabs.android.evanova.app.character.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.app.character.presenter.CharacterListPresenter;
import com.tlabs.android.evanova.mvp.BaseFragment;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.character.CharacterListWidget;

import java.util.List;

public class CharacterListFragment extends BaseFragment {

    private CharacterListWidget listView;
    private CharacterListPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
        this.listView = new CharacterListWidget(getContext());
        this.listView.setListener(new CharacterListWidget.Listener() {
            @Override
            public void onItemClicked(EveCharacter character) {
                presenter.onCharacterSelected(character.getID());
            }
        });
        return this.listView;
    }

    public void setPresenter(CharacterListPresenter presenter) {
        this.presenter = presenter;
    }

    public void setCharacters(final List<EveCharacter> characters) {
        this.listView.setItems(characters);
    }
}
