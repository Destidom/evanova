package com.tlabs.android.evanova.app.corporation.presenter;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.corporation.CorporationUseCase;
import com.tlabs.android.evanova.app.corporation.CorporationView;
import com.tlabs.android.evanova.app.corporation.ui.CorporationActivity;
import com.tlabs.android.jeeves.model.EveCorporation;
import com.tlabs.android.jeeves.views.EveImages;

import javax.inject.Inject;

public class CorporationPresenter extends EvanovaActivityPresenter<CorporationView> {

    private CorporationUseCase useCase;
    private EveCorporation corporation;

    @Inject
    public CorporationPresenter(
            Context context,
            CorporationUseCase useCase) {
        super(context);
        this.useCase = useCase;
    }

    @Override
    public void startView(final Intent intent) {
        final long id = (null == intent) ? -1l : intent.getLongExtra(CorporationActivity.EXTRA_CORP_ID, -1l);
        if (id == -1l) {
            return;
        }
        getView().showLoading(true);
        subscribe(
                () -> this.useCase.loadCorporation(id),
                corporation -> {
                    getView().showLoading(false);
                    getView().showCorporation(corporation);
                    setCorporation(corporation);
                });
    }

    public void onCorporationMenuSelected(final int buttonID) {

    }

    private void setCorporation(final EveCorporation corporation) {
        this.corporation = corporation;
        if (null == corporation) {
            setTitle(R.string.app_name);
            setTitleDescription("");
            setTitleIcon(R.drawable.ic_eve_member);
        }
        else {
            setTitle(corporation.getName());
            setTitleDescription(corporation.getAllianceName());
            setTitleIcon(EveImages.getCorporationIconURL(getContext(), corporation.getID()));
        }
        setBackground(corporation);
    }
}
