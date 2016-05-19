package com.tlabs.android.evanova.app.corporation.ui;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.corporation.CorporationModule;
import com.tlabs.android.evanova.app.corporation.CorporationView;
import com.tlabs.android.evanova.app.corporation.DaggerCorporationComponent;
import com.tlabs.android.evanova.app.corporation.presenter.CorporationPresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.jeeves.model.EveCorporation;

import javax.inject.Inject;

public class CorporationActivity extends BaseActivity implements CorporationView {

    @Inject
    CorporationPresenter presenter;

    private CorporationFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCorporationComponent
                .builder()
                .evanovaComponent(Application.getEveComponent())
                .corporationModule(new CorporationModule())
                .build()
                .inject(this);

        this.fragment = new CorporationFragment();
        setFragment(this.fragment);
        this.presenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.destroyView();
        this.presenter = null;
    }

    @Override
    public void showCorporation(EveCorporation corporation) {

    }
}
