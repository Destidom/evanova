package com.tlabs.android.evanova.app.killmail.impl;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tlabs.android.jeeves.model.data.sde.EveFacade;
import com.tlabs.android.jeeves.service.EveAPIServicePreferences;
import com.tlabs.eve.api.mail.KillMailAttacker;
import com.tlabs.eve.api.mail.KillMailItem;
import com.tlabs.eve.api.mail.KillMailVictim;
import com.tlabs.eve.zkb.ZKillInfoRequest;
import com.tlabs.eve.zkb.ZKillInfoResponse;
import com.tlabs.eve.zkb.ZKillMail;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


@Deprecated //this is old Volley based code but it checks out.
final class KillMailLoader {

    private static final Logger LOG = LoggerFactory.getLogger(KillMailLoader.class);
    private static final String TAG = "KillMailLoader";

    public interface Listener {

        void onKillMail(final ZKillMail killMail);
    }

    private static class KillMailResponseAdapter implements Response.Listener<String>, ErrorListener {
        private final ZKillInfoRequest request;

        public KillMailResponseAdapter(final ZKillInfoRequest request) {
            this.request = request;
        }

        public void onResponse(final ZKillMail killMail) {}

        @Override
        public final void onErrorResponse(VolleyError volleyError) {
            LOG.warn(volleyError.getMessage());
            onResponse((ZKillMail)null);
        }

        @Override
        public final void onResponse(String s) {
            try {
                final ZKillInfoResponse r = com.tlabs.eve.EveFacade.parse(request, IOUtils.toInputStream(s));
                if (r.getKills().isEmpty()) {
                    LOG.warn("KillMail not found {}", request.toURI(""));
                    return;
                }
                onResponse(r.getKills().get(0));
            }
            catch (IOException e) {
                LOG.error(e.getLocalizedMessage());
            }
        }
    }

    private final RequestQueue volley;
    private final Context context;
    private final EveFacade eve;

    @Inject
    public KillMailLoader(final Context context, final EveFacade eve) {
        this.volley = Volley.newRequestQueue(context);
        this.context = context;
        this.eve = eve;
    }

    public final void start() {
        this.volley.start();
    }

    public final void stop() {
        this.volley.cancelAll(TAG);
        this.volley.stop();
    }

    public void load(final long killMailID, final Listener listener) {
        final ZKillInfoRequest request = new ZKillInfoRequest(killMailID);

        final KillMailResponseAdapter adapter = new KillMailResponseAdapter(request) {
            @Override
            public void onResponse(ZKillMail killMail) {
                if (null == listener) {
                    return;
                }

                if (null == killMail) {
                    listener.onKillMail(null);
                    return;
                }

                killMail.setSolarSystemName(eve.getLocationName(killMail.getSolarSystemID()));

                final Map<Long, KillMailItem> grouped = new ArrayMap<>();
                for (KillMailItem item: killMail.getItems()) {
                    final KillMailItem g = grouped.get(item.getTypeID());
                    if (null == g) {
                        item.setTypeName(eve.getItemName(item.getTypeID()));
                        grouped.put(item.getTypeID(), item);
                    }
                    else {
                        g.setDropped(g.getDropped() + item.getDropped());
                        g.setDestroyed(g.getDestroyed() + item.getDestroyed());
                    }
                }

                final List<KillMailItem> items = new ArrayList<>(grouped.size());
                items.addAll(grouped.values());
                Collections.sort(items, (lhs, rhs) -> {
                    if (StringUtils.isBlank(lhs.getTypeName())) {
                        return -1;
                    }
                    if (StringUtils.isBlank(rhs.getTypeName())) {
                        return 1;
                    }
                    return lhs.getTypeName().compareTo(rhs.getTypeName());
                });
                killMail.setItems(items);

                for (KillMailAttacker a: killMail.getAttackers()) {
                    a.setShipTypeName(eve.getItemName(a.getShipTypeID()));
                    a.setWeaponTypeName(eve.getItemName(a.getWeaponTypeID()));
                }

                final KillMailVictim victim = killMail.getVictim();
                victim.setShipTypeName(eve.getItemName(victim.getShipTypeID()));
                listener.onKillMail(killMail);
            }
        };

        EveAPIServicePreferences preferences = new EveAPIServicePreferences(this.context);
        final StringRequest r = new StringRequest(
                Method.GET,
                request.toURI(preferences.getZKillboardURL()),
                adapter,
                adapter);
        r.setTag(TAG);
        this.volley.add(r);
    }
}
