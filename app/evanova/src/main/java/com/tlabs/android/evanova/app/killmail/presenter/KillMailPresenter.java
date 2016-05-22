package com.tlabs.android.evanova.app.killmail.presenter;

import android.content.Context;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.killmail.KillMailUseCase;
import com.tlabs.android.evanova.app.killmail.KillMailView;
import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.eve.zkb.ZKillMail;

import javax.inject.Inject;

import rx.Observer;

public class KillMailPresenter extends EvanovaActivityPresenter<KillMailView> {

    private final KillMailUseCase useCase;
    private final EveAccount owner;

    @Inject
    public KillMailPresenter(
            Context context,
            KillMailUseCase useCase,
            EveAccount account) {
        super(context);

        this.useCase = useCase;
        this.owner = account;
    }

    @Override
    public void setView(KillMailView view) {
        super.setView(view);

        if (null != this.owner) {
            setBackground(EveImages.getCharacterIconURL(getContext(), this.owner.getId()));
            subscribe(
                () -> useCase.listKillMails(this.owner.getOwnerId()),
                kills -> view.showKillMails(kills, this.owner.getOwnerId()));
        }
    }

    public void onKillMailSelected(final long killMailID) {
        if (null == this.owner) {
            return;
        }

        this.useCase.loadKillMail(killMailID, new Observer<ZKillMail>() {
            @Override
            public void onCompleted() {
                getView().showLoading(false);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ZKillMail zKillMail) {
                if (null == zKillMail) {
                    getView().showError(R.string.toast_killmail_error);
                }
                else {
                    getView().showKillMail(zKillMail, owner.getOwnerId());
                }
            }
        });
    }
}
