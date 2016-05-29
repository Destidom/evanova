package com.tlabs.android.evanova.app.mails.main;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.mails.EveMailUseCase;
import com.tlabs.android.evanova.app.mails.KillMailUseCase;
import com.tlabs.eve.api.mail.MailMessage;
import com.tlabs.eve.api.mail.NotificationMessage;
import com.tlabs.eve.zkb.ZKillMail;

import java.util.Collections;

import javax.inject.Inject;

import rx.Observer;

public class MailPresenter extends EvanovaActivityPresenter<MailView> {

    private final EveMailUseCase messagesUseCase;
    private final KillMailUseCase killMailUseCase;

    private long ownerID = -1;

    @Inject
    public MailPresenter(Context context, EveMailUseCase messagesUseCase, KillMailUseCase killMailUseCase) {
        super(context);
        this.messagesUseCase = messagesUseCase;
        this.killMailUseCase = killMailUseCase;
    }

    @Override
    public void setView(MailView view) {
        super.setView(view);
        setBackgroundDefault();
    }

    @Override
    public void startView(Intent intent) {
        setOwner(ownerOf(intent));
    }

    public void setOwner(final long ownerID) {
        this.ownerID = ownerID;

        if (ownerID > 0) {
            subscribe(() -> this.messagesUseCase.loadMailBoxes(ownerID), b -> getView().setMailBoxes(b));
            subscribe(() -> this.messagesUseCase.loadNotificationBoxes(ownerID), b -> getView().setNotificationBoxes(b));
            subscribe(() -> killMailUseCase.loadKillMails(ownerID), kills -> getView().setKillMails(kills, ownerID));

        } else {
            getView().setMailBoxes(Collections.emptyList());
        }
    }

    public void onKillMailSelected(long mailId) {
        final Observer<ZKillMail> observer = new Observer<ZKillMail>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ZKillMail killMail) {
                getView().showKillMail(killMail, ownerID);
            }
        };
        subscribe(() -> {
            this.killMailUseCase.loadKillMail(mailId, observer);
            return null;
        });
    }

    public void onNotificationBoxSelected(long mailboxId) {
        subscribe(
                () -> this.messagesUseCase.loadNotifications(this.ownerID, mailboxId),
                messages -> getView().showNotifications(mailboxId, messages));
    }

    public void onMailboxSelected(long mailboxId) {
        subscribe(
                () -> this.messagesUseCase.loadMessages(this.ownerID, mailboxId),
                messages -> getView().showMails(mailboxId, messages));
    }

    public void onMessageSelected(final MailMessage message) {
        showLoading(true);
        subscribe(
            () -> {
                final MailMessage m = this.messagesUseCase.loadMessage(ownerID, message.getMessageID());
                if (null != m) {
                    this.messagesUseCase.setMailRead(ownerID, Collections.singletonList(message.getMessageID()), true);
                    m.setRead(true);
                }
                return m;
            },
            m -> {
                showLoading(false);
                getView().showMail(m);
            });
    }

    public void onMessageSelected(final NotificationMessage message) {
        showLoading(true);
        subscribe(
            () -> {
                final NotificationMessage m = this.messagesUseCase.loadNotification(ownerID, message.getMessageID());
                if (null != m) {
                    this.messagesUseCase.setNotificationRead(ownerID, Collections.singletonList(message.getMessageID()), true);
                    m.setRead(true);
                }
                return m;
            },
            m -> {
                showLoading(false);
                getView().showNotification(m);
            });
    }

}
