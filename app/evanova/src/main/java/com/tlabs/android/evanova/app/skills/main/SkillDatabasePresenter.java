package com.tlabs.android.evanova.app.skills.main;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.skills.SkillDatabaseUseCase;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.character.Certificate;

import javax.inject.Inject;

public class SkillDatabasePresenter extends EvanovaActivityPresenter<SkillDatabaseView> {

    private SkillDatabaseUseCase useCase;

    @Inject
    public SkillDatabasePresenter(Context context, SkillDatabaseUseCase useCase) {
        super(context);
        this.useCase = useCase;
    }

    @Override
    public void setView(SkillDatabaseView view) {
        super.setView(view);
        setBackgroundDefault();
        subscribe(() -> useCase.getSkillTree(), t -> getView().setSkills(t));
        subscribe(() -> useCase.getCertificateTree(), t -> getView().setCertificates(t));
    }

    @Override
    public void startView(Intent intent) {
        final long charID = ownerOf(intent);
        if (-1l == charID) {
            return;
        }
        subscribe(() -> useCase.loadCharacter(charID), c -> getView().setCharacter(c));
    }

    public void onCertificateSelected(Certificate certificate) {
        getView().showCertificate(certificate);
    }


    public void onSkillSelected(Skill skill) {
        getView().showSkill(skill);
    }
}
