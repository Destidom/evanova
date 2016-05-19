package com.tlabs.android.evanova.app.killmail.impl;

import android.content.Context;

import com.tlabs.android.evanova.app.killmail.KillMailUseCase;
import com.tlabs.android.evanova.content.ContentFacade;
import com.tlabs.android.jeeves.model.data.sde.EveFacade;
import com.tlabs.eve.zkb.ZKillMail;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;

public class KillMailUseCaseImpl implements KillMailUseCase {

    private final ContentFacade content;
    private final KillMailLoader loader;

    @Inject
    public KillMailUseCaseImpl(
            final Context context,
            final ContentFacade content,
            final EveFacade eve) {
        this.content = content;
        this.loader = new KillMailLoader(context, eve);
    }

    @Override
    public List<ZKillMail> listKillMails(long ownerID) {
        return content.getKillMails(ownerID);
    }

    @Override
    public void loadKillMail(long killMailID, Observer<ZKillMail> observer) {
        loader.load(killMailID, km -> {
            observer.onNext(km);
            observer.onCompleted();
        });
    }
}
