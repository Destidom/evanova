package com.tlabs.android.evanova.app.skills.presenter;

import android.content.Context;

import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.skills.SkillDatabaseView;

import javax.inject.Inject;

public class SkillDatabasePresenter extends EvanovaActivityPresenter<SkillDatabaseView> {

    @Inject
    public SkillDatabasePresenter(Context context) {
        super(context);
    }

    @Override
    public void setView(SkillDatabaseView view) {
        super.setView(view);
        setBackgroundDefault();
    }
}
