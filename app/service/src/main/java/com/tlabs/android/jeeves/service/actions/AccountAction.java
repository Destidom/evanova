package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.eve.EveRequest;
import com.tlabs.eve.EveResponse;
import com.tlabs.eve.api.AccessInfo;
import com.tlabs.eve.api.AccessInfoRequest;
import com.tlabs.eve.api.AccessInfoResponse;
import com.tlabs.eve.api.AccountStatus;
import com.tlabs.eve.api.AccountStatusRequest;
import com.tlabs.eve.api.AccountStatusResponse;
import com.tlabs.eve.api.EveAPIRequest;
import com.tlabs.eve.api.character.CharacterSheet;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccountAction extends EveAction {

    private boolean authenticated;
    private final EveAccount template;
    private final List<EveAccount> result;

    public AccountAction(final Context context, final EveAccount account) {
        super(context,
                set(new AccessInfoRequest(), account),
                set(new AccountStatusRequest(), account));

        this.authenticated = false;
        this.template = account;
        this.result = new ArrayList<>(3);
    }

    @Override
    public final EveAction onRequestCompleted(EveRequest<? extends EveResponse> request, EveResponse response) {
        if (response instanceof AccessInfoResponse) {
            onAccessInfo((AccessInfoResponse) response);
            return null;
        }

        if (response instanceof AccountStatusResponse) {
            onAccountStatus((AccountStatusResponse)response);
            finish();
            return null;
        }

        return null;
    }

    public final boolean getAuthenticated() {
        return this.authenticated;
    }

    public final List<EveAccount> getAccounts() {return Collections.unmodifiableList(this.result);}

    private void finish() {
        for (EveAccount account: this.result) {
            this.evanova.saveAccount(account);
        }
    }

    private void onAccessInfo(final AccessInfoResponse r) {
        this.authenticated = !r.hasAuthenticationError() && null != r.getAccessInfo();
        if (!authenticated) {
            final EveAccount account = new EveAccount(this.template);
            account.setAccessMask(0);
            account.setStatus(1);
            account.setStatusMessage("Authentication error.");
            result.add(account);
            return;
        }

        switch (r.getAccessInfo().getType()) {
            case AccessInfo.ACCOUNT:
                addAccount(r);
                break;
            case AccessInfo.CHARACTER:
                addCharacter(r);
                break;
            case AccessInfo.CORPORATION:
                addCorporation(r);
                break;
            case AccessInfo.UNKNOWN:
            default:
                addUnknown(r);
                break;
        }

    }

    private void addAccount(final AccessInfoResponse r) {
        final EveAccount account = newAccount(r, EveAccount.ACCOUNT);
        final AccessInfo accessInfo = r.getAccessInfo();

        if (accessInfo.getCharacters().isEmpty()) {
            account.setStatus(3);
            account.setStatusMessage("No character found");
            this.result.add(account);
        }
        else {
            for (CharacterSheet s: accessInfo.getCharacters()) {
                EveAccount tmp = new EveAccount(account);
                tmp.setName(s.getCharacterName());
                tmp.setOwnerId(s.getCharacterID());
                this.result.add(tmp);
            }
        }
    }

    private void addCharacter(final AccessInfoResponse r) {
        final EveAccount account = newAccount(r, EveAccount.CHARACTER);
        final AccessInfo accessInfo = r.getAccessInfo();

        if (accessInfo.getCharacters().isEmpty()) {
            account.setStatus(3);
            account.setStatusMessage("No character found");
        }
        else {
            account.setStatus(0);
            final CharacterSheet character = accessInfo.getCharacters().get(0);
            account.setOwnerId(character.getCharacterID());
            if (StringUtils.isBlank(account.getName())) {
                account.setName(character.getCharacterName());
            }
        }
        this.result.add(account);
    }

    private void addCorporation(final AccessInfoResponse r) {
        final EveAccount account = newAccount(r, EveAccount.CORPORATION);
        final AccessInfo accessInfo = r.getAccessInfo();

        if (accessInfo.getCharacters().isEmpty()) {
            account.setStatus(3);
            account.setStatusMessage("No corporation found");
        }
        else {
            account.setStatus(0);
            final CharacterSheet character = accessInfo.getCharacters().get(0);
            account.setOwnerId(character.getCorporationID());
            if (StringUtils.isBlank(account.getName())) {
                account.setName(character.getCorporationName());
            }
        }
        this.result.add(account);
    }

    private void addUnknown(final AccessInfoResponse r) {
        final EveAccount account = newAccount(r, EveAccount.UNKNOWN);

        account.setStatus(3);
        account.setStatusMessage("Unknown account type");

        this.result.add(account);
    }

    private void onAccountStatus(final AccountStatusResponse r) {
        for (EveAccount account: this.result) {
            if (r.hasAuthenticationError()) {
                account.setPaidUntil(0);
                account.setLogonCount(0);
                account.setLogonMinutes(0);
                account.setCreationDate(0);
            }
            else {
                final AccountStatus accountStatus = r.getAccountStatus();
                account.setCreationDate(accountStatus.getCreationDate());
                account.setLogonCount(accountStatus.getLogonCount());
                account.setLogonMinutes(accountStatus.getLogonMinutes());
                account.setPaidUntil(accountStatus.getPaidUntil());
            }
        }
    }

    private EveAccount newAccount(final AccessInfoResponse r, int type) {
        final EveAccount account = new EveAccount(this.template);
        account.setType(type);

        final AccessInfo accessInfo = r.getAccessInfo();
        account.setAccessMask(accessInfo.getAccessMask());
        account.setExpires(accessInfo.getExpires());

        return account;
    }

    private static EveAPIRequest<?> set(final EveAPIRequest<?> request, final EveAccount account) {
        if (StringUtils.isNotBlank(account.getKeyID())) {
            request.putParam("keyID", account.getKeyID());
        }
        if (StringUtils.isNotBlank(account.getKeyValue())) {
            request.putParam("vCode", account.getKeyValue());
        }
        if (StringUtils.isNotBlank(account.getAccessToken())) {
            request.putParam("accessToken", account.getAccessToken());
        }
        if (StringUtils.isNotBlank(account.getRefreshToken())) {
            request.putParam("refreshToken", account.getRefreshToken());
        }
        switch (account.getType()) {
            case EveAccount.CHARACTER:
            case EveAccount.ACCOUNT:
                request.putParam("accessType", "character");
                break;
            case EveAccount.CORPORATION:
                request.putParam("accessType", "corporation");
                break;
            default:
                break;
        }
        return request;
    }
}
