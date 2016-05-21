package com.tlabs.android.evanova.app.corporation.presenter;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.corporation.CorporationListView;
import com.tlabs.android.evanova.app.corporation.CorporationUseCase;
import com.tlabs.android.evanova.app.corporation.ui.CorporationActivity;

import javax.inject.Inject;

public class CorporationListPresenter extends EvanovaActivityPresenter<CorporationListView> {

    private CorporationUseCase useCase;

    @Inject
    public CorporationListPresenter(
            Context context,
            CorporationUseCase useCase) {
        super(context);
        this.useCase = useCase;
    }

    @Override
    public void setView(CorporationListView view) {
        super.setView(view);
        setBackgroundDefault();
        showLoading(true);

        subscribe(() -> useCase.loadCorporations(), corporations -> {
            showLoading(false);
            getView().showCorporations(corporations);
        });
    }

    public void onCorporationSelected(final long corporationID) {
        final Intent intent = new Intent(getContext(), CorporationActivity.class);
        intent.putExtra(CorporationActivity.EXTRA_CORP_ID, corporationID);
        startActivity(intent);
    }
}
