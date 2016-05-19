package com.tlabs.android.evanova.app.launcher.presenter;

import android.app.Activity;
import android.content.Intent;

import com.tlabs.android.evanova.app.dashboard.ui.DashboardActivity;
import com.tlabs.android.evanova.app.launcher.ui.LauncherActivity;
import com.tlabs.android.evanova.app.launcher.LauncherUseCase;
import com.tlabs.android.evanova.app.launcher.LauncherView;
import com.tlabs.android.evanova.mvp.ViewPresenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Observer;

public class LauncherPresenter extends ViewPresenter<LauncherView> {

    private static final Logger LOG = LoggerFactory.getLogger(LauncherPresenter.class);

    private LauncherUseCase useCase;

    @Inject
    public LauncherPresenter(LauncherUseCase useCase) {
        this.useCase = useCase;
    }

    public void startLaunch(final Activity activity) {
        this.useCase.runOperations(new Observer<LauncherUseCase.Operation>() {
            @Override
            public void onCompleted() {
                finish();
            }

            @Override
            public void onError(Throwable e) {
                //getView().showError(e);
                LOG.error(e.getLocalizedMessage(), e);
                finish();

            }

            @Override
            public void onNext(LauncherUseCase.Operation operation) {

            }

            private void finish() {
                Intent intent = new Intent(activity, DashboardActivity.class);
                // needed to clear the back stack and make sure we get back to the screen
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    public void startUri(final Activity activity) {
        Intent intent = LauncherSupport.getLaunchIntent(activity);
        if (null == intent) {
            intent = new Intent(activity, LauncherActivity.class);
        }
        activity.startActivity(intent);
        activity.finish();
    }
}
