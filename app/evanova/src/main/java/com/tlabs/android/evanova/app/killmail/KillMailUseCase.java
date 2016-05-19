package com.tlabs.android.evanova.app.killmail;

import com.tlabs.eve.zkb.ZKillMail;

import java.util.List;

import rx.Observer;

public interface KillMailUseCase {

    List<ZKillMail> listKillMails(final long ownerID);

    void loadKillMail(long killMailID, Observer<ZKillMail> observer);

}
