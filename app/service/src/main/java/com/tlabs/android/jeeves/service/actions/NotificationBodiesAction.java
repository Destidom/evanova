package com.tlabs.android.jeeves.service.actions;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.tlabs.eve.api.mail.NotificationMessage;
import com.tlabs.eve.api.mail.NotificationTextRequest;
import com.tlabs.eve.api.mail.NotificationTextResponse;

import java.util.Map;

public final class NotificationBodiesAction extends EveSingleAction<NotificationTextRequest, NotificationTextResponse> {
    private final long ownerID;

    public NotificationBodiesAction(final Context context, final long ownerID, final long[] messageIds) {
        super(context, new NotificationTextRequest(Long.toString(ownerID), messageIds));
        this.ownerID = ownerID;
    }

    @Override
    protected EveAction onActionCompleted(NotificationTextRequest request, NotificationTextResponse response) {
        if (response.getCached()) {
            return null;
        }

        final Map<Long, String> names = new ArrayMap<>();
        for (NotificationMessage m : response.getMessages()) {
            mail.updateNotification(ownerID, m);
            //FIXME MAIL
            /*MailProvider.updateNotificationBody(cr, ownerID, m);
            if (StringUtils.isNotBlank(m.getSenderName())) {
                names.put(m.getSenderID(), m.getSenderName());
            }*/
        }
        //FIXME MAIL
        //EvanovaProvider.updateNames(cr, names);
       // MailProvider.updateNames(cr, names);
        return null;
    }
}
