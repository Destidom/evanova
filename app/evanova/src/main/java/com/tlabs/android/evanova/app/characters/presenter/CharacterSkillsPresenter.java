package com.tlabs.android.evanova.app.characters.presenter;

import android.content.Context;

import com.tlabs.android.evanova.app.characters.CharacterUseCase;
import com.tlabs.android.evanova.app.characters.skills.CharacterSkillsView;
import com.tlabs.android.evanova.app.skills.SkillDatabaseUseCase;
import com.tlabs.android.evanova.mvp.ViewPresenter;
import com.tlabs.android.jeeves.model.EveCharacter;

import javax.inject.Inject;

final class CharacterSkillsPresenter extends ViewPresenter<CharacterSkillsView> {

    private final CharacterUseCase characterUseCase;
    private final SkillDatabaseUseCase skillsUseCase;

    private EveCharacter character;

    @Inject
    public CharacterSkillsPresenter(
            final Context context,
            final CharacterUseCase characterUseCase,
            final SkillDatabaseUseCase skillsUseCase) {
       // super(context);

        this.characterUseCase = characterUseCase;
        this.skillsUseCase = skillsUseCase;
    }

    public void setCharacter(EveCharacter character) {
        this.character = character;
    }

    @Override
    public void setView(CharacterSkillsView view) {
        super.setView(view);
      //  showLoading(true);
        subscribe(() -> skillsUseCase.getSkillTree(), tree -> {
            getView().showSkillTree(tree, this.character);
         //   showLoading(false);
        });

    }
}
