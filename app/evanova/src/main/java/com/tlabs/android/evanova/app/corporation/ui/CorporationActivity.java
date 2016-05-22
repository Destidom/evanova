package com.tlabs.android.evanova.app.corporation.ui;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.corporation.CorporationModule;
import com.tlabs.android.evanova.app.corporation.CorporationView;
import com.tlabs.android.evanova.app.corporation.DaggerCorporationComponent;
import com.tlabs.android.evanova.app.corporation.presenter.CorporationPresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.model.EveCorporation;

import javax.inject.Inject;

public class CorporationActivity extends BaseActivity implements CorporationView {
    public static final String EXTRA_CORP_ID = CorporationActivity.class.getSimpleName() + ".corpID";

    @Inject
    @Presenter
    CorporationPresenter presenter;

    private CorporationViewFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCorporationComponent
                .builder()
                .evanovaComponent(Application.getEveComponent())
                .corporationModule(new CorporationModule())
                .build()
                .inject(this);

        this.fragment = new CorporationViewFragment();
        this.fragment.setPresenter(this.presenter);

        setFragment(this.fragment);
    }

    @Override
    public void showCorporation(EveCorporation corporation) {
        this.fragment.setCorporation(corporation);
    }
}
