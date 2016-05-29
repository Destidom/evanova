package com.tlabs.android.evanova.app.dashboard;


import com.tlabs.android.evanova.app.ApplicationComponent;
import com.tlabs.android.evanova.app.dashboard.main.DashboardActivity;
import com.tlabs.android.evanova.app.dashboard.main.PreferencesFragment;

import dagger.Component;

@Component(
        dependencies = {ApplicationComponent.class},
        modules = {DashboardModule.class})
public interface DashboardComponent {

    void inject(DashboardActivity activity);

    void inject(PreferencesFragment fragment);
}
