package com.tlabs.android.evanova.app.killmail;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.eve.zkb.ZKillMail;

import java.util.List;

public interface KillMailView extends ActivityView {

    void showKillMails(final List<ZKillMail> killMails, final long ownerID);

    void showKillMail(final ZKillMail killMail, final long ownerID);
}
