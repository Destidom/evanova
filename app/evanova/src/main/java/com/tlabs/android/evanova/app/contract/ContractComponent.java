package com.tlabs.android.evanova.app.contract;


import com.tlabs.android.evanova.app.EvanovaComponent;
import com.tlabs.android.evanova.app.contract.ui.ContractActivity;
import com.tlabs.android.evanova.app.UserScope;

import dagger.Component;

@UserScope
@Component(
        dependencies = {EvanovaComponent.class},
        modules = {ContractModule.class})
public interface ContractComponent {

    void inject(ContractActivity activity);
}
