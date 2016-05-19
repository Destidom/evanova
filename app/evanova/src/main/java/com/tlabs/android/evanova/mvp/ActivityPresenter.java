package com.tlabs.android.evanova.mvp;

import android.content.Context;
import android.content.Intent;

import org.apache.commons.lang3.StringUtils;

public class ActivityPresenter<T extends ActivityView> extends ViewPresenter<T> {

    private final Context context;

    public ActivityPresenter(Context context) {
        this.context = context;
    }

    protected final Context getContext() {
        return this.context;
    }

    protected final boolean startActivity(final Intent intent) {
        if (null == intent) {
            return false;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.context.startActivity(intent);
        return true;
    }

    protected final void setBackground(String url) {
        getView().setBackground(url);
    }

    protected final void setTitle(CharSequence s) {
        getView().setTitle(s);
    }

    protected final void setTitle(int sRes) {
        getView().setTitle(sRes);
    }

    protected final void setTitleDescription(int sRes) {
        getView().setTitleDescription(sRes);
    }

    protected final void setLoading(boolean loading) {
        getView().setLoading(loading);
    }

    protected final void setTitleIcon(String url) {
        getView().setTitleIcon(url);
    }

    protected final void setTitleDescription(CharSequence s) {
        if (null == s) {
            getView().setTitleDescription(null);
        }
        else if (StringUtils.isBlank(s)) {
            getView().setTitleDescription(" ");//take the space
        }
        else {
            getView().setTitleDescription(s);
        }
    }

    protected final void setTitleIcon(int iconRes) {
        getView().setTitleIcon(iconRes);
    }

    protected final void showMessage(int sRes) {
        getView().showMessage(sRes);
    }

    protected final void showError(int sRes) {
        getView().showError(sRes);
    }

    protected final void showMessage(CharSequence s) {
        getView().showMessage(s);
    }

    protected final void showError(CharSequence s) {
        getView().showError(s);
    }
}
