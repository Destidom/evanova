package com.tlabs.android.evanova.app.skills.impl;

import com.tlabs.android.evanova.app.skills.SkillDatabaseUseCase;
import com.tlabs.android.evanova.content.ContentFacade;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.SkillTree;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class SkillDatabaseUseCaseImpl implements SkillDatabaseUseCase {

    private ContentFacade content;

    @Inject
    public SkillDatabaseUseCaseImpl(final ContentFacade content) {
        this.content = content;
    }

    @Override
    public Map<Long, String> getSkillGroups() {
        return content.getSkillGroups();
    }

    @Override
    public final Map<SkillTree.SkillGroup, List<Skill>> getSkillTree() {
        return Collections.emptyMap();
    }
}
