package com.tlabs.android.evanova.app.skills.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.app.skills.presenter.SkillDatabasePresenter;
import com.tlabs.android.evanova.mvp.BaseFragment;

public class SkillDatabaseFragment extends BaseFragment {

    private SkillDatabasePresenter presenter;

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    public void setPresenter(SkillDatabasePresenter presenter) {
        this.presenter = presenter;
    }
}
