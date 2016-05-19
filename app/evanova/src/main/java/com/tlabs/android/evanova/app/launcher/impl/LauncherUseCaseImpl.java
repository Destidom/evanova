package com.tlabs.android.evanova.app.launcher.impl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tlabs.android.evanova.BuildConfig;
import com.tlabs.android.evanova.app.launcher.LauncherUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LauncherUseCaseImpl implements LauncherUseCase {

    private static final List<Operation> operations;
    static {
        operations = new ArrayList<>();
        operations.add(new ClearCacheOperation());
        operations.add(new StorageInitOperation());
        operations.add(new DatabaseInitOperation());
        operations.add(new DatabaseDumpOperation());
        operations.add(new PreferencesOperation());
        operations.add(new WakeUpAlarmOperation());
    }

    private final Context context;
    private final boolean firstLaunch;

    @Inject
    public LauncherUseCaseImpl(Context context) {
        this.context = context.getApplicationContext();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        final int previousVersion = preferences.getInt("evanova.build.version.int", 0);

        this.firstLaunch = BuildConfig.VERSION_CODE != previousVersion;
        if (firstLaunch) {
            preferences.
                    edit().
                    putInt("evanova.build.version.int", BuildConfig.VERSION_CODE).
                    apply();
        }
    }

    @Override
    public void runOperations(Observer<LauncherUseCase.Operation> observer) {
        Observable
            .defer(() -> Observable.from(operations))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(operation -> {
                if (!operation.runOnce() || operation.runOnce() && firstLaunch) {
                    operation.execute(context);
                }
            }).
            subscribe(observer);
    }

    @Override
    public Intent nextIntent(Intent intent) {
        return null;
    }
}
