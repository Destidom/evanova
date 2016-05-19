package com.tlabs.android.jeeves.network;

import android.content.Context;

import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.android.jeeves.service.EveAPIServicePreferences;
import com.tlabs.android.util.Log;
import com.tlabs.eve.EveRequest;
import com.tlabs.eve.api.EveAPIRequest;
import com.tlabs.eve.api.character.CharacterRequest;
import com.tlabs.eve.api.corporation.CorporationRequest;

import org.apache.commons.lang3.StringUtils;
import org.devfleet.crest.CrestService;
import org.devfleet.crest.model.CrestCharacter;
import org.devfleet.crest.retrofit.CrestClient;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public final class Authentication {

    private static final String LOG = "EveAuthenticationHelper";

    private static Authentication helper;

    private final EvanovaFacade evanova;

    private final CrestClient crest;
    private final Map<Long, WeakReference<CrestService>> authenticated;

    private Authentication(
            final Context context,
            final EvanovaFacade evanova) {
        final EveAPIServicePreferences preferences = new EveAPIServicePreferences(context.getApplicationContext());

        this.evanova = evanova;
        this.authenticated = new HashMap<>();
        this.crest =
            CrestClient
                .TQ(CrestClient.CHARACTER_SCOPES)
                .id(preferences.getApplicationId())
                .key(preferences.getApplicationKey())
                .redirect(preferences.getApplicationRedirect())
                .build();
        preferences.setCrestLogin(this.crest.getLoginUri());//FIXME berk
    }

    public static Authentication from(final Context context, final EvanovaFacade facade) {
        if (null == helper) {
            helper = new Authentication(context, facade);
        }
        return helper;
    }

    public EveAccount authenticate(final String authCode) {
        try {
            final CrestService service = this.crest.fromAuthCode(authCode);
            final CrestCharacter character = service.getCharacter();

            final EveAccount entity = new EveAccount();
            entity.setOwnerId(character.getId());
            entity.setName(character.getName());
            entity.setRefreshToken(character.getRefreshToken());
            entity.setAccessToken(character.getAccessToken());
            entity.setAccessMask(this.crest.getCharacterAccessMask());
            entity.setType(EveAccount.CHARACTER);
            this.evanova.saveAccount(entity);
            this.authenticated.put(character.getId(), new WeakReference<>(service));
            return entity;
        }
        catch (IOException | IllegalStateException e) {
            Log.e(LOG, e.getLocalizedMessage());
            return null;
        }
    }

    public boolean authenticate(final EveRequest<?> r) {
        if (!(r instanceof EveAPIRequest<?>)) {
            if (Log.D)
                Log.d(LOG, "authenticate(true): " + r.getClass().getSimpleName() + ": no authentication required.");
            return true;
        }

        if (!(r instanceof EveAPIRequest.Authenticated)) {
            if (Log.D)
                Log.d(LOG, "authenticate(true): " + r.getClass().getSimpleName() + ": no authentication required.");
            return true;
        }

        if (StringUtils.isNotBlank(r.getParameters().get("keyID")) && StringUtils.isNotBlank(r.getParameters().get("vCode"))) {
            if (Log.D)
                Log.d(LOG, "authenticate(true): " + r.getClass().getSimpleName() + ": keys already set.");
            return true;
        }
        if (StringUtils.isNotBlank(r.getParameters().get("accessToken")) && StringUtils.isNotBlank(r.getParameters().get("accessType"))) {
            if (Log.D)
                Log.d(LOG, "authenticate(true): " + r.getClass().getSimpleName() + ": token already set.");
            return true;
        }

        final EveAPIRequest<?> apiRequest = (EveAPIRequest<?>) r;
        if (addAPIKeysV2(apiRequest)) {
            if (Log.D)
                Log.d(LOG, "authenticate(true): " + r.getClass().getSimpleName() + ": authenticated by helper.");
            return true;
        }
        if (r instanceof EveAPIRequest.Public) {
            if (Log.D)
                Log.d(LOG, "authenticate(true): " + r.getClass().getSimpleName() + ": public info only");
            return true;
        }

        if (Log.D)
            Log.d(LOG, "authenticate(false): " + r.getClass().getSimpleName() + ": no valid authentication found");
        return false;
    }

    private boolean addAPIKeysV2(final EveAPIRequest<?> r) {
        long ownerID = -1;
        if (r instanceof CharacterRequest) {
            ownerID = Long.parseLong(((CharacterRequest) r).getCharacterID());
        }
        else if (r instanceof CorporationRequest) {
            ownerID = Long.parseLong(((CorporationRequest) r).getCorporationID());
        }
        if (ownerID == -1) {
            return false;
        }

        final EveAccount account = findAccount(ownerID);
        if (null == account) {
            if (Log.D)
                Log.d(LOG, "authenticate: no valid account found " + r.getClass().getSimpleName());
            return false;
        }

        if (StringUtils.isNotBlank(account.getKeyValue())) {
            r.putParam("keyID", account.getKeyID());
            r.putParam("vCode", account.getKeyValue());
            if (Log.D)
                Log.d(LOG, "authenticate(true): " + r.getClass().getSimpleName() + ": by api keys");
            return true;
        }

        if (StringUtils.isNotBlank(account.getAccessToken())) {
            r.putParam("accessType", r instanceof CorporationRequest<?> ? "corporation" : "character");
            r.putParam("accessToken", account.getAccessToken());
            if (Log.D)
                Log.d(LOG, "authenticate(true): " + r.getClass().getSimpleName() + ": by token");
            return true;
        }

        return false;
    }

    private EveAccount findAccount(final long ownerID) {
        final EveAccount account = this.evanova.getOwnerAccount(ownerID);
        if (null == account) {
            if (Log.D)
                Log.d(LOG, "authenticate: no account found for owner " + ownerID);
        }

        if (StringUtils.isBlank(account.getRefreshToken())) {
            return account;
        }
        return refreshApiToken(account);
    }

    private EveAccount refreshApiToken(final EveAccount entity) {
        WeakReference<CrestService> ref = this.authenticated.get(entity.getOwnerId());
        CrestService service = null == ref ? null : ref.get();
        if (null == service) {
            try {
                service = this.crest.fromRefreshToken(entity.getRefreshToken());
                this.authenticated.put(entity.getOwnerId(), new WeakReference<>(service));
            }
            catch (IOException e) {
                Log.e(LOG, e.getLocalizedMessage());
                return null;
            }
        }
        try {
            final CrestCharacter character = service.getCharacter();
            if (!StringUtils.equals(entity.getAccessToken(), character.getAccessToken())) {
                entity.setAccessToken(character.getAccessToken());
                this.evanova.saveAccount(entity);
            }
            return entity;
        }
        catch (IllegalStateException e) {
            Log.e(LOG, e.getLocalizedMessage());
            return null;
        }
    }

}
