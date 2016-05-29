package com.tlabs.android.evanova.app.skills;

import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.eve.api.SkillTree;
import com.tlabs.eve.api.character.CertificateTree;

public interface SkillDatabaseUseCase {

    CertificateTree getCertificateTree();

    SkillTree getSkillTree();

    EveCharacter loadCharacter(final long charID);
}
