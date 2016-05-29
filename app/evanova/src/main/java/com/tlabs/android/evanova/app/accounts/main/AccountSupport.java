package com.tlabs.android.evanova.app.accounts.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;

import com.tlabs.android.evanova.R;
import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.model.text.EveAccountParser;

import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

final class AccountSupport {
    private static final Logger LOG = LoggerFactory.getLogger(AccountSupport.class);
    private static final int ACTIVITY_IMPORT_TEXT = 314;
    private static final int ACTIVITY_IMPORT_EVEMON = 315;

    private AccountSupport() {
    }

    public static List<EveAccount> parseResult(
            Context context,
            int requestCode,
            int resultCode,
            Intent intent) {
        if ((null == intent) || (null == intent.getData())) {
            return Collections.emptyList();
        }
        if (requestCode == ACTIVITY_IMPORT_TEXT) {
            return parseTextAccounts(context, intent.getData());
        }
        if (requestCode == ACTIVITY_IMPORT_EVEMON) {
            return parseEveMonAccounts(context, intent.getData());
        }
        return Collections.emptyList();
    }

    public static void startImportSSO(final Activity activity, final String loginUri) {
        final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(loginUri));
        browserIntent.putExtra(Browser.EXTRA_APPLICATION_ID, activity.getPackageName());
        activity.startActivity(browserIntent, new Bundle());
    }

    public static void startImportEve(final Activity activity, final String loginUri) {
        final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(loginUri));
        browserIntent.putExtra(Browser.EXTRA_APPLICATION_ID, activity.getPackageName());
        activity.startActivity(browserIntent, new Bundle());
    }

    public static void startImportText(final Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        activity.startActivityForResult(
                Intent.createChooser(intent, activity.getResources().getString(R.string.select)),
                AccountSupport.ACTIVITY_IMPORT_TEXT);
    }

    public static void startImportEveMon(final Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        activity.startActivityForResult(
                Intent.createChooser(intent, activity.getResources().getString(R.string.select)),
                AccountSupport.ACTIVITY_IMPORT_EVEMON);
    }

    private static List<EveAccount> parseTextAccounts(final Context context, final Uri uri) {
        InputStream in = null;
        try {
            in = context.getContentResolver().openInputStream(uri);
            return EveAccountParser.parseText(in);
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage());
            return Collections.emptyList();
        }
        finally {
            IOUtils.closeQuietly(in);
        }
    }

    private static List<EveAccount> parseEveMonAccounts(final Context context, final Uri uri) {
        InputStream in = null;
        try {
            in = context.getContentResolver().openInputStream(uri);
            return EveAccountParser.parseEveMon(in);
        }
        catch (IOException e) {
            LOG.error(e.getLocalizedMessage());
            return Collections.emptyList();
        }
        finally {
            IOUtils.closeQuietly(in);
        }
    }
}
