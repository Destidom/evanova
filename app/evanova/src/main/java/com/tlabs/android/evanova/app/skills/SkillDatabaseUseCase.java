package com.tlabs.android.evanova.app.skills;

import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.SkillTree;

import java.util.List;
import java.util.Map;

public interface SkillDatabaseUseCase {

    Map<Long, String> getSkillGroups();

    Map<SkillTree.SkillGroup, List<Skill>> getSkillTree();
}
