package com.tlabs.android.evanova.app.contracts;


import com.tlabs.android.evanova.app.ApplicationComponent;
import com.tlabs.android.evanova.app.contracts.main.ContractActivity;

import dagger.Component;

@Component(
        dependencies = {ApplicationComponent.class},
        modules = {ContractModule.class})
public interface ContractComponent {

    void inject(ContractActivity activity);
}
