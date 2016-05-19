package com.tlabs.android.evanova.mvp;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.tlabs.android.evanova.R;
import com.tlabs.android.jeeves.views.EveImages;


import org.apache.commons.lang.StringUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseActivity extends AppCompatActivity implements ActivityView {

    private Unbinder unbind;
    private BaseActivityTitle title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        this.title = new BaseActivityTitle(this);
        this.unbind = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbind.unbind();
    }

    @Override
    public void setTitleDescription(CharSequence s) {
        this.title.setDescription(s);
    }

    @Override
    public void setTitleDescription(int sRes) {
        this.title.setDescription(sRes);
    }

    @Override
    public void setTitleIcon(int iconRes) {
        this.title.setImage(iconRes);
    }

    @Override
    public void setTitleIcon(String url) {
        this.title.setImage(url);
    }

    @Override
    public void setLoading(boolean loading) {
        this.title.setRefreshing(loading);
    }

    @Override
    public void showMessage(CharSequence s) {
        Snackbar.make(findViewById(R.id.activity2_container), s, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(int sRes) {
        Snackbar.make(findViewById(R.id.activity2_container), sRes, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showError(CharSequence s) {
        Snackbar.make(findViewById(R.id.activity2_container), s, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showError(int sRes) {
        Snackbar.make(findViewById(R.id.activity2_container), sRes, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setBackground(String url) {
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
        transaction.replace(R.id.activity2_container, newFragment);
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
        transaction.replace(R.id.activity2_container, newFragment);
        if (backstack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    private boolean isCurrent(final Fragment f) {
        final Fragment other = getSupportFragmentManager().findFragmentById(R.id.activity2_container);
        return other == f;
    }

    private boolean isCurrentCompat(final android.app.Fragment f) {
        final android.app.Fragment other = getFragmentManager().findFragmentById(R.id.activity2_container);
        return other == f;
    }

    private final void snackbar(final int text) {
        Snackbar.make(findViewById(R.id.activity2_container), text, Snackbar.LENGTH_SHORT).show();
    }

    private final void snackbar(final String text) {
        Snackbar.make(findViewById(R.id.activity2_container), text, Snackbar.LENGTH_SHORT).show();
    }
}
