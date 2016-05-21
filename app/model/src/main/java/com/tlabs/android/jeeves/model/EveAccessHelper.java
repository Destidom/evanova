package com.tlabs.android.jeeves.model;

import com.tlabs.eve.api.EveAPI;

import org.devfleet.crest.CrestAccess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class EveAccessHelper {

     private EveAccessHelper() {}

    public static boolean hasCharacterCrest(Long mask) {
        return hasCharacterCrest(Collections.singletonList(mask));
    }

    public static boolean hasCharacterCrest(List<Long> masks) {
        for (Long m: masks) {
            if (CrestAccess.hasAnyScope(CrestAccess.CHARACTER_SCOPES, m)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasCorporationCrest(Long mask) {
        return hasCharacterCrest(Collections.singletonList(mask));
    }

    public static boolean hasCorporationCrest(List<Long> masks) {
        for (Long m: masks) {
            if (CrestAccess.hasAnyScope(CrestAccess.CORPORATION_SCOPES, m)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getCrestScopes(Long mask) {
        return getCrestScopes(Collections.singletonList(mask));
    }

    public static List<String> getCrestScopes(List<Long> masks) {
        final List<String> scopes = new ArrayList<>();
        for (Long m: masks) {
            for (String s: CrestAccess.getScope(m)) {
                if (!scopes.contains(s)) {
                    scopes.add(s);
                }
            }
        }
        return scopes;
    }

    public static boolean hasAnyAccess(Long mask, EveAPI.CharacterAccess... access) {
        return hasAnyAccess(Collections.singletonList(mask), access);
    }

    public static boolean hasAnyAccess(List<Long> masks, EveAPI.CharacterAccess... access) {
        if (null == access || access.length == 0) {
            return true;
        }
        for (EveAPI.CharacterAccess a: access) {
            for (long m: masks) {
                if ((a.getAccessMask() & m) == a.getAccessMask()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasAllAccess(Long mask, EveAPI.CharacterAccess... access) {
        return hasAllAccess(Collections.singletonList(mask), access);
    }

    public static boolean hasAllAccess(List<Long> masks, EveAPI.CharacterAccess... access) {
        if (null == access || access.length == 0) {
            return true;
        }
        for (EveAPI.CharacterAccess a: access) {
            boolean hasAccess = false;
            for (long m: masks) {
                if ((a.getAccessMask() & m) == a.getAccessMask()) {
                    hasAccess = true;
                    break;
                }
            }
            if (!hasAccess) {
                return false;
            }
        }
        return true;
    }

}
