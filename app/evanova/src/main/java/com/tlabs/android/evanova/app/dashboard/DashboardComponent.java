package com.tlabs.android.evanova.app.dashboard;



import com.tlabs.android.evanova.app.EvanovaComponent;
import com.tlabs.android.evanova.app.dashboard.ui.DashboardActivity;
import com.tlabs.android.evanova.app.dashboard.ui.PreferencesFragment;
import com.tlabs.android.evanova.app.UserScope;

import dagger.Component;

@UserScope
@Component(
        dependencies = {EvanovaComponent.class},
        modules = {DashboardModule.class})
public interface DashboardComponent {

    void inject(DashboardActivity activity);

    void inject(PreferencesFragment fragment);
}
