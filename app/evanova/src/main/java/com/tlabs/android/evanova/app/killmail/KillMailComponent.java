package com.tlabs.android.evanova.app.killmail;

import com.tlabs.android.evanova.app.EvanovaComponent;
import com.tlabs.android.evanova.app.UserScope;

import dagger.Component;

@UserScope
@Component(
        dependencies = {EvanovaComponent.class},
        modules = {KillMailModule.class}
)
public interface KillMailComponent {
}
