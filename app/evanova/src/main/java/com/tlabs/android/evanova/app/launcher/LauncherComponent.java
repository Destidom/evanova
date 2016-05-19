package com.tlabs.android.evanova.app.launcher;

import com.tlabs.android.evanova.app.ApplicationComponent;
import com.tlabs.android.evanova.app.launcher.ui.LauncherActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        dependencies = {ApplicationComponent.class},
        modules = {LauncherModule.class})
public interface LauncherComponent {

    void inject(LauncherActivity activity);

}
