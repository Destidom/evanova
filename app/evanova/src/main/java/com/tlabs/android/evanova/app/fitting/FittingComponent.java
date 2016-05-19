package com.tlabs.android.evanova.app.fitting;

import com.tlabs.android.evanova.app.EvanovaComponent;
import com.tlabs.android.evanova.app.fitting.ui.ShipFittingActivity;
import com.tlabs.android.evanova.app.fitting.ui.ShipFittingListActivity;
import com.tlabs.android.evanova.app.UserScope;

import dagger.Component;

@UserScope
@Component(
        dependencies = {
                EvanovaComponent.class},
        modules = {
                FittingModule.class})
public interface FittingComponent {

    void inject(ShipFittingListActivity activity);
    void inject(ShipFittingActivity activity);

}
