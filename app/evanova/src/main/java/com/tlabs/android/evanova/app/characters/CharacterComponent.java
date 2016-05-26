package com.tlabs.android.evanova.app.characters;

import com.tlabs.android.evanova.app.ApplicationComponent;
import com.tlabs.android.evanova.app.characters.ui.CharacterListActivity;
import com.tlabs.android.evanova.app.characters.ui.CharacterViewActivity;
import com.tlabs.android.evanova.app.characters.ui.CharacterViewFragment;
import com.tlabs.android.evanova.app.mails.MailModule;
import com.tlabs.android.evanova.app.skills.SkillDatabaseModule;

import dagger.Component;

@Component(
        dependencies = {ApplicationComponent.class},
        modules = {
                SkillDatabaseModule.class,
                CharacterModule.class,
                MailModule.class}
)
public interface CharacterComponent {

    void inject(CharacterListActivity activity);

    void inject(CharacterViewActivity activity);

    void inject(CharacterViewFragment fragment);


}
