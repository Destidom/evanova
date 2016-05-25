package com.tlabs.android.evanova.app.corporations.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tlabs.android.evanova.app.corporations.presenter.CorporationPresenter;
import com.tlabs.android.evanova.mvp.BaseFragment;
import com.tlabs.android.jeeves.model.EveCorporation;

abstract class CorporationFragment extends BaseFragment {

    CorporationPresenter presenter;
    EveCorporation corporation;

    public void setPresenter(CorporationPresenter presenter) {
        this.presenter = presenter;
    }

    public final void setCorporation(final EveCorporation corporation) {
        this.corporation = corporation;
        if ((null != corporation) && (null != getView())) {
            onCorporationChanged(corporation);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onCorporationChanged(corporation);
    }

    protected void onCorporationChanged(final EveCorporation corporation) {}
}
