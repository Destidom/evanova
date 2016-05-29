package com.tlabs.android.jeeves.views.skills;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.tlabs.eve.api.character.Certificate;

public class CertificateViewWidget extends FrameLayout implements CertificateWidget {

    public CertificateViewWidget(Context context) {
        super(context);
        init();
    }

    public CertificateViewWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CertificateViewWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setCertificate(Certificate certificate) {

    }

    private void init() {
    }

}
