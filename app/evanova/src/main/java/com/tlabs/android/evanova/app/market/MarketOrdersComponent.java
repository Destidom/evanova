package com.tlabs.android.evanova.app.market;

import com.tlabs.android.evanova.app.ApplicationComponent;
import com.tlabs.android.evanova.app.market.main.MarketOrdersActivity;

import dagger.Component;

@Component(
        dependencies = {ApplicationComponent.class},
        modules = {
                MarketOrdersModule.class}
)
public interface MarketOrdersComponent {

    void inject(MarketOrdersActivity activity);



}
