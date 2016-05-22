package com.tlabs.android.evanova.app.character;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.SkillTree;

import java.util.List;
import java.util.Map;

public interface CharacterSkillView extends ActivityView {

    void showSkillTree(
            final Map<SkillTree.SkillGroup, List<Skill>> tree,
            final EveCharacter character);
}
