/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This file has been modified to fit this MVP package.
 */
package com.tlabs.android.evanova.mvp;

import android.content.Intent;

import org.apache.commons.lang.Validate;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * Analyzes an Activity or Fragment with Presenter annotations passed as parameter to obtain a list
 * of Presenter instances to be linked to the source lifecycle.
 */
final class PresenterLifeCycle {

    public static class NotAccessibleException extends RuntimeException {
        public NotAccessibleException() {
        }

        public NotAccessibleException(String detailMessage) {
            super(detailMessage);
        }

        public NotAccessibleException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public NotAccessibleException(Throwable throwable) {
            super(throwable);
        }
    }

    private final Set<ViewPresenter> presenters = new HashSet<>();

    /**
     * Obtains all the presenter instances declared in the source param, configures the associated
     * view and initializes the presenters lifecycle.
     *
     * @param source used to obtain the presenter references.
     * @param view to be configured as presenters view.
     */
    public void initializeLifeCycle(Object source, Object view) {
        Validate.notNull(source, "source cannot be null");
        Validate.notNull(view, "view cannot be null");

        addAnnotatedPresenter(source);
        setView(view);
    }

    /**
     * Updates all the already registered presenters lifecycle and updates the view instance
     * associated to these presenters.
     *
     */
    public void onResume() {
        for (ViewPresenter presenter : presenters) {
            presenter.resumeView();
        }
    }

    public void onStart(final Intent intent) {
        for (ViewPresenter presenter : presenters) {
            presenter.startView(intent);
        }
    }
    /**
     * Pauses all the already registered presenters lifecycle.
     */
    public void onPause() {
        for (ViewPresenter presenter : presenters) {
            presenter.pauseView();
        }
    }

    /**
     * Destroys all the already registered presenters lifecycle.
     */
    public void onDestroy() {
        for (ViewPresenter presenter : presenters) {
            presenter.destroyView();
        }
    }

    /**
     * Registers a presenter instance.
     *
     * @param presenter to be registered
     */
    public void registerPresenter(ViewPresenter presenter) {
        if (presenter == null) {
            throw new IllegalArgumentException("The presenter instance to be registered can't be null");
        }
        presenters.add(presenter);
    }

    /**
     * Updates the view instance associated to all the already registered presenters.
     *
     * @param view to be assigned to the presenters.
     */
    public void setView(Object view) {
        if (view == null) {
            throw new IllegalArgumentException(
                    "The view instance used to configure the presenters can't be null");
        }
        for (ViewPresenter presenter : presenters) {
            presenter.setView(view);
        }
    }

    private void addAnnotatedPresenter(Object source) {
        for (Field field : source.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Presenter.class)) {
                if (Modifier.isPrivate(field.getModifiers())) {
                    throw new NotAccessibleException("presenter on " + field.getName() + " canot be private");
                }
                else {
                    try {
                        field.setAccessible(true);
                        ViewPresenter presenter = (ViewPresenter) field.get(source);
                        registerPresenter(presenter);
                        field.setAccessible(false);
                    }
                    catch (IllegalAccessException e) {
                        NotAccessibleException exception = new NotAccessibleException(field.getName(), e);
                        throw exception;
                    }
                }
            }
        }
    }
}
