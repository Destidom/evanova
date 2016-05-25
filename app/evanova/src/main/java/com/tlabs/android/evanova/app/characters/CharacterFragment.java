package com.tlabs.android.evanova.app.characters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.mvp.BaseFragment;
import com.tlabs.android.jeeves.model.EveCharacter;

public abstract class CharacterFragment extends BaseFragment implements CharacterView {

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void showCharacter(EveCharacter character) {

    }

    @Override
    public void updateCharacter(EveCharacter character) {

    }
}
