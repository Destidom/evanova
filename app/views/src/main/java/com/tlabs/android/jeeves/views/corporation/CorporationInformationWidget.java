package com.tlabs.android.jeeves.views.corporation;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.tlabs.android.jeeves.model.EveCorporation;
import com.tlabs.android.jeeves.views.EveFormat;

public class CorporationInformationWidget extends FrameLayout implements CorporationWidget {

    private WebView webView;


    public CorporationInformationWidget(Context context) {
        super(context);
        init();
    }

    public CorporationInformationWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CorporationInformationWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setCorporation(EveCorporation corporation) {
        String html = EveFormat.formatHTML(corporation.getDescription());
        webView.loadDataWithBaseURL(
                "evanova://corp",
                html, "text/html", "utf-8", null);
    }

    private void init() {
        this.webView = new WebView(getContext());
        this.webView.setBackgroundColor(Color.TRANSPARENT);
        this.addView(this.webView);
    }

}
