package com.tlabs.android.evanova.app.mails.impl;

import com.tlabs.android.evanova.app.mails.EveMailUseCase;
import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.eve.api.mail.MailMessage;
import com.tlabs.eve.api.mail.NotificationMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

public class EveMailUseCaseImpl implements EveMailUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(EveMailUseCaseImpl.class);

    private final MailFacade mail;

    @Inject
    public EveMailUseCaseImpl(final MailFacade mail) {
        this.mail = mail;
    }

    @Override
    public List<MailFacade.Mailbox> loadMailBoxes(final long ownerID) {
        return mail.getMailBoxes(ownerID);
    }

    @Override
    public MailMessage loadMessage(final long messageID) {
        return mail.getMail(messageID);
    }

    @Override
    public List<MailMessage> loadMessages(final long ownerID, final long mailboxID) {
        return mail.getMails(ownerID, mailboxID);
    }

    @Override
    public List<MailFacade.Mailbox> loadNotificationBoxes(final long ownerID) {
        return mail.getNotificationBoxes(ownerID);
    }

    @Override
    public NotificationMessage loadNotification(final long messageID) {
        return mail.getNotification(messageID);
    }

    @Override
    public List<NotificationMessage> loadNotifications(final long ownerID, final long mailboxID) {
        return mail.getNotifications(ownerID, mailboxID);
    }
}
