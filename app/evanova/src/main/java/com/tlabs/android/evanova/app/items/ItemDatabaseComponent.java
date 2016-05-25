package com.tlabs.android.evanova.app.items;

import com.tlabs.android.evanova.app.ApplicationComponent;
import com.tlabs.android.evanova.app.items.ui.ItemDatabaseActivity;

import dagger.Component;

@Component(
        dependencies = {ApplicationComponent.class},
        modules = {ItemDatabaseModule.class})
public interface ItemDatabaseComponent {

    void inject(ItemDatabaseActivity activity);
}
