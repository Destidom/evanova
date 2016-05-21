package com.tlabs.android.jeeves.model.text;

import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.eve.api.evemon.EveMonAccount;
import com.tlabs.eve.api.evemon.EveMonSettingsParser;
import com.tlabs.eve.api.evemon.EveMonSettingsResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class EveAccountParser {
    private static final Logger LOG = LoggerFactory.getLogger(EveAccountParser.class);
    private EveAccountParser() {}


    public static List<EveAccount> parseEveMon(final InputStream in) throws IOException {
        final List<EveAccount> newKeys = new ArrayList<>();
        EveMonSettingsResponse r = new EveMonSettingsParser().parse(in);
        for (EveMonAccount info: r.getApiKeys()) {
            EveAccount account = new EveAccount();
            account.setKeyID(info.getKeyID());
            account.setKeyValue(info.getKeyValue());

            account.setAccessMask(info.getAccessMask());
            account.setName("EveMon " + info.getKeyID());
            newKeys.add(account);
        }

        LOG.debug("parseEveMon: " + newKeys.size());
        return newKeys;
    }

    public static List<EveAccount> parseText(final InputStream in) throws IOException {
        final List<EveAccount> keys = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty() && !line.startsWith("#")) {// comment
                final EveAccount key = parseTextKey(line);
                if (null == key) {
                    LOG.debug("Invalid line '{}'", line);
                }
                else if (!keys.contains(key)) {
                    keys.add(key);
                }
            }
        }
        return keys;
    }

    private static EveAccount parseTextKey(String line) {
        //api key page version has tabs when you copy/paste
        EveAccount k = parseTextKey(line, "\t");
        if (null == k) {
            k = parseTextKey(line, ":");//Us and Aura
        }
        return k;
    }

    // parses "[username:]<user id>:<key>" or "<user id>:[username:]<key>"
    private static EveAccount parseTextKey(String line, String separatorChar) {
        String[] split = StringUtils.split(line, separatorChar);
        if (null == split) {
            return null;
        }

        String keyID = null;
        String apiKey = null;
        String keyName = null;

        if (split.length == 2) {
            keyID = split[0].trim();
            apiKey = split[1].trim();
        }
        else if (split.length >= 3) {
            //old vs new
            try {
                //api key v2 line copy/paste has id before name
                Long.parseLong(split[0].trim());
                keyID = split[0].trim();
                keyName = split[1].trim();
            }
            catch (NumberFormatException e) {
                //Text files
                keyID = split[1].trim();
                keyName = split[0].trim();
            }
            apiKey = split[2].trim();
        }

        if (!validate(keyID, apiKey)) {
            return null;
        }

        EveAccount key = new EveAccount();
        key.setKeyID(keyID);
        key.setKeyValue(apiKey);
        key.setName(keyName);
        return key;
    }

    private static boolean validate(String keyID, String apiKey) {
        if (StringUtils.isBlank(keyID)) {
            return false;
        }
        try {
            Long.parseLong(keyID);
        }
        catch (NumberFormatException e) {
            return false;
        }

        if (StringUtils.isBlank(apiKey)) {
            return false;
        }
        return true;
    }
}
