package com.tlabs.android.evanova.mvp;


import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.R.id;
import com.tlabs.android.jeeves.views.EveImages;

final class BaseActivityTitle {

    private final Toolbar toolbar;
    private final ProgressBar refreshBar;

    private final AppCompatActivity activity;

    private boolean refreshing = false;

    public BaseActivityTitle(final AppCompatActivity activity) {
        this.activity = activity;
        this.toolbar = (Toolbar) activity.findViewById(id.toolbar);
        activity.setSupportActionBar(this.toolbar);

        this.refreshBar = (ProgressBar) activity.findViewById(id.toolbarProgress);
        this.refreshBar.setVisibility(View.GONE);

        this.toolbar.setNavigationIcon(R.mipmap.ic_action_previous_item);
        this.toolbar.setNavigationOnClickListener(v -> {
            activity.onBackPressed();
        });
    }

    protected final Toolbar getToolbar() {
        return this.toolbar;
    }

    public void setText(int titleRes) {
        this.toolbar.setTitle(titleRes);
    }

    public void setText(CharSequence title) {
        this.toolbar.setTitle(title);
    }

    public void setImage(int imageRes) {
        toolbar.setLogo(imageRes);
    }

    public void setImage(String imageUrl) {
        RX.subscribe(() -> EveImages.getTitleImage(this.toolbar.getContext(), imageUrl),
        bitmap -> {
            if (null != bitmap) {
                //toolbar.setNavigationIcon(new BitmapDrawable(toolbar.getResources(), bitmap));
                //actionBar.setIcon(new BitmapDrawable(toolbar.getResources(), bitmap));
                toolbar.setLogo(new BitmapDrawable(toolbar.getResources(), bitmap));
            }
        });
    }

    public void setDescription(int subtitleRes) {
        this.toolbar.setSubtitle(subtitleRes);
    }

    public void setDescription(CharSequence subtitle) {
        this.toolbar.setSubtitle(subtitle);
    }

    public final void setRefreshing(boolean refreshing) {
        if (this.refreshing == refreshing) {
            return;
        }
        this.refreshing = refreshing;
        this.refreshBar.setVisibility(refreshing ? View.VISIBLE : View.GONE);
    }

    public void invalidate() {
        this.activity.supportInvalidateOptionsMenu();
        this.refreshBar.setVisibility(this.refreshing ? View.VISIBLE : View.GONE);
    }
}