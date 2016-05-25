package com.tlabs.android.evanova.app.mails.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.mails.DaggerMailComponent;
import com.tlabs.android.evanova.app.mails.MailModule;
import com.tlabs.android.evanova.app.mails.MailView;
import com.tlabs.android.evanova.app.mails.presenter.KillMailPresenter;
import com.tlabs.android.evanova.app.mails.presenter.MailPresenter;
import com.tlabs.android.evanova.app.mails.presenter.NotificationPresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.android.jeeves.views.mails.KillMailListWidget;
import com.tlabs.android.jeeves.views.mails.MailboxListWidget;
import com.tlabs.android.jeeves.views.ui.pager.TabPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPagerAdapter;
import com.tlabs.eve.api.mail.MailMessage;
import com.tlabs.eve.api.mail.NotificationMessage;
import com.tlabs.eve.zkb.ZKillMail;

import java.util.List;

import javax.inject.Inject;

public class MailActivity extends BaseActivity implements MailView {

    public static final String EXTRA_OWNER_ID = MailActivity.class.getName() + ".ownerID";

    private static class MailAdapter extends ViewPagerAdapter {
        public MailAdapter(Context context) {
            super(context);
            addView(new MailboxListWidget(getContext()), R.string.skills_pager_certificates);
            addView(new MailboxListWidget(getContext()), R.string.skills_pager_certificates);
            addView(new KillMailListWidget(getContext()), R.string.skills_pager_certificates);
        }

        public void setKillMails(final List<ZKillMail> killMails) {
            final KillMailListWidget widget = getView(2);
            widget.setItems(killMails);
        }

        public void setMailboxes(final List<MailFacade.Mailbox> mailboxes) {
            final MailboxListWidget widget = getView(0);
            widget.setItems(mailboxes);
        }

        public void setNotificationBoxes(final List<MailFacade.Mailbox> mailboxes) {
            final MailboxListWidget widget = getView(1);
            widget.setItems(mailboxes);
        }
    }

    @Inject
    @Presenter
    KillMailPresenter killMailPresenter;

    @Inject
    @Presenter
    MailPresenter mailPresenter;

    @Inject
    @Presenter
    NotificationPresenter notificationPresenter;


    private TabPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerMailComponent
                .builder()
                .applicationComponent(Application.getAppComponent())
                .mailModule(new MailModule())
                .build()
                .inject(this);

        this.pager = new TabPager(this);
        this.pager.setAdapter(new MailAdapter(this));

        final FrameLayout frame = (FrameLayout)findViewById(R.id.activity_container);
        frame.addView(this.pager);
    }

    @Override
    public void showKillMails(List<ZKillMail> killMails, long ownerID) {
        final MailAdapter adapter = pager.getAdapter();
        adapter.setKillMails(killMails);
    }

    @Override
    public void showKillMail(ZKillMail killMail, long ownerID) {

    }

    @Override
    public void showMailBoxes(List<MailFacade.Mailbox> mailboxes) {
        final MailAdapter adapter = pager.getAdapter();
        adapter.setMailboxes(mailboxes);
    }

    @Override
    public void showMails(long mailboxID, List<MailMessage> messages) {

    }

    @Override
    public void showMail(MailMessage message) {

    }

    @Override
    public void showNotificationBoxes(List<MailFacade.Mailbox> mailboxes) {
        final MailAdapter adapter = pager.getAdapter();
        adapter.setNotificationBoxes(mailboxes);
    }

    @Override
    public void showNotifications(long mailboxID, List<NotificationMessage> messages) {

    }

    @Override
    public void showNotification(NotificationMessage message) {

    }
}
