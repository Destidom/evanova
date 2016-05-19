package com.tlabs.android.evanova.app.launcher.impl;

import android.content.Context;

import com.tlabs.android.evanova.app.launcher.LauncherUseCase;

import java.io.File;

final class ClearCacheOperation implements LauncherUseCase.Operation {

    @Override
    public void execute(Context context){
        delete(context.getCacheDir());
    }

    @Override
    public boolean runOnce() {
        return true;
    }

    private static void delete(File file) {
        if (null == file) {
            return;
        }
        if (file.isDirectory()) {
            for (File f: file.listFiles()) {
                delete(f);
            }
        }
        file.delete();
    }
}
