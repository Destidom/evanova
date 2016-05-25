package com.tlabs.android.evanova.app.dashboard.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.tlabs.android.evanova.mvp.BaseFragment;

public final class AboutFragment extends BaseFragment {

	private WebView helpView;
	
	@Override
    public View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
    	this.helpView = new WebView(getContext());
    	this.helpView.setBackgroundColor(Color.TRANSPARENT);

    	return this.helpView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadHelp("about.html");
    }

    public final void loadHelp(String relativeHelpFilename) {
		helpView.stopLoading();		
    	helpView.loadUrl("file:///android_asset/help/" + relativeHelpFilename);
    }

}
