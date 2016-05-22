package com.tlabs.android.evanova.app.skills;

import com.tlabs.android.evanova.app.EvanovaComponent;
import com.tlabs.android.evanova.app.UserScope;
import com.tlabs.android.evanova.app.skills.ui.SkillDatabaseActivity;

import dagger.Component;

@UserScope
@Component(
        dependencies = {EvanovaComponent.class},
        modules = {SkillDatabaseModule.class})
public interface SkillDatabaseComponent {

    void inject(SkillDatabaseActivity activity);
}
