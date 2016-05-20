package com.tlabs.android.evanova.app.character;

import com.tlabs.android.evanova.app.EvanovaComponent;
import com.tlabs.android.evanova.app.UserScope;
import com.tlabs.android.evanova.app.character.ui.CharacterActivity;
import com.tlabs.android.evanova.app.character.ui.CharacterListActivity;

import dagger.Component;

@UserScope
@Component(
        dependencies = {EvanovaComponent.class},
        modules = {CharacterModule.class}
)
public interface CharacterComponent {

    void inject(CharacterListActivity activity);

    void inject(CharacterActivity activity);

}
