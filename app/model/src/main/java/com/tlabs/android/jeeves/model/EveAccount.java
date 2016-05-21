package com.tlabs.android.jeeves.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

public class EveAccount {
    public static final int UNKNOWN = -1;
    public static final int CHARACTER = 0;
    public static final int CORPORATION = 1;
    public static final int ACCOUNT = 2;

    private long id;
    private long ownerId;
    private int type = UNKNOWN;

    private String name;

    private String keyID;
    private String keyValue;

    private String accessToken;
    private String refreshToken;

    private long accessMask;
    private long creationDate;
    private long logonCount;
    private long logonMinutes;

    private long paidUntil;
    private long expires;

    private int status;
    private String statusMessage;

    private long shipID;
    private String shipName;

    public EveAccount() {
    }

    public EveAccount(final EveAccount account) {
        this.id = account.id;
        this.type = account.type;
        this.ownerId = account.ownerId;
        this.name = account.name;
        this.keyID = account.keyID;
        this.keyValue = account.keyValue;
        this.accessToken = account.accessToken;
        this.refreshToken = account.refreshToken;
        this.accessMask = account.accessMask;
        this.creationDate = account.creationDate;
        this.logonCount = account.logonCount;
        this.logonMinutes = account.logonMinutes;
        this.paidUntil = account.paidUntil;
        this.expires = account.expires;
        this.status = account.status;
        this.statusMessage = account.statusMessage;
        this.shipID = account.shipID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyID() {
        return keyID;
    }

    public void setKeyID(String keyID) {
        this.keyID = keyID;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getAccessMask() {
        return accessMask;
    }

    public void setAccessMask(long accessMask) {
        this.accessMask = accessMask;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public long getLogonCount() {
        return logonCount;
    }

    public void setLogonCount(long logonCount) {
        this.logonCount = logonCount;
    }

    public long getLogonMinutes() {
        return logonMinutes;
    }

    public void setLogonMinutes(long logonMinutes) {
        this.logonMinutes = logonMinutes;
    }

    public long getPaidUntil() {
        return paidUntil;
    }

    public void setPaidUntil(long paidUntil) {
        this.paidUntil = paidUntil;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getShipID() {
        return shipID;
    }

    public void setShipID(long shipID) {
        this.shipID = shipID;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public final List<String> getScopes() {
        if (StringUtils.isEmpty(this.refreshToken)) {
            return Collections.emptyList();
        }
        return EveAccessHelper.getCrestScopes(this.accessMask);
    }

    public final boolean hasCharacterScope() {
        if (StringUtils.isEmpty(this.refreshToken)) {
            return false;
        }
        return EveAccessHelper.hasCharacterCrest(this.accessMask);
    }

    public final boolean hasCorporationScope() {
        if (StringUtils.isEmpty(this.refreshToken)) {
            return false;
        }
        return EveAccessHelper.hasCorporationCrest(this.accessMask);
    }

    public final boolean hasApiKey() {
        return StringUtils.isNotBlank(this.keyValue);
    }
}