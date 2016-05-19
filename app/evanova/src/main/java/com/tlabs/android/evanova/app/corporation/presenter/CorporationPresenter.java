package com.tlabs.android.evanova.app.corporation.presenter;

import com.tlabs.android.evanova.app.corporation.CorporationUseCase;
import com.tlabs.android.evanova.app.corporation.CorporationView;
import com.tlabs.android.evanova.mvp.ViewPresenter;

import javax.inject.Inject;

public class CorporationPresenter extends ViewPresenter<CorporationView> {

    private CorporationUseCase useCase;

    @Inject
    public CorporationPresenter(CorporationUseCase useCase) {
        this.useCase = useCase;
    }
}
