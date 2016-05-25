package com.tlabs.android.evanova.app;

import android.content.Context;

import com.tlabs.android.jeeves.network.EveCrest;
import com.tlabs.android.jeeves.service.EveAPIServicePreferences;

import org.devfleet.crest.CrestAccess;
import org.devfleet.crest.CrestService;
import org.devfleet.dotlan.DotlanService;
import org.devfleet.dotlan.impl.DotlanServiceImpl;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Application application;
    private final EveAPIServicePreferences apiPreferences;

    public ApplicationModule(Application application) {
        this.application = application;
        this.apiPreferences = new EveAPIServicePreferences(application);
    }

    @Provides
    public Context provideApplicationContext() {
        return this.application.getApplicationContext();
    }

    @Provides
    public Application provideApplication() {
        return this.application;
    }


    @Provides
    @Named("apiURL")
    public String provideApiURL() {
        return this.apiPreferences.getApiKeyInstallUri();
    }

    @Provides
    @Named("crestURL")
    public String provideCrestCharacterURL() {
        return EveCrest.buildLoginUri(application, CrestAccess.CHARACTER_SCOPES);
    }


    @Provides
    @Named("publicCrest")
    public CrestService providePublicCrest() {
        return EveCrest.obtainService(this.application);
    }

    @Provides
    public DotlanService provideDotlan() {
        return new DotlanServiceImpl();
    }

}
