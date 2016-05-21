package com.tlabs.android.jeeves.model.data.evanova;

import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.model.data.evanova.entities.AccountEntity;

import org.apache.commons.lang3.StringUtils;

public final class AccountEntities {

    private AccountEntities() {
    }

    public static EveAccount map(final AccountEntity entity) {
        if (null == entity) {
            return null;
        }

        final EveAccount account = new EveAccount();
        account.setId(entity.getId());
        account.setKeyID(Long.toString(entity.getKeyID()));
        account.setKeyValue(entity.getKeyValue());
        account.setAccessToken(entity.getAccessToken());
        account.setRefreshToken(entity.getRefreshToken());
        account.setType(entity.getType());
        account.setPaidUntil(entity.getPaidUntil());
        account.setName(entity.getName());
        account.setAccessMask(entity.getMask());
        account.setCreationDate(entity.getCreated());
        account.setExpires(entity.getExpires());
        account.setLogonCount(entity.getLogonCount());
        account.setLogonMinutes(entity.getLogonMinutes());
        account.setOwnerId(entity.getOwnerID());
        account.setType(entity.getType());


        return account;
    }

    public static AccountEntity map(final EveAccount account) {
        if (null == account) {
            return null;
        }

        final AccountEntity entity = new AccountEntity();
        entity.setId(account.getId());
        if (StringUtils.isBlank(account.getKeyID())) {
            entity.setKeyID(0);
            entity.setKeyValue(null);
        }
        else {
            entity.setKeyID(Long.parseLong(account.getKeyID()));
            entity.setKeyValue(account.getKeyValue());
        }

        entity.setAccessToken(account.getAccessToken());
        entity.setRefreshToken(account.getRefreshToken());
        entity.setType(account.getType());
        entity.setPaidUntil(account.getPaidUntil());
        entity.setName(account.getName());
        entity.setMask(account.getAccessMask());
        entity.setCreated(account.getCreationDate());
        entity.setExpires(account.getExpires());
        entity.setLogonCount(account.getLogonCount());
        entity.setLogonMinutes(account.getLogonMinutes());
        entity.setOwnerID(account.getOwnerId());
        entity.setType(entity.getType());
        return entity;
    }
}