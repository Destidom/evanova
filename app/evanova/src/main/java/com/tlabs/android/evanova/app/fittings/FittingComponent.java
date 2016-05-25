package com.tlabs.android.evanova.app.fittings;

import com.tlabs.android.evanova.app.ApplicationComponent;
import com.tlabs.android.evanova.app.fittings.ui.ShipFittingActivity;
import com.tlabs.android.evanova.app.fittings.ui.ShipFittingListActivity;

import dagger.Component;

@Component(
        dependencies = {
                ApplicationComponent.class},
        modules = {
                FittingModule.class})
public interface FittingComponent {

    void inject(ShipFittingListActivity activity);
    void inject(ShipFittingActivity activity);

}
