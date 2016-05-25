package com.tlabs.android.evanova.app.corporations;

import com.tlabs.android.evanova.app.ApplicationComponent;
import com.tlabs.android.evanova.app.UserComponent;
import com.tlabs.android.evanova.app.corporations.ui.CorporationViewActivity;
import com.tlabs.android.evanova.app.corporations.ui.CorporationListActivity;

import dagger.Component;

@Component(
        dependencies = {ApplicationComponent.class},

        modules = {CorporationModule.class}
)
public interface CorporationComponent extends UserComponent{

    void inject(CorporationListActivity activity);

    void inject(CorporationViewActivity activity);
}
