package com.tlabs.android.evanova.app.mails.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ViewFlipper;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.mails.DaggerMailComponent;
import com.tlabs.android.evanova.app.mails.MailModule;
import com.tlabs.android.evanova.app.mails.MailView;
import com.tlabs.android.evanova.app.mails.presenter.MailPresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.android.jeeves.views.mails.EveMessageWidget;
import com.tlabs.android.jeeves.views.mails.KillMailAttackersWidget;
import com.tlabs.android.jeeves.views.mails.KillMailDetailsWidget;
import com.tlabs.android.jeeves.views.mails.KillMailItemsWidget;
import com.tlabs.android.jeeves.views.mails.KillMailListWidget;
import com.tlabs.android.jeeves.views.mails.KillMailWidget;
import com.tlabs.android.jeeves.views.mails.MailboxListWidget;
import com.tlabs.android.jeeves.views.mails.MessageListWidget;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.pager.TabPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPagerAdapter;
import com.tlabs.eve.api.mail.MailMessage;
import com.tlabs.eve.api.mail.NotificationMessage;
import com.tlabs.eve.zkb.ZKillMail;

import java.util.List;

import javax.inject.Inject;

public class MailActivity extends BaseActivity implements MailView {

    public static final String EXTRA_OWNER_ID = MailActivity.class.getName() + ".ownerID";

    private static final int FLIPPER_MAIN = 0;
    private static final int FLIPPER_MAILS = 1;
    private static final int FLIPPER_NOTIFICATIONS = 2;
    private static final int FLIPPER_KILLMAILS = 3;
    private static final int FLIPPER_MAIL_MESSAGE = 4;
    private static final int FLIPPER_NOTIFICATION_MESSAGE = 5;

    private static class KillMailAdapter extends ViewPagerAdapter {
        public KillMailAdapter(Context context) {
            super(context);
            addView(new KillMailDetailsWidget(getContext()), R.string.mails_killmails_pager_details);
            addView(new KillMailAttackersWidget(getContext()), R.string.mails_killmails_pager_attackers);
            addView(new KillMailItemsWidget(getContext()), R.string.mails_killmails_pager_items);
        }

        public void setKillMail(final ZKillMail killMail) {
            for (View v: getViews()) {
                ((KillMailWidget)v).setKillMail(killMail);
            }
        }
    }

    private static class MailAdapter extends ViewPagerAdapter {
        public MailAdapter(Context context) {
            super(context);

            final MailboxListWidget wMails = new MailboxListWidget(getContext());
            wMails.setListener(new AbstractListRecyclerView.Listener<MailFacade.Mailbox>() {
                @Override
                public void onItemClicked(MailFacade.Mailbox mailbox) {
                    onMailboxSelected(mailbox.getId());
                }

                @Override
                public void onItemSelected(MailFacade.Mailbox mailbox, boolean selected) {
                    onMailboxSelected(mailbox.getId());
                }

                @Override
                public void onItemMoved(MailFacade.Mailbox mailbox, int from, int to) {

                }
            });
            addView(wMails, R.string.mails_pager_messages);

            final MailboxListWidget wNotifications = new MailboxListWidget(getContext());
            wNotifications.setListener(new AbstractListRecyclerView.Listener<MailFacade.Mailbox>() {
                @Override
                public void onItemClicked(MailFacade.Mailbox mailbox) {
                    onNotificationBoxSelected(mailbox.getId());
                }

                @Override
                public void onItemSelected(MailFacade.Mailbox mailbox, boolean selected) {
                    onNotificationBoxSelected(mailbox.getId());
                }

                @Override
                public void onItemMoved(MailFacade.Mailbox mailbox, int from, int to) {

                }
            });
            addView(wNotifications, R.string.mails_pager_notifications);

            final KillMailListWidget wKillMails = new KillMailListWidget(getContext());
            wKillMails.setListener(new AbstractListRecyclerView.Listener<ZKillMail>() {
                @Override
                public void onItemClicked(ZKillMail killMail) {
                    onKillMailSelected(killMail.getKillID());
                }

                @Override
                public void onItemSelected(ZKillMail killMail, boolean selected) {
                    onKillMailSelected(killMail.getKillID());
                }

                @Override
                public void onItemMoved(ZKillMail killMail, int from, int to) {

                }
            });
            addView(wKillMails, R.string.mails_pager_killmails);
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


        protected void onMailboxSelected(final long mailboxId) {}

        protected void onNotificationBoxSelected(final long mailboxId) {}

        protected void onKillMailSelected(final long mailId) {}
    }

    @Inject
    @Presenter
    MailPresenter mailPresenter;

    private TabPager pager;
    private ViewFlipper flipper;

    private MessageListWidget<MailMessage> wMails;
    private MessageListWidget<NotificationMessage> wNotifications;
    private EveMessageWidget wMailMessage;
    private EveMessageWidget wNotificationMessage;

    private TabPager killMailPager;

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
        this.pager.setAdapter(new MailAdapter(this) {
            @Override
            protected void onKillMailSelected(long mailId) {
                mailPresenter.onKillMailSelected(mailId);
            }

            @Override
            protected void onNotificationBoxSelected(long mailboxId) {
                mailPresenter.onNotificationBoxSelected(mailboxId);
            }

            @Override
            protected void onMailboxSelected(long mailboxId) {
                mailPresenter.onMailboxSelected(mailboxId);
            }
        });

        this.flipper = new ViewFlipper(this);
        this.flipper.addView(this.pager);

        this.wMails = new MessageListWidget(this);
        this.wMails.setListener(new AbstractListRecyclerView.Listener<MailMessage>() {
            @Override
            public void onItemClicked(MailMessage mailMessage) {
                mailPresenter.onMessageSelected(mailMessage);
            }

            @Override
            public void onItemSelected(MailMessage mailMessage, boolean selected) {
                mailPresenter.onMessageSelected(mailMessage);
            }

            @Override
            public void onItemMoved(MailMessage mailMessage, int from, int to) {

            }
        });

        this.flipper.addView(this.wMails);

        this.wNotifications = new MessageListWidget(this);
        this.wNotifications.setListener(new AbstractListRecyclerView.Listener<NotificationMessage>() {
            @Override
            public void onItemClicked(NotificationMessage mailMessage) {
                mailPresenter.onMessageSelected(mailMessage);
            }

            @Override
            public void onItemSelected(NotificationMessage mailMessage, boolean selected) {
                mailPresenter.onMessageSelected(mailMessage);
            }

            @Override
            public void onItemMoved(NotificationMessage mailMessage, int from, int to) {

            }
        });

        this.flipper.addView(this.wNotifications);

        this.killMailPager = new TabPager(this);
        this.killMailPager.setAdapter(new KillMailAdapter(this));
        this.flipper.addView(this.killMailPager);

        this.wMailMessage = new EveMessageWidget(this);

        this.flipper.addView(this.wMailMessage);

        this.wNotificationMessage = new EveMessageWidget(this);
        this.flipper.addView(this.wNotificationMessage);

        setView(this.flipper);
    }

    @Override
    public void onBackPressed() {
        switch (this.flipper.getDisplayedChild()) {
            case FLIPPER_KILLMAILS:
            case FLIPPER_MAILS:
            case FLIPPER_NOTIFICATIONS:
                this.flipper.setDisplayedChild(FLIPPER_MAIN);
                break;
            case FLIPPER_MAIL_MESSAGE:
                this.flipper.setDisplayedChild(FLIPPER_MAILS);
                break;
            case FLIPPER_NOTIFICATION_MESSAGE:
                this.flipper.setDisplayedChild(FLIPPER_NOTIFICATIONS);
                break;
            case FLIPPER_MAIN:
            default:
                super.onBackPressed();
                break;
        }
    }

    @Override
    public void setKillMails(List<ZKillMail> killMails, long ownerID) {
        final MailAdapter adapter = pager.getAdapter();
        adapter.setKillMails(killMails);
    }

    @Override
    public void showKillMail(ZKillMail killMail, long ownerID) {
        final KillMailAdapter adapter = killMailPager.getAdapter();
        adapter.setKillMail(killMail);
        this.flipper.setDisplayedChild(FLIPPER_KILLMAILS);
    }

    @Override
    public void setMailBoxes(List<MailFacade.Mailbox> mailboxes) {
        final MailAdapter adapter = pager.getAdapter();
        adapter.setMailboxes(mailboxes);
    }

    @Override
    public void showMails(long mailboxID, List<MailMessage> messages) {
        this.wMails.setItems(messages);
        this.flipper.setDisplayedChild(FLIPPER_MAILS);
    }

    @Override
    public void showMail(MailMessage message) {
        this.wMailMessage.setMessage(message);
        this.flipper.setDisplayedChild(FLIPPER_MAIL_MESSAGE);
    }

    @Override
    public void setNotificationBoxes(List<MailFacade.Mailbox> mailboxes) {
        final MailAdapter adapter = pager.getAdapter();
        adapter.setNotificationBoxes(mailboxes);
    }

    @Override
    public void showNotifications(long mailboxID, List<NotificationMessage> messages) {
       this.wNotifications.setItems(messages);
        this.flipper.setDisplayedChild(FLIPPER_NOTIFICATIONS);
    }

    @Override
    public void showNotification(NotificationMessage message) {
        this.wNotificationMessage.setMessage(message);
        this.flipper.setDisplayedChild(FLIPPER_NOTIFICATION_MESSAGE);
    }
}
