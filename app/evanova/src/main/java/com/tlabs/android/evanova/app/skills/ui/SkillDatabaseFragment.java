package com.tlabs.android.evanova.app.skills.ui;

import com.tlabs.android.evanova.app.skills.presenter.SkillDatabasePresenter;
import com.tlabs.android.evanova.mvp.BaseFragment;

public class SkillDatabaseFragment extends BaseFragment {

    private SkillDatabasePresenter presenter;

    public void setPresenter(SkillDatabasePresenter presenter) {
        this.presenter = presenter;
    }
}
