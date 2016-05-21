package com.tlabs.android.evanova.app.skills;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.skills.presenter.SkillDatabasePresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;

import javax.inject.Inject;

public class SkillDatabaseActivity extends BaseActivity implements SkillDatabaseView {

    @Inject
    SkillDatabasePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerSkillDatabaseComponent.builder()
                .evanovaComponent(Application.getEveComponent())
                .skillDatabaseModule(new SkillDatabaseModule())
                .build()
                .inject(this);

        this.presenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.destroyView();
        this.presenter = null;
    }

}
