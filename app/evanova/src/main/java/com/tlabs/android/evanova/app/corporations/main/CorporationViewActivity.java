package com.tlabs.android.evanova.app.corporations.main;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.corporations.CorporationComponent;
import com.tlabs.android.evanova.app.corporations.CorporationModule;
import com.tlabs.android.evanova.app.corporations.DaggerCorporationComponent;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.BaseFragment;
import com.tlabs.android.jeeves.model.EveCorporation;

public class CorporationViewActivity extends BaseActivity implements CorporationView {

    private CorporationViewFragment viewFragment;
    private CorporationInfoFragment detailsFragment;

    private CorporationComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.component =
                DaggerCorporationComponent
                        .builder()
                        .applicationComponent(Application.getAppComponent())
                        .corporationModule(new CorporationModule())
                        .build();

        this.viewFragment = CorporationViewFragment.newInstance(
                getIntent().getLongExtra(CorporationPresenter.EXTRA_OWNER_ID, -1));
        this.viewFragment.setRetainInstance(true);

        this.detailsFragment = CorporationInfoFragment.newInstance(
                getIntent().getLongExtra(CorporationPresenter.EXTRA_OWNER_ID, -1));

        this.detailsFragment.setRetainInstance(true);

        stackFragment(this.viewFragment);
    }

    @Override
    protected void inject(BaseFragment fragment) {
        component.inject((CorporationViewFragment)fragment);
    }

    @Override
    public void setCorporation(EveCorporation corporation) {
        this.viewFragment.setCorporation(corporation);
        this.detailsFragment.setCorporation(corporation);
    }

    @Override
    public void showCorporationInformation() {
        stackFragment(this.viewFragment);
    }
}
