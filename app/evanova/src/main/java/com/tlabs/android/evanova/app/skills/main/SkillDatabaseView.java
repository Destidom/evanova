package com.tlabs.android.evanova.app.skills.main;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.SkillTree;
import com.tlabs.eve.api.character.Certificate;
import com.tlabs.eve.api.character.CertificateTree;

interface SkillDatabaseView extends ActivityView {

    void setCharacter(final EveCharacter character);

    void setSkills(final SkillTree tree);

    void setCertificates(final CertificateTree tree);

    void showSkill(final Skill skill);

    void showCertificate(final Certificate certificate);
}
