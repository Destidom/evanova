package com.tlabs.android.evanova.app.mails;

import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.eve.api.mail.MailMessage;
import com.tlabs.eve.api.mail.NotificationMessage;

import java.util.List;

public interface EveMailUseCase {

    List<MailFacade.Mailbox> loadMailBoxes(final long ownerID);

    MailMessage loadMessage(final long ownerID, final long messageID);

    List<MailMessage> loadMessages(final long ownerID, final long mailboxID);

    List<MailFacade.Mailbox> loadNotificationBoxes(final long ownerID);

    NotificationMessage loadNotification(final long ownerID, final long messageID);

    List<NotificationMessage> loadNotifications(final long ownerID, final long mailboxID);

    void setMailRead(final long ownerID, final List<Long> messages, final boolean read);

    void setNotificationRead(final long ownerID, final List<Long> messages, final boolean read);
}
