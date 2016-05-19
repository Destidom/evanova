package com.tlabs.android.jeeves.notifications;

import android.content.Context;

import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.eve.api.character.SkillInTraining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class AccountInspector extends Inspector {
    private static final long H72 = 3L * 24L * 3600L * 1000L;

    private final EveAccount account;
    private final EvanovaFacade facade;

    public AccountInspector(final Context base, final EvanovaFacade facade, final EveAccount account) {
        super(base);
        this.facade = facade;
        this.account = account;
    }

    @Override
    public List<Notification> inspect() {
        final List<Notification> notifications = new ArrayList<>(2);
        final Notification training = inspectTraining();
        if (null != training) {
            notifications.add(training);
        }

        final Notification expiration = inspectExpiration();
        if (null != expiration) {
            notifications.add(expiration);
        }

        if (notifications.size() < 2) {
            return notifications;
        }

        final StringBuilder text = new StringBuilder();
        for (Notification n: notifications) {
            text.append(n.getText());
            text.append("\n");
        }
        return Collections.singletonList(
                Notification.from(this, this.account).
                        setText(text.toString()).
                        setCount(notifications.size()));
    }

    private Notification inspectTraining() {
        if (!getPreferences().getAccountTrainingNotificationOption(account.getId())) {
            return null;
        }

        //FIXME
       /* final List<Long> characters = facade.listCharacters(account.getId());
        if (characters.isEmpty()) {
            return null;
        }

        for (long id: characters) {
            if (hasTraining(id)) {
                return null;
            }
        }

        return Notification.from(this, this.account).
                setSubtitle("Account has no active training.").
                setText("Account has no active training.");*/
        return null;
    }

    private Notification inspectExpiration() {
        if (account.getPaidUntil() == 0) {
            return null;
        }
        if (!getPreferences().getAccountStatusNotificationOption(account.getId())) {
            return null;
        }
        final long expiresIn = this.account.getPaidUntil() - System.currentTimeMillis();
        if (expiresIn <= 0) {
            return null;
        }

        if (expiresIn < H72) {
            final String message = account.getName() + " paid until " + FormatHelper.DateTime.SHORT(this.account.getPaidUntil());
            return Notification.from(this, this.account).
                    setSubtitle(message).
                    setText(message).
                    setWhen(this.account.getPaidUntil());
        }
        return null;
    }

    private boolean hasTraining(final long characterID) {
        final List<SkillInTraining> queue = facade.getTrainingQueue(characterID);
        if (queue.isEmpty()) {
            return false;
        }
        for (SkillInTraining t: queue) {
            if (t.getStartTime() == 0 && t.getEndTime() == 0) {
                continue;
            }
            if (t.getStartTime() < System.currentTimeMillis() && t.getEndTime() > System.currentTimeMillis()) {
                return true;
            }
        }

        return false;
    }
}
