package com.tlabs.android.evanova.app.corporation;

import com.tlabs.android.evanova.app.EvanovaComponent;
import com.tlabs.android.evanova.app.corporation.ui.CorporationActivity;
import com.tlabs.android.evanova.app.corporation.ui.CorporationListActivity;
import com.tlabs.android.evanova.app.UserScope;

import dagger.Component;

@UserScope
@Component(
        dependencies = {EvanovaComponent.class},
        modules = {CorporationModule.class}
)
public interface CorporationComponent {

    void inject(CorporationListActivity activity);

    void inject(CorporationActivity activity);
}
