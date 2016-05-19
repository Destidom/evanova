package com.tlabs.android.evanova.app.accounts;

import com.tlabs.android.evanova.app.ApplicationComponent;
import com.tlabs.android.evanova.app.UserScope;
import com.tlabs.android.evanova.app.accounts.ui.AccountActivity;

import dagger.Component;

@UserScope
@Component(
        dependencies = {ApplicationComponent.class},
        modules = {AccountModule.class})
public interface AccountComponent {

    void inject(AccountActivity activity);
}
