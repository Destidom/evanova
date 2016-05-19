package com.tlabs.android.jeeves.data;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class SearchProvider extends ContentProvider {

    private static final String AUTHORITY = "com.tlabs.android.evanova.search";

    public static final Uri URI = Uri.parse("content://" + AUTHORITY);

    public static final Uri ALL = URI.buildUpon().appendPath("all").build();

    public static final Uri SKILLS = URI.buildUpon().appendPath("skills").build();
    public static final Uri LOCATIONS = URI.buildUpon().appendPath("location").build();
    public static final Uri ITEMS = URI.buildUpon().appendPath("items").build();
    public static final Uri NAMES = URI.buildUpon().appendPath("names").build();
    public static final Uri ASSETS = URI.buildUpon().appendPath("assets").build();
    public static final Uri SHIPS = URI.buildUpon().appendPath("ships").build();
    public static final Uri MODULES = URI.buildUpon().appendPath("modules").build();

    private static final UriMatcher uriMatcher;

    private static final int CONTENT_ALL = 0;
    private static final int CONTENT_ITEMS = 1;
    private static final int CONTENT_LOCATIONS = 2;
    private static final int CONTENT_SKILLS = 3;
    private static final int CONTENT_NAMES = 4;
    private static final int CONTENT_ASSETS = 5;
    private static final int CONTENT_SHIPS = 6;
    private static final int CONTENT_MODULES = 7;

    private static final int SINK = 98;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, "all", CONTENT_ALL);
        uriMatcher.addURI(AUTHORITY, "all/*", CONTENT_ALL);

        uriMatcher.addURI(AUTHORITY, "skills/search_suggest_query", CONTENT_SKILLS);
        uriMatcher.addURI(AUTHORITY, "skills/search_suggest_query/*", CONTENT_SKILLS);

        uriMatcher.addURI(AUTHORITY, "items", CONTENT_ITEMS);
        uriMatcher.addURI(AUTHORITY, "items/*", CONTENT_ITEMS);

        uriMatcher.addURI(AUTHORITY, "ships", CONTENT_SHIPS);
        uriMatcher.addURI(AUTHORITY, "ships/*", CONTENT_SHIPS);

        uriMatcher.addURI(AUTHORITY, "modules", CONTENT_MODULES);
        uriMatcher.addURI(AUTHORITY, "modules/*", CONTENT_MODULES);

        uriMatcher.addURI(AUTHORITY, "names", CONTENT_NAMES);
        uriMatcher.addURI(AUTHORITY, "names/*", CONTENT_NAMES);

        uriMatcher.addURI(AUTHORITY, "assets/search_suggest_query", CONTENT_ASSETS);
        uriMatcher.addURI(AUTHORITY, "assets/search_suggest_query/*", CONTENT_ASSETS);

        uriMatcher.addURI(AUTHORITY, "location/search_suggest_query", CONTENT_LOCATIONS);
        uriMatcher.addURI(AUTHORITY, "location/search_suggest_query/*", CONTENT_LOCATIONS);

    }

    private EveDatabaseOpenHelper eveDatabase;

    @Override
    public boolean onCreate() {
        this.eveDatabase = EveDatabaseOpenHelper.from(getContext().getApplicationContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
        //We do not export our data
        default:
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;

        switch (uriMatcher.match(uri)) {
        case CONTENT_ITEMS: {
            String term = selectionArgs[0];
            term = null == term ? "" : term.trim();
            if (term.length() >= 3) {
                cursor = searchItem(term);
            }
            break;
        }
        case CONTENT_SHIPS: {
            String term = selectionArgs[0];
            term = null == term ? "" : term.trim();
            if (term.length() >= 3) {
                cursor = searchItem(term, 6);
            }
            break;
        }
        case CONTENT_MODULES: {
            String term = selectionArgs[0];
            term = null == term ? "" : term.trim();
            if (term.length() >= 3) {
                cursor = searchItem(term, 7);
            }
            break;
        }

        case CONTENT_SKILLS: {
            String term = uri.getLastPathSegment();
            term = null == term ? "" : term.trim();
            if (term.length() >= 3) {
                cursor = searchSkill(term);
            }
            break;
        }

        case CONTENT_ASSETS: {
            String term = selectionArgs[0];
            term = null == term ? "" : term.trim();
            if (term.length() >= 3) {
                cursor = searchItem(term);
            }
            break;
        }
        case CONTENT_LOCATIONS: {
            String term = uri.getLastPathSegment();
            term = null == term ? "" : term.trim();
            if (term.length() >= 3) {
                cursor = searchLocation(term.trim(), uri.getQueryParameter("status"));
            }
            break;
        }
        case SINK:
        default:
            break;
        }

        return null == cursor ? new MatrixCursor(new String[] { "_id", SearchManager.SUGGEST_COLUMN_TEXT_1 }) : cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException(uri.toString());
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException(uri.toString());
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException(uri.toString());
    }

    public static List<String> searchLocations(final ContentResolver r, final String search, final float maxSecurityStatus) {
        return searchLocations(
                r, LOCATIONS
                    .buildUpon()
                    .appendPath("search_suggest_query")
                    .appendPath(search)
                    .appendQueryParameter("status", Float.toString(maxSecurityStatus))
                    .build());
    }

    public static List<String> searchLocations(final ContentResolver r, final String search) {
        return searchLocations(
                r, LOCATIONS
                    .buildUpon()
                    .appendPath("search_suggest_query")
                    .appendPath(search)
                    .build());
    }

    private static List<String> searchLocations(final ContentResolver r, final Uri uri) {
        final List<String> returned = new ArrayList<>();
        final Cursor c = r.query(uri, null, null, null, null);
        if (c.getCount() == 0) {
            c.close();
            return returned;
        }
        c.moveToFirst();
        while (!c.isAfterLast()) {
            returned.add(c.getString(0));
            c.moveToNext();
        }
        c.close();
        return returned;
    }

    //Totally hackish way of listing jump freighters because I don't have time to get them from SDE
    //it's every item with attribute 861
    private static final String[] JUMPSHIPS = new String[]{
            "Panther",
            "Redeemer",
            "Sin",
            "Widow",
            "Rorqual",
            "Rorqual ORE Development Edition",

            "Archon",
            "Chimera",
            "Nidhoggur",
            "Thanatos",


            "Moros",
            "Moros Interbus Edition",
            "Naglfar",
            "Naglfar Justice Edition",
            "Phoenix",
            "Phoenix Wiyrkomi Edition",
            "Revelation",
            "Revelation Sarum Edition",

            "Anshar",
            "Ark",
            "Nomad",
            "Rhea",

            "Jump Bridge",
            "QA Jump Bridge",
            "Aeon",
            "Hel",
            "Nyx",
            "Revenant",
            "Wyvern",

            "Avatar",
            "Erebus",
            "Leviathan",
            "Ragnarok"
    };

    public static List<String> searchJumpShips(final ContentResolver r, final String search) {
        final List<String> returned = new ArrayList<>(Arrays.asList(JUMPSHIPS));
        CollectionUtils.filter(returned, s -> s.toLowerCase().startsWith(search.toLowerCase()));
        Collections.sort(returned);
        return returned;
    }

    private static final String TABLE_SKILLS = "apiSkillTree";
    private static final String TABLE_LOCATIONS = "jeevesLocations";
    private static final String TABLE_NAMES = "jeevesNames";
    private static final String TABLE_ITEMS = "jeevesItems";

    private static final String querySearchItem =
            "select n.name_id as _id, n.name_id as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA + ", n.name as " + SearchManager.SUGGEST_COLUMN_TEXT_1 +
                    " from " + TABLE_NAMES + " n " +
                    " where n.name_type = 0 and n.name like '%{$1}%' and n.published = 1" +
                    " order by " + SearchManager.SUGGEST_COLUMN_TEXT_1 + " ASC";
    private Cursor searchItem(final String term) {
        SQLiteDatabase db = eveDatabase.getReadableDatabase();
        return db.rawQuery(StringUtils.replace(querySearchItem, "{$1}", term), null);
    }

    private static final String querySearchItemWithCategory =
            "select n.name_id as _id, n.name_id as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA + ", n.name as " + SearchManager.SUGGEST_COLUMN_TEXT_1 +
                    " from " + TABLE_NAMES + " n, " + TABLE_ITEMS + " i " +
                    " where n.name_type = 0 and n.name like '%{$1}%' and i.typeID = n.name_id and i.categoryID = {$2} and n.published = 1 " +
                    " order by " + SearchManager.SUGGEST_COLUMN_TEXT_1 + " ASC";
    private Cursor searchItem(final String term, int categoryId) {
        SQLiteDatabase db = eveDatabase.getReadableDatabase();
        return db.rawQuery(
                StringUtils.replace(StringUtils.replace(querySearchItemWithCategory, "{$1}", term), "{$2}", Long.toString(categoryId)),
                null);
    }

    private Cursor searchSkill(String searchTerm) {
        SQLiteDatabase db = eveDatabase.getReadableDatabase();
        return db.query(TABLE_SKILLS, new String[]{"_id", "_id as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA, "name",
                "name as " + SearchManager.SUGGEST_COLUMN_TEXT_1}, "name like '%" + searchTerm + "%'", null, null, null, "name ASC");
    }

    private Cursor searchLocation(String searchTerm, final String statusParam) {
        //type id 5 is solar systems.
        String query = "typeID = 5 AND name like '%" + searchTerm + "%'";
        if (StringUtils.isNotBlank(statusParam)) {
            query = query + " AND security <= " + statusParam;
        }
        SQLiteDatabase db = eveDatabase.getReadableDatabase();
        return db.query(TABLE_LOCATIONS, new String[]{"name"}, query, null, null, null, "name ASC");
    }
}
