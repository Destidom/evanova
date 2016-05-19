package com.tlabs.android.jeeves.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.tlabs.android.jeeves.model.data.social.MailDatabase;

import java.sql.SQLException;

public final class MailDatabaseOpenHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "mail.db";

    private static final int V27 = 13;
    private static final int DATABASE_VERSION = V27;

    private static MailDatabaseOpenHelper helper;

    private MailDatabase database;

    private MailDatabaseOpenHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        try {
            this.database = new MailDatabase(getConnectionSource());
        }
        catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static MailDatabaseOpenHelper from(final Context context) {
        if (null == helper) {
            helper = new MailDatabaseOpenHelper(context.getApplicationContext());
        }
        return helper;
    }

    public MailDatabase getDatabase() {
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        this.database.onCreate(connectionSource);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        this.database.onUpgrade(connectionSource, oldVersion, newVersion);
    }
}
