package com.tlabs.android.jeeves.views.mails;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.api.mail.KillMailVictim;
import com.tlabs.eve.zkb.ZKillMail;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class KillMailListWidget extends AbstractListRecyclerView<ZKillMail> {

    private class KillMailHolder extends AbstractListRowHolder<ZKillMail> {
        private TextView solarSystemView;
        private TextView shipView;
        private TextView nameView;
        private TextView corporationNameView;
        private TextView dateView;

        private KillMailHolder(View view) {
            super(view);
            this.solarSystemView = (TextView)itemView.findViewById(R.id.j_killmail_row_System);
            this.shipView = (TextView)itemView.findViewById(R.id.j_killmail_row_VictimShip);
            this.nameView = (TextView)itemView.findViewById(R.id.j_killmail_row_VictimName);
            this.corporationNameView = (TextView)itemView.findViewById(R.id.j_killmail_row_VictimCorporation);
            this.dateView = (TextView)itemView.findViewById(R.id.j_killmail_row_DateText);
        }

        @Override
        public void render(final ZKillMail killMail) {
            this.solarSystemView.setText(killMail.getSolarSystemName());
            this.dateView.setText(EveFormat.DateTime.MEDIUM(killMail.getKillTime()));

            final KillMailVictim blapped = killMail.getVictim();
            String name = blapped.getCharacterName();
            if (StringUtils.isBlank(name)) {
                name = blapped.getAllianceName();
            }
            this.nameView.setText(name);
            if (ownerID == blapped.getCharacterID()) {
                this.nameView.setTextColor(Color.RED);
            }
            else if (killMail.getFinalBlow(ownerID)) {
                this.nameView.setTextColor(Color.GREEN);
            }
            else if (ownerID == blapped.getCorporationID()) {
                this.nameView.setTextColor(Color.YELLOW);
            }
            else {
                this.nameView.setTextColor(Color.WHITE);
            }

            this.corporationNameView.setText(blapped.getCorporationName());
            this.shipView.setText(blapped.getShipTypeName());

        }
    }

    private long ownerID = -1;

    public KillMailListWidget(Context context) {
        super(context);
    }

    public KillMailListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KillMailListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<ZKillMail> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new KillMailHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_killmail, parent, false));
    }

    public void setKillMails(final List<ZKillMail> killMails, final long ownerID) {
        this.ownerID = ownerID;
        setItems(killMails);
    }

    @Override
    protected long getItemId(ZKillMail item) {
        return item.getKillID();
    }
}
