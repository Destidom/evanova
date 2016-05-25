/** This code is based on Rosie RosieFragment.java*/

/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tlabs.android.evanova.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends DialogFragment implements ActivityView {

    private Unbinder unbind;

    private PresenterLifeCycle presenterLifeCycle = new PresenterLifeCycle();

    private boolean injected;

    protected abstract View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenterLifeCycle.initializeLifeCycle(this, this);
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        injectDependencies();
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        injectDependencies();
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injectDependencies();
        View view = onCreateFragmentView(inflater, container, savedInstanceState);
        this.unbind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterLifeCycle.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenterLifeCycle.onPause();
    }


    @Override public void onDestroy() {
        super.onDestroy();
        presenterLifeCycle.onDestroy();
    }

    @Override
    public void onDestroyView() {
        this.unbind.unbind();
        super.onDestroyView();
    }

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

    @Override
    public void showSearch() {
        if (getActivity() instanceof ActivityView) {
            ((ActivityView)getActivity()).showSearch();
        }
    }

    private void injectDependencies() {
        if (injected) {
            return;
        }
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity)getActivity()).inject(this);
            injected = true;
        }
    }
}
