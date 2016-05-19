package com.tlabs.android.evanova.app.launcher.impl;

import android.content.Context;

import com.tlabs.android.evanova.app.launcher.LauncherUseCase;
import com.tlabs.android.jeeves.service.EveNotificationService;

final class WakeUpAlarmOperation implements LauncherUseCase.Operation {
    @Override
    public void execute(Context context) {
        EveNotificationService.setAlarms(context, false);
    }

    @Override
    public boolean runOnce() {
        return false;
    }

}