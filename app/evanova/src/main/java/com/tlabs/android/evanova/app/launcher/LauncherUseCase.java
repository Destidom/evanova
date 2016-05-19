package com.tlabs.android.evanova.app.launcher;

import android.content.Context;
import android.content.Intent;

import rx.Observer;

public interface LauncherUseCase {

    interface Operation {

        void execute(final Context context);

        boolean runOnce();
    }

    void runOperations(Observer<Operation> observer);

    Intent nextIntent(final Intent intent);
}
