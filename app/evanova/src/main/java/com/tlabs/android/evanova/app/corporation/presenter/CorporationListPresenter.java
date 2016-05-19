package com.tlabs.android.evanova.app.corporation.presenter;

import com.tlabs.android.evanova.app.corporation.CorporationListView;
import com.tlabs.android.evanova.app.corporation.CorporationUseCase;
import com.tlabs.android.evanova.mvp.ViewPresenter;

import javax.inject.Inject;

public class CorporationListPresenter extends ViewPresenter<CorporationListView> {

    private CorporationUseCase useCase;

    @Inject
    public CorporationListPresenter(CorporationUseCase useCase) {
        this.useCase = useCase;
    }
}
