package com.tlabs.android.evanova.app.characters.skills;

import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.SkillTree;

import java.util.List;
import java.util.Map;

public interface CharacterSkillsView {

    void showSkillTree(
            final Map<SkillTree.SkillGroup, List<Skill>> tree,
            final EveCharacter character);
}
