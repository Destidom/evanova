package com.tlabs.android.evanova.app.wallet.main;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.wallet.WalletUseCase;

import javax.inject.Inject;

public class WalletPresenter extends EvanovaActivityPresenter<WalletView> {

    private final WalletUseCase useCase;

    @Inject
    public WalletPresenter(Context context, WalletUseCase useCase) {
        super(context);
        this.useCase = useCase;
    }

    @Override
    public void startView(Intent intent) {
        subscribe(() -> useCase.getTransactions(ownerOf(intent), 1000), transactions -> getView().setWalletTransactions(transactions));
        subscribe(() -> useCase.getJournal(ownerOf(intent), 1000), journal -> getView().setWalletJournal(journal));
    }
}
