package com.tlabs.android.evanova.app.items;

import com.tlabs.android.evanova.app.EvanovaComponent;
import com.tlabs.android.evanova.app.UserScope;
import com.tlabs.android.evanova.app.items.ui.ItemDatabaseActivity;

import dagger.Component;

@UserScope
@Component(
        dependencies = {EvanovaComponent.class},
        modules = {ItemDatabaseModule.class})
public interface ItemDatabaseComponent {

    void inject(ItemDatabaseActivity activity);
}
