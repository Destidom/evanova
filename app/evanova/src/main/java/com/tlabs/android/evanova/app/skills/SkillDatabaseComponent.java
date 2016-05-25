package com.tlabs.android.evanova.app.skills;

import com.tlabs.android.evanova.app.ApplicationComponent;
import com.tlabs.android.evanova.app.skills.ui.SkillDatabaseActivity;

import dagger.Component;

@Component(
        dependencies = {ApplicationComponent.class},
        modules = {SkillDatabaseModule.class})
public interface SkillDatabaseComponent {

    void inject(SkillDatabaseActivity activity);
}
