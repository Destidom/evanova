package com.tlabs.android.evanova.app.corporations.list;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.corporations.CorporationUseCase;
import com.tlabs.android.evanova.app.corporations.main.CorporationViewActivity;

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
        final Intent intent = new Intent(getContext(), CorporationViewActivity.class);
        intent.putExtra(EXTRA_OWNER_ID, corporationID);
        startActivity(intent);
    }
}
