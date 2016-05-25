package com.tlabs.android.evanova.app.skills.ui;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.skills.DaggerSkillDatabaseComponent;
import com.tlabs.android.evanova.app.skills.SkillDatabaseModule;
import com.tlabs.android.evanova.app.skills.SkillDatabaseView;
import com.tlabs.android.evanova.app.skills.presenter.SkillDatabasePresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;

import javax.inject.Inject;

public class SkillDatabaseActivity extends BaseActivity implements SkillDatabaseView {

    @Inject
    SkillDatabasePresenter presenter;

    private SkillDatabaseFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerSkillDatabaseComponent.builder()
                .applicationComponent(Application.getAppComponent())
                .skillDatabaseModule(new SkillDatabaseModule())
                .build()
                .inject(this);

        this.fragment = new SkillDatabaseFragment();
        this.fragment.setPresenter(this.presenter);
        setFragment(this.fragment);

        this.presenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.destroyView();
        this.presenter = null;
    }

}
