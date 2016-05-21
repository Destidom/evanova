package com.tlabs.android.evanova.preferences;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tlabs.android.util.PreferenceSupport;

import org.apache.commons.lang.StringUtils;
import org.devfleet.dotlan.DotlanJumpOptions;
import org.devfleet.dotlan.DotlanOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SavedPreferences extends PreferenceSupport {
    private static final Logger LOG = LoggerFactory.getLogger(SavedPreferences.class);

    public static final String KEY_TAB = "saved.dashboard.tab";//String
	public static final String KEY_TAB_INFO = "saved.dashboard.tab.info";//String

	private static final String KEY_LAST_ACCOUNT = "saved.accountID";

	private static final String KEY_LAST_SKILL = "saved.skill.id";
	private static final String KEY_SKILL_OPTION = "saved.skill.view";
	private static final String KEY_ORDERS_OPTION = "saved.skill.order";
	
	private static final String KEY_ROUTE_MRU = "saved.routes.mru";
	
	public static final String KEY_CACHE_INVALIDATE = "preferences.cache.invalidate";//boolean
	
	private static final String KEY_SELECTED_CHARACTER_LIST_TYPE = "saved.characterListViewType";

	private static ObjectMapper mapper = new ObjectMapper();

	public SavedPreferences(Context context) {
		super(context);
	}

	public void setLastViewedAccount(long id) {
		Editor editor = getPreferences().edit();
		editor.putLong(KEY_LAST_ACCOUNT, id);
		editor.apply();
	}

	public long getLastViewedAccount() {
		return getPreferences().getLong(KEY_LAST_ACCOUNT, -1L);
	}

	public long getSkill() {
		return getPreferences().getLong(KEY_LAST_SKILL, -1L);
	}
	
	public void setSkillViewOption(int option) {
		getPreferences().
		edit().
		putString(KEY_SKILL_OPTION, "" + option).
		commit();
	}
	
	public int getSkillViewOption(int defaultValue) {
		return getInt(getPreferences(), KEY_SKILL_OPTION, defaultValue);
	}
	
	public void setOrdersViewOption(int option) {
		getPreferences().
		edit().
		putString(KEY_ORDERS_OPTION, "" + option).
		commit();
	}
			
	public String getDashboardTab() {
		return getPreferences().getString(KEY_TAB, null);
	}
	
	public void setDashboardTab(String tabClass) {
		getPreferences().
		edit().
		putString(KEY_TAB, tabClass).
		commit();
	}

	public int getInformationDashboardTab() {
		return getPreferences().getInt(KEY_TAB_INFO, -1);
	}

	public void setDashboardInformationTab(int tab) {
		getPreferences().
				edit().
				putInt(KEY_TAB_INFO, tab).
                commit();
	}

	public void setInvalidateCache() {
	    //pref set to false/true on purpose to have an event firing at any time.
		getPreferences().
        edit().
        putBoolean(KEY_CACHE_INVALIDATE, false).
        apply();
		getPreferences().
        edit().
        putBoolean(KEY_CACHE_INVALIDATE, true).
        apply();
    }

	public void addMostRecentRoute(final DotlanOptions options) {
		List<DotlanOptions> current = getMostRecentRoutes();
        for (DotlanOptions c: current) {
            if (equals(c, options)) {
                return;
            }
        }

		current.add(options);
		setMostRecentRoutes(current);
	}

	public void setMostRecentRoutes(final List<DotlanOptions> options) {
        try {
            setString(KEY_ROUTE_MRU, mapper.writeValueAsString(options));
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
	}

	public List<DotlanOptions> getMostRecentRoutes() {
        final String mru = getString(KEY_ROUTE_MRU, null);
        if (StringUtils.isBlank(mru)) {
			final List<DotlanOptions> returned = new ArrayList<>(1);
			returned.add(new DotlanOptions().addWaypoint("Jita").addWaypoint("Amarr"));
            return returned;
        }
        try {
            final List<DotlanJumpOptions> options = mapper.readValue(mru, new TypeReference<List<DotlanJumpOptions>>(){});
            final List<DotlanOptions> returned = new ArrayList<>(options.size());
            for (DotlanJumpOptions j: options) {
                if (StringUtils.isBlank(j.getJumpShip())) {
                    final DotlanOptions route = new DotlanOptions();
                    route.setWaypoints(j.getWaypoints());
                    returned.add(route);
                }
                else {
                    returned.add(j);
                }
            }
            return returned;
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
			final List<DotlanOptions> returned = new ArrayList<>(1);
			returned.add(new DotlanOptions().addWaypoint("Jita").addWaypoint("Amarr"));
			return returned;
        }
    }

    private static boolean equals(final DotlanOptions o1, final DotlanOptions o2) {
        if (o1 instanceof DotlanJumpOptions) {
            if (o2 instanceof DotlanJumpOptions) {
                final DotlanJumpOptions j1 = (DotlanJumpOptions)o1;
                final DotlanJumpOptions j2 = (DotlanJumpOptions)o2;
                return
                        j1.getFrom().equals(j2.getFrom()) &&
                        j1.getTo().equals(j2.getTo()) &&
                        j1.getJumpShip().equals(j2.getJumpShip());
            }
            return false;
        }
        if (o2 instanceof DotlanJumpOptions) {
            return false;
        }
        return o1.getFrom().equals(o2.getFrom()) && o1.getTo().equals(o2.getTo());
    }
}
