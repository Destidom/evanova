package com.tlabs.android.evanova.app.corporation.ui;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.corporation.CorporationListView;
import com.tlabs.android.evanova.app.corporation.CorporationModule;
import com.tlabs.android.evanova.app.corporation.DaggerCorporationComponent;
import com.tlabs.android.evanova.app.corporation.presenter.CorporationListPresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.jeeves.model.EveCorporation;

import java.util.List;

import javax.inject.Inject;

public class CorporationListActivity extends BaseActivity implements CorporationListView {

    @Inject
    CorporationListPresenter presenter;

    private CorporationListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCorporationComponent
                .builder()
                .evanovaComponent(Application.getEveComponent())
                .corporationModule(new CorporationModule())
                .build()
                .inject(this);

        this.fragment = new CorporationListFragment();
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

    @Override
    public void showCorporations(List<EveCorporation> corporations) {
        this.fragment.setCorporations(corporations);
    }
}
