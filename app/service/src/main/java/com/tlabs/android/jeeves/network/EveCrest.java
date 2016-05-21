package com.tlabs.android.jeeves.network;

import android.content.Context;

import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.service.R;

import org.apache.commons.lang3.StringUtils;
import org.devfleet.crest.CrestAccess;
import org.devfleet.crest.CrestService;
import org.devfleet.crest.retrofit.CrestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class EveCrest {
    private static final Logger LOG = LoggerFactory.getLogger(EveCrest.class);
    private EveCrest() {}

    public static String loginUri(final Context context, final String[] scopes) {
        return CrestClient.getLoginUri(
            "login.eveonline.com",
            enh(context, R.string.jeeves_crest_id),
            enh(context, R.string.jeeves_crest_key),
            enh(context, R.string.jeeves_crest_callback),
            scopes
        );
    }

    public static String loginUri(final Context context, EveAccount account) {
        if ((null == account) || StringUtils.isBlank(account.getRefreshToken())) {
            return loginUri(context, CrestAccess.PUBLIC_SCOPES);
        }
        if (account.getType() == EveAccount.CORPORATION) {
            return loginUri(context, CrestAccess.CORPORATION_SCOPES);
        }
        return loginUri(context, CrestAccess.CHARACTER_SCOPES);
    }

    public static CrestClient client() {
        return CrestClient.TQ(CrestAccess.PUBLIC_SCOPES).build();
    }

    public static CrestClient client(final Context context, final String[] scopes) {
        return
            CrestClient
                .TQ(scopes)
                .id(enh(context, R.string.jeeves_crest_id))
                .key(enh(context, R.string.jeeves_crest_key))
                .redirect(enh(context, R.string.jeeves_crest_callback))
                .build();
    }

    public static CrestService service(final Context context) {
        try {
            return CrestClient.TQ("publicData").build().fromDefault();
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            LOG.debug("{}: no public CREST available", e.getLocalizedMessage());
            return null;
        }
    }

    public static CrestService service(
            final Context context, final EveAccount account) {
        final CrestClient authenticatedCREST = client(
                context,
                (account.getType() == EveAccount.CORPORATION) ? CrestAccess.CORPORATION_SCOPES : CrestAccess.CHARACTER_SCOPES);
        try {
            return authenticatedCREST.fromRefreshToken(account.getRefreshToken());
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            LOG.debug("{}: providing public CREST", e.getLocalizedMessage());
            return service(context);
        }
    }
    //TODO the idea behind this function is to decrypt resources.
    private static String enh(final Context context, final int r) {
        return context.getResources().getString(r);
    }
}
