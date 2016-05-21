package com.tlabs.android.jeeves.model.data.cache;

import com.tlabs.android.jeeves.model.data.cache.entities.CacheRequestEntity;
import com.tlabs.android.jeeves.model.data.cache.entities.CacheSovereigntyEntity;
import com.tlabs.android.jeeves.model.data.cache.entities.CacheStationEntity;
import com.tlabs.eve.EveFacade;
import com.tlabs.eve.EveRequest;
import com.tlabs.eve.EveResponse;
import com.tlabs.eve.api.AccessInfoRequest;
import com.tlabs.eve.api.AccountStatusRequest;
import com.tlabs.eve.api.Sovereignty;
import com.tlabs.eve.api.Station;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

final class CacheEntities {
    private static final Logger LOG = LoggerFactory.getLogger(CacheEntities.class);

    private CacheEntities() {}

    public static <T extends EveResponse> T transform(final EveRequest<T> request, final CacheRequestEntity entity) {
        if (null == entity) {
            return null;
        }
        try {
            final T t = parseResponse(request, entity.getContent());
            if (null != t) {
                t.setCached(true);
            }
            return t;
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public static <T extends EveResponse> CacheRequestEntity transform(final EveRequest<T> request, T response) {
        final CacheRequestEntity entity = new CacheRequestEntity();
        entity.setKey(md5(request));
        entity.setExpires(response.getCachedUntil());
        entity.setGenerated(System.currentTimeMillis());
        entity.setContent(response.getContent());
        return entity;
    }

    public static CacheStationEntity transform(final Station station) {
        CacheStationEntity entity = new CacheStationEntity();
        entity.setId(station.getStationID());
        entity.setOwnerID(station.getCorporationID());
        entity.setOwnerName(station.getCorporationName());
        entity.setSolarSystemID(station.getSolarSystemID());
        entity.setStationType(station.getStationTypeID());
        entity.setStationName(station.getStationName());
        return entity;
    }

    public static CacheSovereigntyEntity transform(final Sovereignty sovereignty) {
        CacheSovereigntyEntity entity = new CacheSovereigntyEntity();
        entity.setId(sovereignty.getSolarSystemID());

        entity.setAllianceID(sovereignty.getAllianceID());
        entity.setCorpID(sovereignty.getCorporationID());
        entity.setFactionID(sovereignty.getFactionID());

        return entity;
    }

    public static String md5(final EveRequest<?> request) {
        String key = request.getClass().getSimpleName() + request.getPage();
        Map<String, String> params = request.getParameters();
        for (String p : params.keySet()) {
            if ("vCode".equals(p) || "keyID".equals(p) || "accessToken".equals(p)) {
                if (!(request instanceof AccessInfoRequest) && !(request instanceof AccountStatusRequest)) {
                    continue;
                }
            }
            key = appendKey(key, p + "=" + params.get(p));
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(key.getBytes());

            //We need to make an HEX string out the MD5 hash
            //otherwise some SQL queries may choke on it.
            final byte[] md5 = digest.digest();
            final StringBuilder hexString = new StringBuilder();
            for (byte b : md5) {
                hexString.append(Integer.toHexString(0xFF & b));
            }
            return hexString.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T extends EveResponse> T parseResponse(EveRequest<T> request, byte[] data) throws IOException {
        final long now = System.currentTimeMillis();
        LOG.debug("parsing response of {}", request.getClass().getSimpleName());

        if (ArrayUtils.isEmpty(data)) {
            LOG.warn("{} cache is empty", request.getClass().getSimpleName());
            return null;
        }

        final T t = EveFacade.parse(request, new ByteArrayInputStream(data));
        t.setContent(data);

        final long elapsed = System.currentTimeMillis() - now;
        if (elapsed > 3000) {
            LOG.warn("{} cache parsed in {} ms", request.getClass().getSimpleName(), elapsed);
        }
        else {
            LOG.debug("{} cache parsed in {} ms", request.getClass().getSimpleName(), elapsed);
        }
        return t;
    }

    private static String appendKey(String key, String value) {
        if (StringUtils.isBlank(value)) {
            return key;
        }
        return key + "/" + value;
    }
}