package com.tlabs.android.jeeves.network;

import android.content.Context;

import com.tlabs.android.jeeves.service.EveAPIServicePreferences;
import com.tlabs.android.util.Log;
import com.tlabs.eve.EveRequest;
import com.tlabs.eve.ccp.EveRSSRequest;
import com.tlabs.eve.ccp.ImageRequest;
import com.tlabs.eve.central.EveCentralRequest;
import com.tlabs.eve.net.AbstractEveNetwork;
import com.tlabs.eve.zkb.ZKillRequest;

import java.net.HttpURLConnection;

public final class HttpNetwork extends AbstractEveNetwork {

    private EveAPIServicePreferences preferences;

    public HttpNetwork(final Context context) {
        super();
        this.preferences = new EveAPIServicePreferences(context);
    }

    @Override
    protected void onPrepareConnection(HttpURLConnection connection) {
        connection.setRequestProperty(
                "User-Agent",
                "Evanova (Android); https://play.google.com/store/apps/details?id=com.tlabs.android.evanova");
        Log.d("Evanova", ""  + connection.getURL());
    }

    @Override
    public String getUri(EveRequest<?> request) {
        if (request instanceof ImageRequest) {
            return preferences.getEveImageURL();
        }
        if (request instanceof EveRSSRequest) {
            return preferences.getEveRSSURL();
        }
        if (request instanceof EveCentralRequest) {
            return preferences.getEveCentralURL();
        }
        //FIXME CREST
        /*if (request instanceof CRESTRequest) {
            return preferences.getCrestURL();
        }*/
        if (request instanceof ZKillRequest) {
            return preferences.getZKillboardURL();
        }
        return preferences.getEveURL();
    }

}
