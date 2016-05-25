package com.tlabs.android.jeeves.views.skills;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

import com.tlabs.android.jeeves.views.ui.list.SingleListGroupDisplayAdapter;

public class CertificateRequirementWidget extends ExpandableListView {
    public interface Listener {

        void onCertificateItemSelected(final long id);

    }

    private CertificateRequirementAdapter adapter;
    private CertificateRequirementWidget.Listener listener;

    public CertificateRequirementWidget(Context context) {
        super(context);
        init();
    }

    public CertificateRequirementWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CertificateRequirementWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnGroupExpandListener(new SingleListGroupDisplayAdapter(this));
        setOnChildClickListener((parent, viiew, group, child, id) -> {
            if (null == listener) {
                return false;
            }
            listener.onCertificateItemSelected(id);
            return true;
        });

        this.adapter = new CertificateRequirementAdapter();
        setAdapter(this.adapter);
    }
}
