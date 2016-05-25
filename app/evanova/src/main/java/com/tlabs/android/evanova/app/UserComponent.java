package com.tlabs.android.evanova.app;

import dagger.Component;

@Component(
        dependencies = {ApplicationComponent.class},
        modules = {UserModule.class})
public interface UserComponent extends ApplicationComponent {


}
