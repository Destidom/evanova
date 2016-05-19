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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccountAction extends EveAction {

    private boolean authenticated;
    private final EveAccount account;
    private final List<EveAccount> result;

    public AccountAction(final Context context, final EveAccount account) {
        super(context,
                set(new AccessInfoRequest(), account),
                set(new AccountStatusRequest(), account));

        this.authenticated = false;
        this.account = account;
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
        if (!this.authenticated) {
            return;
        }
    }

    private void onAccessInfo(final AccessInfoResponse r) {
        this.authenticated = !r.hasAuthenticationError() && null != r.getAccessInfo();
        if (!authenticated) {
            this.account.setAccessMask(0);
            this.account.setStatus(1);
            this.account.setStatusMessage("Authentication error.");
            this.evanova.saveAccount(this.account);
            return;
        }

        final AccessInfo accessInfo = r.getAccessInfo();
        this.account.setAccessMask(accessInfo.getAccessMask());
        this.account.setExpires(accessInfo.getExpires());

        switch (accessInfo.getType()) {
            case AccessInfo.ACCOUNT:
                account.setType(EveAccount.ACCOUNT);
                if (accessInfo.getCharacters().isEmpty()) {
                    account.setStatus(3);
                    account.setStatusMessage("No character found");
                    this.result.add(this.evanova.saveAccount(this.account));
                }
                else {
                    for (CharacterSheet s: accessInfo.getCharacters()) {
                        EveAccount tmp = new EveAccount(this.account);
                        tmp.setName(s.getCharacterName());
                        tmp.setOwnerId(s.getCharacterID());
                        this.result.add(this.evanova.saveAccount(tmp));
                    }
                }
                break;
            case AccessInfo.CHARACTER:
                account.setType(EveAccount.CHARACTER);
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
                this.result.add(this.evanova.saveAccount(this.account));
                break;
            case AccessInfo.CORPORATION:
                account.setType(EveAccount.CORPORATION);
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
                this.result.add(this.evanova.saveAccount(this.account));
                break;
            case AccessInfo.UNKNOWN:
            default:
                account.setType(EveAccount.UNKNOWN);
                account.setStatus(3);
                account.setStatusMessage("Unknown account type");
                this.result.add(this.evanova.saveAccount(this.account));
                break;
        }

    }

    private void onAccountStatus(final AccountStatusResponse r) {
        if (r.hasAuthenticationError()) {
            this.account.setPaidUntil(0);
            this.account.setLogonCount(0);
            this.account.setLogonMinutes(0);
            this.account.setCreationDate(0);
        }
        else {
            final AccountStatus accountStatus = r.getAccountStatus();
            this.account.setCreationDate(accountStatus.getCreationDate());
            this.account.setLogonCount(accountStatus.getLogonCount());
            this.account.setLogonMinutes(accountStatus.getLogonMinutes());
            this.account.setPaidUntil(accountStatus.getPaidUntil());
        }
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
