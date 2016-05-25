package com.tlabs.android.evanova.app.dashboard;


import com.tlabs.android.evanova.app.ApplicationComponent;
import com.tlabs.android.evanova.app.dashboard.ui.DashboardActivity;
import com.tlabs.android.evanova.app.dashboard.ui.PreferencesFragment;

import dagger.Component;

@Component(
        dependencies = {ApplicationComponent.class},
        modules = {DashboardModule.class})
public interface DashboardComponent {

    void inject(DashboardActivity activity);

    void inject(PreferencesFragment fragment);
}
