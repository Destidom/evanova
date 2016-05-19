package com.tlabs.android.evanova.app.launcher.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.launcher.DaggerLauncherComponent;
import com.tlabs.android.evanova.app.launcher.LauncherModule;
import com.tlabs.android.evanova.app.launcher.LauncherView;
import com.tlabs.android.evanova.app.launcher.presenter.LauncherPresenter;

import javax.inject.Inject;

public final class LauncherActivity extends AppCompatActivity implements LauncherView {

    @Inject
    LauncherPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerLauncherComponent
                .builder()
                .applicationComponent(Application.getAppComponent())
                .launcherModule(new LauncherModule())
                .build()
                .inject(this);

        setContentView(R.layout.launcher);

        this.presenter.setView(this);
        if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
            this.presenter.startUri(this);
        }
        else {
            this.presenter.startLaunch(this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.destroyView();
        this.presenter = null;
    }

    //FIX for Android M
    //https://code.google.com/p/android-developer-preview/issues/detail?id=2353
    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setVisible(true);
        }
    }

}
