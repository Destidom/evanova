package com.tlabs.android.evanova.app.killmail.presenter;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.killmail.KillMailUseCase;
import com.tlabs.android.evanova.app.killmail.KillMailView;
import com.tlabs.android.evanova.mvp.ViewPresenter;
import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.eve.zkb.ZKillMail;

import javax.inject.Inject;

import rx.Observer;

public class KillMailPresenter extends ViewPresenter<KillMailView> {

    private final KillMailUseCase useCase;
    private final EveAccount owner;

    @Inject
    public KillMailPresenter(KillMailUseCase useCase, EveAccount account) {
        this.useCase = useCase;
        this.owner = account;
    }

    @Override
    public void setView(KillMailView view) {
        super.setView(view);
        if (null != this.owner) {
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
                getView().setLoading(false);
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
