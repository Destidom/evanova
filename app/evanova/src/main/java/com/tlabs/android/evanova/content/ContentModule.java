package com.tlabs.android.evanova.content;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ContentModule {

    @Provides
    public ContentPublisher provideContentPublisher(final Context context) {
        return new ContentPublisher(context.getApplicationContext());
    }
    @Provides
    public ContentFacade provideContentFacade(ContentFacadeImpl impl) {
        return impl;
    }


}
