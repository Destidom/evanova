package com.tlabs.android.evanova.app.fittings.presenter;

import com.tlabs.android.evanova.app.fittings.FittingView;
import com.tlabs.android.evanova.mvp.ViewPresenter;
import com.tlabs.android.evanova.app.fittings.FittingUseCase;
import com.tlabs.eve.dogma.Fitter;

import javax.inject.Inject;


public class FittingPresenter extends ViewPresenter<FittingView> {

    private final FittingUseCase useCase;

    @Inject
    public FittingPresenter(FittingUseCase useCase) {
        this.useCase = useCase;
    }

    public void setFitting(Fitter fitting) {
        getView().setFitting(fitting);
    }
}
