package com.tlabs.android.evanova.app.mails;

import com.tlabs.android.evanova.app.mails.EveMailUseCase;
import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.eve.EveNetwork;
import com.tlabs.eve.api.mail.MailBodiesRequest;
import com.tlabs.eve.api.mail.MailBodiesResponse;
import com.tlabs.eve.api.mail.MailMessage;
import com.tlabs.eve.api.mail.NotificationMessage;
import com.tlabs.eve.api.mail.NotificationTextRequest;
import com.tlabs.eve.api.mail.NotificationTextResponse;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.inject.Inject;

public class EveMailUseCaseImpl implements EveMailUseCase {

    private final MailFacade mail;
    private final EveNetwork api;

    @Inject
    public EveMailUseCaseImpl(
            final MailFacade mail,
            final EveNetwork api) {
        this.mail = mail;
        this.api = api;
    }

    @Override
    public List<MailFacade.Mailbox> loadMailBoxes(final long ownerID) {
        return mail.getMailBoxes(ownerID);
    }

    @Override
    public MailMessage loadMessage(final long ownerID, final long messageID) {
        final MailMessage message = mail.getMail(messageID);
        if (null == message) {
            return null;
        }

        if (StringUtils.isNotBlank(message.getBody())) {
            return message;
        }

        final MailBodiesRequest request = new MailBodiesRequest(Long.toString(ownerID), new long[]{messageID});
        final MailBodiesResponse response = api.execute(request);
        if (response.hasError() || response.getMessages().isEmpty()) {
            return message;
        }

        final MailMessage body = response.getMessages().get(0);
        message.setBody(body.getBody());
        this.mail.updateMail(ownerID, message);
        return message;
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
    public NotificationMessage loadNotification(final long ownerID, final long messageID) {
        final NotificationMessage message = mail.getNotification(messageID);
        if (null == message) {
            return null;
        }

        if (StringUtils.isNotBlank(message.getBody())) {
            return message;
        }

        final NotificationTextRequest request = new NotificationTextRequest(Long.toString(ownerID), new long[]{messageID});
        final NotificationTextResponse response = api.execute(request);
        if (response.hasError() || response.getMessages().isEmpty()) {
            return message;
        }

        final NotificationMessage body = response.getMessages().get(0);
        message.setBody(body.getBody());
        this.mail.updateNotification(ownerID, message);
        return message;
    }

    @Override
    public List<NotificationMessage> loadNotifications(final long ownerID, final long mailboxID) {
        return mail.getNotifications(ownerID, mailboxID);
    }

    @Override
    public void setMailRead(long ownerID, List<Long> messages, boolean read) {
        this.mail.setRead(messages, read);
    }

    @Override
    public void setNotificationRead(long ownerID, List<Long> messages, boolean read) {
        this.mail.setRead(messages, read);
    }
}
