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
    public static final String EXTRA_CORP_ID = CorporationActivity.class.getSimpleName() + ".corpID";

    @Inject
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
        this.presenter.setView(this);
        this.presenter.startWithIntent(getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.destroyView();
        this.presenter = null;
    }

    @Override
    public void showCorporation(EveCorporation corporation) {
        this.fragment.setCorporation(corporation);
    }
}
