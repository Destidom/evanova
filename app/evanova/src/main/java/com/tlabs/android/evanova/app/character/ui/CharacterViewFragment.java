package com.tlabs.android.evanova.app.character.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.ui.ButtonMenuWidget;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.character.CharacterInfoWidget;
import com.tlabs.android.jeeves.views.character.CharacterTrainingDetailsWidget;

public class CharacterViewFragment extends CharacterFragment {

    private CharacterInfoWidget wInfo;
    private CharacterTrainingDetailsWidget wTraining;

    private ButtonMenuWidget wMenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.f_character_view, null);

        this.wInfo = (CharacterInfoWidget)view.findViewById(R.id.f_character_CharacterInfoWidget);
        this.wInfo.setListener(() -> presenter.onCharacterDetailsSelected());

        this.wTraining = (CharacterTrainingDetailsWidget)view.findViewById(R.id.f_character_CharacterTrainingDetailsWidget);
        this.wTraining.setListener(() -> presenter.onCharacterTrainingSelected());

        this.wMenu = (ButtonMenuWidget)view.findViewById(R.id.f_character_ButtonMenuWidget);
        this.wMenu.setListener(buttonId -> presenter.onCharacterMenuSelected(buttonId));
        return view;
    }

    @Override
    protected void onCharacterChanged(EveCharacter character) {
        wMenu.setCharacter(character);
        wInfo.setCharacter(character);
        wTraining.setTraining(character.getTraining());
    }
}
