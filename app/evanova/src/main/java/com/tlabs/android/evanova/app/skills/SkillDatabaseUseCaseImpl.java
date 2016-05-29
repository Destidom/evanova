package com.tlabs.android.evanova.app.skills;

import com.tlabs.android.evanova.app.skills.SkillDatabaseUseCase;
import com.tlabs.android.evanova.content.ContentFacade;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.eve.api.SkillTree;
import com.tlabs.eve.api.character.CertificateTree;

import javax.inject.Inject;

public class SkillDatabaseUseCaseImpl implements SkillDatabaseUseCase {

    private ContentFacade content;

    @Inject
    public SkillDatabaseUseCaseImpl(final ContentFacade content) {
        this.content = content;
    }

    @Override
    public SkillTree getSkillTree() {
        return content.getSkills();
    }

    @Override
    public CertificateTree getCertificateTree() {
        return content.getCertificates();
    }

    @Override
    public EveCharacter loadCharacter(long charID) {
        return this.content.getCharacter(charID, true);
    }
}
