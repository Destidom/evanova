package com.tlabs.android.evanova.app.launcher.impl;

import android.content.Context;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.launcher.LauncherUseCase;
import com.tlabs.android.util.Environment;
import com.tlabs.android.util.Log;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class DatabaseDumpOperation implements LauncherUseCase.Operation {
	private static final Logger LOG = LoggerFactory.getLogger(DatabaseDumpOperation.class);

	@Override
	public void execute(Context context) {
		if (!Log.D) {
			return;
		}
		if (!Environment.isExternalStorageAvailable(true)) {
			return;
		}

		backupDatabase(context, "eve.db", "eve.db");
		backupDatabase(context, "evanova.db", "evanova.db");
		backupDatabase(context, "cache.db", "cache.db");
		backupDatabase(context, "mail.db", "mail.db");
	}

	@Override
	public boolean runOnce() {
		return false;
	}

	private static void backupDatabase(final Context context, String original, String db) {
		LOG.warn("Backing up {}...", db);
		File originalFile = context.getDatabasePath(original);
		if (!originalFile.exists()) {
			LOG.warn("Database file not found: {} ", db);
			return;
		}

		final String sdroot = Environment.getExternalStorageDirectory() + "/" + context.getResources().getString(R.string.app_dir);

		final File bakfile = new File(sdroot + "/" + db);
		if (bakfile.exists()) {
			bakfile.delete();
		}

		InputStream in = null;
		OutputStream out = null;
		try {
            bakfile.createNewFile();
			in = new BufferedInputStream(new FileInputStream(originalFile));
			out = new BufferedOutputStream(new FileOutputStream(bakfile));

			IOUtils.copy(in, out);
			out.flush();
			LOG.warn("Backup completed {} -> {}", originalFile, bakfile);
		}
		catch (IOException e) {
			LOG.error(e.getLocalizedMessage());
		}
		finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}
}