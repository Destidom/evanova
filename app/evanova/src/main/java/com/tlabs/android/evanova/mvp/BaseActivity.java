package com.tlabs.android.evanova.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.tlabs.android.evanova.R;
import com.tlabs.android.jeeves.views.EveImages;


import org.apache.commons.lang3.StringUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseActivity extends AppCompatActivity implements ActivityView {

    private Unbinder unbind;
    private PresenterLifeCycle lifeCycle = new PresenterLifeCycle();

    private BaseActivityTitle title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        this.title = new BaseActivityTitle(this);
        this.unbind = ButterKnife.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.lifeCycle.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.lifeCycle.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.lifeCycle.initializeLifeCycle(this, this);
        this.lifeCycle.onStart(getIntent());
    }

    @Override
    protected void onDestroy() {
        this.lifeCycle.onDestroy();
        this.unbind.unbind();
        super.onDestroy();
    }

    @Override
    public final void setTitleDescription(CharSequence s) {
        this.title.setDescription(s);
    }

    @Override
    public final void setTitleDescription(int sRes) {
        this.title.setDescription(sRes);
    }

    @Override
    public final void setTitleIcon(int iconRes) {
        this.title.setImage(iconRes);
    }

    @Override
    public final void setTitleIcon(String url) {
        this.title.setImage(url);
    }

    @Override
    public final void showLoading(boolean loading) {
        this.title.setRefreshing(loading);
    }

    @Override
    public final void showMessage(CharSequence s) {
        Snackbar.make(findViewById(R.id.activity_container), s, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public final void showMessage(int sRes) {
        Snackbar.make(findViewById(R.id.activity_container), sRes, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public final void showError(CharSequence s) {
        Snackbar.make(findViewById(R.id.activity_container), s, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public final void showError(int sRes) {
        Snackbar.make(findViewById(R.id.activity_container), sRes, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public final void setBackground(String url) {
        if (StringUtils.isBlank(url)) {
            getWindow().setBackgroundDrawableResource(R.drawable.md_transparent);
        }
        else {
            EveImages.load(url, this);
        }
    }

    protected final void setFragment(final Fragment newFragment) {
        setFragment(newFragment, false);
    }

    protected final void setFragment(final android.app.Fragment newFragment) {
        setFragmentCompat(newFragment, false);
    }

    protected final void stackFragment(final Fragment newFragment) {
        setFragment(newFragment, true);
    }

    protected final void stackFragment(final android.app.Fragment newFragment) {
        setFragmentCompat(newFragment, true);
    }

    private final void setFragmentCompat(final android.app.Fragment newFragment, boolean backstack) {
        if (isCurrentCompat(newFragment)) {
            if (!newFragment.isVisible()) {
                android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.show(newFragment);
                transaction.commit();
            }
            return;
        }

        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_container, newFragment);
        if (backstack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    private final void setFragment(final Fragment newFragment, final boolean backstack) {
        if (isCurrent(newFragment)) {
            if (!newFragment.isVisible()) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.show(newFragment);
                transaction.commit();
            }
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_container, newFragment);
        if (backstack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();

    }

    private boolean isCurrent(final Fragment f) {
        final Fragment other = getSupportFragmentManager().findFragmentById(R.id.activity_container);
        return other == f;
    }

    private boolean isCurrentCompat(final android.app.Fragment f) {
        final android.app.Fragment other = getFragmentManager().findFragmentById(R.id.activity_container);
        return other == f;
    }

    private final void snackbar(final int text) {
        Snackbar.make(findViewById(R.id.activity_container), text, Snackbar.LENGTH_SHORT).show();
    }

    private final void snackbar(final String text) {
        Snackbar.make(findViewById(R.id.activity_container), text, Snackbar.LENGTH_SHORT).show();
    }
}
