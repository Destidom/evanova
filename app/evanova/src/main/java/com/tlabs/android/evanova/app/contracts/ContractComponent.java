package com.tlabs.android.evanova.app.contracts;


import com.tlabs.android.evanova.app.UserComponent;
import com.tlabs.android.evanova.app.UserScope;
import com.tlabs.android.evanova.app.contracts.ui.ContractActivity;

import dagger.Subcomponent;

@UserScope
@Subcomponent(
        modules = {ContractModule.class})
public interface ContractComponent extends UserComponent {

    void inject(ContractActivity activity);
}
