package com.tlabs.android.evanova.app.corporations.main;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.contracts.main.ContractActivity;
import com.tlabs.android.evanova.app.corporations.CorporationUseCase;
import com.tlabs.android.evanova.app.market.main.MarketOrdersActivity;
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
    public void startView(Intent intent) {
        setCorporation(ownerOf(intent));
    }

    public void setCorporation(final long corpID) {
        if (corpID <= 0) {
            return;
        }
        showLoading(true);
        subscribe(
            () -> {
                final EveCorporation loaded = this.useCase.loadCorporation(corpID);
                this.corporation = loaded;
                return loaded;
            },
            corporation -> {
                getView().setCorporation(corporation);
                showLoading(false);
                showCorporationImpl(corporation);
            });
    }

    public void onCorporationMenuSelected(final int buttonID) {
        if (null == this.corporation) {
            return;
        }

        switch(buttonID) {
            case R.id.menuCorporationButton: {
                getView().showCorporationInformation();
                break;
            }
            case R.id.menuMarketButton: {
                final Intent intent = new Intent(getContext(), MarketOrdersActivity.class);
                intent.putExtra(EXTRA_OWNER_ID, this.corporation.getID());
                startActivity(intent);
                break;
            }
            case R.id.menuContractsButton: {
                final Intent intent = new Intent(getContext(), ContractActivity.class);
                intent.putExtra(EXTRA_OWNER_ID, this.corporation.getID());
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }

    private void showCorporationImpl(final EveCorporation corporation) {
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
