package com.tlabs.android.jeeves.views.skills;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.ui.list.SingleListGroupDisplayAdapter;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.character.Certificate;
import com.tlabs.eve.api.character.CertificateTree;

import java.util.Map;

public class CertificateListWidget extends ExpandableListView {
    public interface Listener {

        void onCertificateSelected(final Certificate certificate);

    }

    private CertificateListWidget.Listener listener;

    public CertificateListWidget(Context context) {
        super(context);
        init();
    }

    public CertificateListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CertificateListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public final void setCertificates(final CertificateTree tree, final Map<Long, String> skillGroups) {
        final CertificateTreeAdapter adapter = new CertificateTreeAdapter(tree, skillGroups);
        setAdapter(adapter);
    }

    public final void setCharacter(final EveCharacter character) {
        final CertificateTreeAdapter adapter = (CertificateTreeAdapter)getExpandableListAdapter();
        if (null != adapter) {
            adapter.setCharacter(character);
        }
    }

    private void init() {
        setOnGroupExpandListener(new SingleListGroupDisplayAdapter(this));
        setOnChildClickListener((parent, view, group, child, id) -> {
            if (null == listener) {
                return false;
            }
            listener.onCertificateSelected((Certificate) getExpandableListAdapter().getChild(group, child));
            return true;
        });

    }
}
