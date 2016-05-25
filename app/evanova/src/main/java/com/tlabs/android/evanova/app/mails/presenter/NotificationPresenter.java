package com.tlabs.android.evanova.app.mails.presenter;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.mails.EveMailUseCase;
import com.tlabs.android.evanova.app.mails.MailView;
import com.tlabs.android.evanova.app.mails.ui.MailActivity;

import java.util.Collections;

import javax.inject.Inject;

public class NotificationPresenter extends EvanovaActivityPresenter<MailView> {

    private final EveMailUseCase useCase;

    @Inject
    public NotificationPresenter(Context context, EveMailUseCase useCase) {
        super(context);
        this.useCase = useCase;
    }


    @Override
    public void startView(Intent intent) {
        setOwner((null == intent) ? -1l : intent.getLongExtra(MailActivity.EXTRA_OWNER_ID, -1));
    }

    public void setOwner(final long ownerID) {
        if (ownerID > 0) {
            subscribe(() -> this.useCase.loadNotificationBoxes(ownerID), b -> getView().showNotificationBoxes(b));
        }
        else {
            getView().showNotificationBoxes(Collections.emptyList());
        }
    }

}
