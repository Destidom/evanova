package com.tlabs.android.evanova.app.fittings.presenter;

import com.tlabs.android.evanova.app.fittings.FittingListView;
import com.tlabs.android.evanova.mvp.ViewPresenter;
import com.tlabs.android.evanova.app.fittings.FittingUseCase;

import javax.inject.Inject;

public class FittingListPresenter extends ViewPresenter<FittingListView> {

    private final FittingUseCase useCase;

    @Inject
    public FittingListPresenter(FittingUseCase useCase) {
        this.useCase = useCase;
    }

    public void loadFittings() {
       // getView().showLoading(true);
        subscribe(() -> this.useCase.loadFittings(), fittings -> {
      //      getView().showLoading(false);
            getView().setFittings(fittings);
        });
    }

}
