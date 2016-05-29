package com.tlabs.android.evanova.app.characters.main;

import android.os.Bundle;
import android.widget.ViewFlipper;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.characters.CharacterModule;
import com.tlabs.android.evanova.app.characters.DaggerCharacterComponent;
import com.tlabs.android.evanova.app.skills.SkillDatabaseModule;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.model.EveCharacter;

import javax.inject.Inject;

public class CharacterViewActivity extends BaseActivity implements CharacterMainView {

    @Inject
    @Presenter
    CharacterPresenter presenter;

    private CharacterMainWidget wMain;
    private CharacterDetailsWidget wDetails;

    private ViewFlipper flipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerCharacterComponent
                .builder()
                .applicationComponent(Application.getAppComponent())
                .skillDatabaseModule(new SkillDatabaseModule())
                .characterModule(new CharacterModule())
                .build()
                .inject(this);

        this.wDetails = new CharacterDetailsWidget(this);

        this.wMain = new CharacterMainWidget(this);
        this.wMain.setListener(new CharacterMainWidget.Listener() {
            @Override
            public void onCharacterDetailsSelected() {
                presenter.onCharacterDetailsSelected();
            }

            @Override
            public void onCharacterTrainingSelected() {
                presenter.onCharacterTrainingSelected();
            }

            @Override
            public void onCharacterMenuSelected(int buttonId) {
                presenter.onCharacterMenuSelected(buttonId);
            }
        });


        this.flipper = new ViewFlipper(this);
        this.flipper.addView(this.wMain);
        this.flipper.addView(this.wDetails);

        this.setView(this.flipper);
    }

    @Override
    public void onBackPressed() {
        if (this.flipper.getDisplayedChild() == 0) {
            super.onBackPressed();
        }
        else {
            this.flipper.setDisplayedChild(0);
        }
    }

    @Override
    public void setCharacter(EveCharacter character) {
        wMain.setCharacter(character);
        wDetails.setCharacter(character);
    }

    @Override
    public void updateCharacter(EveCharacter character) {
        wMain.setCharacter(character);
    }

    @Override
    public void showCharacterDetails() {
        this.flipper.setDisplayedChild(1);
    }
}

