package com.tlabs.android.evanova.app.mails;

import com.tlabs.android.evanova.app.ApplicationComponent;
import com.tlabs.android.evanova.app.mails.ui.MailActivity;

import dagger.Component;

@Component(
        dependencies = {ApplicationComponent.class},
        modules = {MailModule.class}
)
public interface MailComponent {

    void inject(MailActivity activity);
}
