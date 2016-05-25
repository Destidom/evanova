package com.tlabs.android.evanova.app.mails;

import com.tlabs.android.evanova.app.mails.impl.EveMailUseCaseImpl;
import com.tlabs.android.evanova.app.mails.impl.KillMailUseCaseImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class MailModule {

    @Provides
    public EveMailUseCase provideEveMailUseCase(final EveMailUseCaseImpl impl) {
        return impl;
    }


    @Provides
    public KillMailUseCase provideKillMailUseCase(final KillMailUseCaseImpl impl) {
        return impl;
    }
}
