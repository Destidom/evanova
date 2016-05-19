package com.tlabs.android.evanova.app.launcher.impl;

import android.content.Context;

import com.tlabs.android.evanova.app.launcher.LauncherUseCase;

import java.io.File;

final class DatabaseInitOperation implements LauncherUseCase.Operation {

	@Override
	public void execute(Context context) {
		deleteDatabase(context, "media.db");
		deleteDatabase(context, "eve-database.db");
	}

	@Override
	public boolean runOnce() {
		return true;
	}


	private static void deleteDatabase(final Context context, String original) {
		File originalFile = context.getDatabasePath(original);
		if (originalFile.exists()) {
			originalFile.delete();
			return;
		}
	}
}