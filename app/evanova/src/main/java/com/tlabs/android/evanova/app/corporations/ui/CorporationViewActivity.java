package com.tlabs.android.evanova.app.corporations.ui;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.corporations.CorporationModule;
import com.tlabs.android.evanova.app.corporations.CorporationView;
import com.tlabs.android.evanova.app.corporations.DaggerCorporationComponent;
import com.tlabs.android.evanova.app.corporations.presenter.CorporationPresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.model.EveCorporation;

import javax.inject.Inject;

public class CorporationViewActivity extends BaseActivity implements CorporationView {
    public static final String EXTRA_CORP_ID = CorporationViewActivity.class.getSimpleName() + ".corpID";

    @Inject
    @Presenter
    CorporationPresenter presenter;

    private CorporationViewFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCorporationComponent
                .builder()
                .applicationComponent(Application.getAppComponent())
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
