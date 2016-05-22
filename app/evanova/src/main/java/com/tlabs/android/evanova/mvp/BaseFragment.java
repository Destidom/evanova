package com.tlabs.android.evanova.mvp;

import android.support.v4.app.DialogFragment;

public class BaseFragment extends DialogFragment implements ActivityView {

    @Override
    public final void setBackground(String url) {
        if (getActivity() instanceof ActivityView) {
            ((ActivityView)getActivity()).setBackground(url);
        }
    }

    @Override
    public final void setTitle(CharSequence s) {
        if (null != getActivity()) {
            getActivity().setTitle(s);
        }
    }

    @Override
    public final void setTitle(int sRes) {
        if (null != getActivity()) {
            getActivity().setTitle(sRes);
        }
    }

    @Override
    public final void setTitleDescription(CharSequence s) {
        if (getActivity() instanceof ActivityView) {
            ((ActivityView)getActivity()).setTitleDescription(s);
        }
    }

    @Override
    public final void setTitleDescription(int sRes) {
        if (getActivity() instanceof ActivityView) {
            ((ActivityView)getActivity()).setTitleDescription(sRes);
        }
    }

    @Override
    public final void setTitleIcon(int iconRes) {
        if (getActivity() instanceof ActivityView) {
            ((ActivityView)getActivity()).setTitleIcon(iconRes);
        }
    }

    @Override
    public final void setTitleIcon(String url) {
        if (getActivity() instanceof ActivityView) {
            ((ActivityView)getActivity()).setTitleIcon(url);
        }
    }

    @Override
    public final void showLoading(boolean loading) {
        if (getActivity() instanceof ActivityView) {
            ((ActivityView)getActivity()).showLoading(loading);
        }
    }

    @Override
    public final void showMessage(CharSequence s) {
        if (getActivity() instanceof ActivityView) {
            ((ActivityView)getActivity()).showMessage(s);
        }
    }

    @Override
    public final void showMessage(int sRes) {
        if (getActivity() instanceof ActivityView) {
            ((ActivityView)getActivity()).showMessage(sRes);
        }
    }

    @Override
    public final void showError(CharSequence s) {
        if (getActivity() instanceof ActivityView) {
            ((ActivityView)getActivity()).showError(s);
        }
    }

    @Override
    public final void showError(int sRes) {
        if (getActivity() instanceof ActivityView) {
            ((ActivityView)getActivity()).showError(sRes);
        }
    }
}
