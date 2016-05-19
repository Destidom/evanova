package com.tlabs.android.jeeves.service;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.WorkerThread;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.ArrayMap;

import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.android.jeeves.network.Authentication;
import com.tlabs.android.jeeves.service.actions.AccountAction;
import com.tlabs.android.jeeves.service.actions.CharacterCacheAction;
import com.tlabs.android.jeeves.service.actions.CharacterCalendarAction;
import com.tlabs.android.jeeves.service.actions.CharacterCalendarEventAction;
import com.tlabs.android.jeeves.service.actions.CharacterColoniesAction;
import com.tlabs.android.jeeves.service.actions.CharacterContractItemsAction;
import com.tlabs.android.jeeves.service.actions.CharacterContractsAction;
import com.tlabs.android.jeeves.service.actions.CharacterInfoAction;
import com.tlabs.android.jeeves.service.actions.CharacterMailboxAction;
import com.tlabs.android.jeeves.service.actions.CharacterSocialAction;
import com.tlabs.android.jeeves.service.actions.CorporationCacheAction;
import com.tlabs.android.jeeves.service.actions.CorporationContractItemsAction;
import com.tlabs.android.jeeves.service.actions.CorporationContractsAction;
import com.tlabs.android.jeeves.service.actions.CorporationInfoAction;
import com.tlabs.android.jeeves.service.actions.CorporationMailboxAction;
import com.tlabs.android.jeeves.service.actions.CorporationSocialAction;
import com.tlabs.android.jeeves.service.actions.CorporationStarbaseAction;
import com.tlabs.android.jeeves.service.actions.MailBodiesAction;
import com.tlabs.android.jeeves.service.actions.NotificationBodiesAction;
import com.tlabs.android.jeeves.service.actions.ServerInformationAction;
import com.tlabs.android.jeeves.service.events.BroadcastedEvent;
import com.tlabs.android.jeeves.service.events.CharacterCalendarEndEvent;
import com.tlabs.android.jeeves.service.events.CharacterCalendarStartEvent;
import com.tlabs.android.jeeves.service.events.CharacterSocialEndEvent;
import com.tlabs.android.jeeves.service.events.CharacterSocialStartEvent;
import com.tlabs.android.jeeves.service.events.CharacterUpdateEndEvent;
import com.tlabs.android.jeeves.service.events.CharacterUpdateStartEvent;
import com.tlabs.android.jeeves.service.events.ContractItemsEndEvent;
import com.tlabs.android.jeeves.service.events.ContractItemsStartEvent;
import com.tlabs.android.jeeves.service.events.CorporationSocialEndEvent;
import com.tlabs.android.jeeves.service.events.CorporationSocialStartEvent;
import com.tlabs.android.jeeves.service.events.CorporationUpdateEndEvent;
import com.tlabs.android.jeeves.service.events.CorporationUpdateStartEvent;
import com.tlabs.android.jeeves.service.events.EveAccountUpdateEndEvent;
import com.tlabs.android.jeeves.service.events.EveAccountUpdateStartEvent;
import com.tlabs.android.jeeves.service.events.EveApiEvent;
import com.tlabs.android.jeeves.service.events.EveApiKeyImportEndEvent;
import com.tlabs.android.jeeves.service.events.EveApiKeyImportStartEvent;
import com.tlabs.android.jeeves.service.events.EveAuthCodeImportEndEvent;
import com.tlabs.android.jeeves.service.events.EveAuthCodeImportStartEvent;
import com.tlabs.android.jeeves.service.events.MailMessageEndEvent;
import com.tlabs.android.jeeves.service.events.MailMessageStartEvent;
import com.tlabs.android.jeeves.service.events.NotificationMessageEndEvent;
import com.tlabs.android.jeeves.service.events.NotificationMessageStartEvent;
import com.tlabs.android.jeeves.service.events.ServerInformationUpdateStartEvent;
import com.tlabs.android.jeeves.service.events.StarbaseEndEvent;
import com.tlabs.android.jeeves.service.events.StarbaseStartEvent;
import com.tlabs.android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.time.StopWatch;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

final class EveApiServiceHelper {

    private static final String LOG = "EveApiServiceHelper";
    private static Map<Integer, EveApiEvent> lru = new ArrayMap<>();

    private final Context context;
    private final EvanovaFacade evanova;

    private final EveApiActionHelper actions;
    private final Authentication authentication;

    public EveApiServiceHelper(final Context context, final CacheFacade cache, final EvanovaFacade evanova) {
        this.context = context;
        this.evanova = evanova;
        this.authentication = Authentication.from(context, evanova);

        this.actions = new EveApiActionHelper(context, cache, this.authentication);
    }

    @WorkerThread
    public final void execute(final EveApiEvent event, final Messenger reply) {
        if (null == event) {
            return;
        }

        if (lru.containsKey(event.hashCode())) {
            Log.w(LOG, "EveApiServiceHelper.execute: already started " + event.getClass().getSimpleName());
            return;
        }
        lru.put(event.hashCode(), event);

        Observable.create(new Observable.OnSubscribe<EveApiEvent>() {
            @Override
            public void call(Subscriber<? super EveApiEvent> subscriber) {
                StopWatch sw = null;
                if (Log.D) {
                    sw = new StopWatch();
                    sw.start();
                    Log.d(LOG, "EveApiServiceHelper.execute: start " + ToStringBuilder.reflectionToString(event));
                }

                executeImpl(event, reply);
                subscriber.onNext(event);
                subscriber.onCompleted();
                if (Log.D) {
                    sw.stop();
                    Log.d(LOG, "EveApiServiceHelper.execute: end " + event.getClass().getSimpleName() + "; " + sw.getTime() + "ms.");
                }
            }
        }).
        subscribeOn(Schedulers.io()).
        subscribe(e -> lru.remove(e.hashCode()));
    }

    private void executeImpl(final EveApiEvent event, final Messenger messenger) {
        if (event instanceof ServerInformationUpdateStartEvent) {
            reloadServerInformation((ServerInformationUpdateStartEvent) event, messenger);
            return;
        }
        if (event instanceof EveApiKeyImportStartEvent) {
            importApiKey((EveApiKeyImportStartEvent) event, messenger);
            return;
        }
        if (event instanceof EveAuthCodeImportStartEvent) {
            importAuthCode((EveAuthCodeImportStartEvent) event, messenger);
            return;
        }
        if (event instanceof EveAccountUpdateStartEvent) {
            reloadAccount((EveAccountUpdateStartEvent) event, messenger);
            return;
        }
        if (event instanceof MailMessageStartEvent) {
            reloadMailBody((MailMessageStartEvent) event, messenger);
            return;
        }
        if (event instanceof NotificationMessageStartEvent) {
            reloadNotificationBody((NotificationMessageStartEvent) event, messenger);
            return;
        }
        if (event instanceof CharacterUpdateStartEvent) {
            reloadCharacter((CharacterUpdateStartEvent) event, messenger);
            return;
        }
        if (event instanceof CorporationUpdateStartEvent) {
            reloadCorporation((CorporationUpdateStartEvent) event, messenger);
            return;
        }
        if (event instanceof ContractItemsStartEvent) {
            reloadContractItems((ContractItemsStartEvent) event, messenger);
            return;
        }
        if (event instanceof StarbaseStartEvent) {
            reloadStarbaseDetails((StarbaseStartEvent) event, messenger);
            return;
        }
        if (event instanceof CharacterSocialStartEvent) {
            reloadCharacterSocial((CharacterSocialStartEvent) event, messenger);
            return;
        }
        if (event instanceof CorporationSocialStartEvent) {
            reloadCorporationSocial((CorporationSocialStartEvent) event, messenger);
            return;
        }
        if (event instanceof CharacterCalendarStartEvent) {
            reloadCharacterCalendar((CharacterCalendarStartEvent) event, messenger);
            return;
        }
        Log.w(LOG, "Unhandled event " + event.getClass());
    }

    private void reloadAccount(final EveAccountUpdateStartEvent startEvent, final  Messenger messenger) {
        final EveAccount account = evanova.getAccount(startEvent.getAccountID());
        if (null == account) {
            Log.w(LOG, "invalid account id " + startEvent.getAccountID());
            sendReply(context, new EveAccountUpdateEndEvent(startEvent.getAccountID(), false), messenger);
            return;
        }
        final AccountAction action = new AccountAction(context, account);
        this.actions.executeActions(startEvent.getForce(), action);

        sendReply(context, new EveAccountUpdateEndEvent(startEvent.getAccountID(), true), messenger);
        EveNotificationService.scheduleAlarm(context, EveNotificationService.getAccountAlarm(context, account.getId()));
        if (action.getAuthenticated() && startEvent.getForce()) {
           reloadAccounts(action);
        }
    }

    private void importApiKey(final EveApiKeyImportStartEvent startEvent, final Messenger messenger) {
        if (startEvent.getKeyID() <= 0) {
            Log.w(LOG, "No key id");
            sendReply(context, new EveApiKeyImportEndEvent(0, Collections.emptyList()), messenger);
            return;
        }
        if (StringUtils.isBlank(startEvent.getKeyValue())) {
            Log.w(LOG, "No key value");
            sendReply(context, new EveApiKeyImportEndEvent(startEvent.getKeyID(), Collections.emptyList()), messenger);
            return;
        }

        final String keyName =
                StringUtils.isBlank(startEvent.getKeyName()) ? Long.toString(startEvent.getKeyID()) : startEvent.getKeyName();

        final EveAccount account = new EveAccount();
        account.setKeyID(Long.toString(startEvent.getKeyID()));
        account.setKeyValue(startEvent.getKeyValue());
        account.setName(keyName);

        final AccountAction action = new AccountAction(context, account);
        this.actions.executeActions(true, action);

        if (!action.getAuthenticated()) {
            sendReply(context, new EveApiKeyImportEndEvent(
                    startEvent.getKeyID(),
                    Collections.emptyList()), messenger);
        }

        final List<Long> newAccounts = new ArrayList<>(3);
        for (EveAccount a: action.getAccounts()) {
            newAccounts.add(a.getId());
        }

        sendReply(context, new EveApiKeyImportEndEvent(startEvent.getKeyID(), newAccounts), messenger);
        reloadAccounts(action);
    }

    private void reloadAccounts(final AccountAction action) {
        for (EveAccount a: action.getAccounts()) {
            if (a.getStatus() == 0) {
                switch (a.getType()) {
                    case EveAccount.ACCOUNT:
                    case EveAccount.CHARACTER: {
                        final CharacterUpdateStartEvent event = new CharacterUpdateStartEvent(a.getOwnerId(), true);
                        reloadCharacter(event, null);
                        break;
                    }
                    case EveAccount.CORPORATION: {
                        final CorporationUpdateStartEvent event = new CorporationUpdateStartEvent(a.getOwnerId(), true);
                        reloadCorporation(event, null);
                        break;
                    }
                    default:
                        break;
                }
            }
        }

        final List<Long> newAccounts = new ArrayList<>(3);
        for (EveAccount a: action.getAccounts()) {
            newAccounts.add(a.getId());
        }

        for (Long id: newAccounts) {
            EveNotificationService.scheduleAlarm(context, EveNotificationService.getAccountAlarm(context, id));
        }
    }

    private void importAuthCode(final EveAuthCodeImportStartEvent startEvent, final Messenger messenger) {
        if (StringUtils.isBlank(startEvent.getAuthCode())) {
            Log.w(LOG, "No auth code");
            sendReply(context, new EveAuthCodeImportEndEvent(null, 0), messenger);
            return;
        }

        final EveAccount account = authentication.authenticate(startEvent.getAuthCode());
        if (null == account) {
            sendReply(context, new EveAuthCodeImportEndEvent(startEvent.getAuthCode(), 0), messenger);
            return;
        }

        sendReply(context, new EveAuthCodeImportEndEvent(startEvent.getAuthCode(), account.getId()), messenger);
        reloadCharacter(new CharacterUpdateStartEvent(account.getOwnerId(), true), null);
        EveNotificationService.scheduleAlarm(context, EveNotificationService.getAccountAlarm(context, account.getId()));
    }

    private void reloadCharacter(final CharacterUpdateStartEvent startEvent, final Messenger messenger) {
        final long charID = startEvent.getCharID();
        if (charID <= 0) {
            return;
        }
        if (!startEvent.getForce()) {
            if (null == evanova.hitCharacter(startEvent.getCharID())) {
                Log.d(LOG, "reloadCharacter: no CharacterInfo " + startEvent.getCharID());
                return;
            }
        }
        sendReply(context, startEvent, messenger);
        this.actions.executeActions(
                startEvent.getForce(),
                new CharacterInfoAction(context, charID),
                new CharacterCacheAction(context, charID),
                new CharacterColoniesAction(context, charID),
                new CharacterMailboxAction(context, charID),
                new CharacterContractsAction(context, charID));

        final EveApiEvent event = new CharacterUpdateEndEvent(charID);
        sendReply(context, event, messenger);
        if (startEvent.getForce()) {
            EveNotificationService.scheduleAlarm(context, EveNotificationService.getCharacterAlarm(context, charID));
        }
    }

    private void reloadCorporation(final CorporationUpdateStartEvent startEvent, final Messenger messenger) {
        final long corpID = startEvent.getCorporationID();
        if (corpID <= 0) {
            return;
        }
        if (!startEvent.getForce()) {
            if (null == evanova.hitCorporation(startEvent.getCorporationID())) {
                Log.d(LOG, "reloadCorporation: no CorporationInfo " + startEvent.getCorporationID());
                return;
            }
        }
        sendReply(context, startEvent, messenger);
        this.actions.executeActions(
                startEvent.getForce(),
                new CorporationInfoAction(context, corpID),
                new CorporationCacheAction(context, corpID),
                new CorporationMailboxAction(context, corpID),
                new CorporationContractsAction(context, corpID));

        final EveApiEvent event = new CorporationUpdateEndEvent(corpID);
        sendReply(context, event, messenger);
        if (startEvent.getForce()) {
            EveNotificationService.scheduleAlarm(context, EveNotificationService.getCorporationAlarm(context, corpID));
        }
    }

    private void reloadContractItems(final ContractItemsStartEvent startEvent, final Messenger messenger) {
        final long ownerID = startEvent.getOwnerID();
        if (ownerID <= 0) {
            return;
        }

        final long contractID = startEvent.getContractID();
        if (contractID <= 0) {
            return;
        }

        sendReply(context, startEvent, messenger);
        if (null == evanova.hitCharacter(ownerID)) {
            this.actions.executeActions(
                    false,
                    new CorporationContractItemsAction(context, ownerID, contractID));
        }
        else {
            this.actions.executeActions(
                    false,
                    new CharacterContractItemsAction(context, ownerID, contractID));
        }
        sendReply(context, new ContractItemsEndEvent(ownerID, contractID), messenger);
    }

    private void reloadStarbaseDetails(final StarbaseStartEvent startEvent, final Messenger messenger) {
        if (startEvent.getCorporationID() <= 0) {
            return;
        }
        if (startEvent.getStarbaseID() <= 0) {
            return;
        }
        sendReply(context, startEvent, messenger);
        this.actions.executeActions(
                false,
                new CorporationStarbaseAction(context, startEvent.getCorporationID(), startEvent.getStarbaseID()));
        sendReply(context, new StarbaseEndEvent(startEvent.getCorporationID(), startEvent.getStarbaseID()), messenger);
    }

    private void reloadServerInformation(final ServerInformationUpdateStartEvent event, final Messenger messenger) {
        this.actions.executeActions(
                false,
                new ServerInformationAction(context));
    }

    private void reloadMailBody(final MailMessageStartEvent startEvent, final Messenger messenger) {
        if (startEvent.getCharID() <= 0) {
            return;
        }
        sendReply(context, startEvent, messenger);
        this.actions.executeActions(
                false,
                new MailBodiesAction(context, startEvent.getCharID(), startEvent.getMailIds()));
        sendReply(context, new MailMessageEndEvent(startEvent.getCharID(), startEvent.getMailIds()), messenger);
    }

    private void reloadCharacterSocial(final CharacterSocialStartEvent startEvent, final Messenger messenger) {
        sendReply(context, startEvent, messenger);
        this.actions.executeActions(
                false,
                new CharacterSocialAction(context, startEvent.getCharID()));
        sendReply(context, new CharacterSocialEndEvent(startEvent.getCharID()), messenger);
    }

    private void reloadCorporationSocial(final CorporationSocialStartEvent startEvent, final Messenger messenger) {
        if (startEvent.getCorporationID() <= 0) {
            return;
        }
        sendReply(context, startEvent, messenger);
        this.actions.executeActions(
                false,
                new CorporationSocialAction(context, startEvent.getCorporationID()));
        sendReply(context, new CorporationSocialEndEvent(startEvent.getCorporationID()), messenger);
    }

    private void reloadNotificationBody(final NotificationMessageStartEvent startEvent, final Messenger messenger) {
        if (startEvent.getCharID() <= 0) {
            return;
        }
        sendReply(context, startEvent, messenger);
        this.actions.executeActions(
                false,
                new NotificationBodiesAction(context, startEvent.getCharID(), startEvent.getMailIds()));
        sendReply(context,
                new NotificationMessageEndEvent(startEvent.getCharID(), startEvent.getMailIds()),
                messenger);
    }

    private void reloadCharacterCalendar(final CharacterCalendarStartEvent startEvent, final Messenger messenger) {
        if (startEvent.getCharID() <= 0) {
            return;
        }

        sendReply(context, startEvent, messenger);

        final CharacterCalendarAction action = new CharacterCalendarAction(context, startEvent.getCharID());
        this.actions.executeActions(false, action);

        final List<CharacterCalendarEventAction> eventActions = new ArrayList<>(action.getEvents().size());
        for (Long event: action.getEvents()) {
            eventActions.add(new CharacterCalendarEventAction(context, startEvent.getCharID(), event));
        }
        this.actions.executeActions(false, eventActions.toArray(new CharacterCalendarEventAction[eventActions.size()]));
        sendReply(context,
                new CharacterCalendarEndEvent(startEvent.getCharID()),
                messenger);
    }

    private static void sendReply(final Context context, final EveApiEvent event, final Messenger messenger) {
        Intent intent = null;

        if (null == messenger || event instanceof BroadcastedEvent) {
            intent = new Intent(EveApiEvent.class.getName());
            intent.putExtra("event", Parcels.wrap(event));
        }

        if (event instanceof BroadcastedEvent) {
            if (Log.D) {
                Log.d(LOG, "sendBroadcast: " + event.getClass().getSimpleName());
            }
            context.sendBroadcast(intent);
        }
        if (null == messenger) {
            if (Log.D) {
                Log.d(LOG, "sendLocalBroadcast: " + event.getClass().getSimpleName());
            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
        else {
            if (Log.D) {
                Log.d(LOG, "sendMessage: " + ToStringBuilder.reflectionToString(event, ToStringStyle.SHORT_PREFIX_STYLE));
            }
            sendMessage(event, messenger);
        }
    }

    private static void sendMessage(final EveApiEvent event, final Messenger messenger) {
        final Message message = Message.obtain();
        message.obj = event;
        try {
            messenger.send(message);
        }
        catch (RemoteException e) {
            Log.e(LOG, "sendMessage: exception sending message: " + e.getMessage());
        }
    }
}
