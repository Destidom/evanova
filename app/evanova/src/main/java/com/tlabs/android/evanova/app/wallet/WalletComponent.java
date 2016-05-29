package com.tlabs.android.evanova.app.wallet;

import com.tlabs.android.evanova.app.ApplicationComponent;
import com.tlabs.android.evanova.app.wallet.WalletModule;
import com.tlabs.android.evanova.app.wallet.main.WalletActivity;

import dagger.Component;

@Component(
        dependencies = {ApplicationComponent.class},
        modules = {WalletModule.class}
)
public interface WalletComponent {

    void inject(WalletActivity activity);

}
