package com.tlabs.android.jeeves.views.killmail;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.eve.api.mail.KillMailVictim;
import com.tlabs.eve.zkb.ZKillMail;

public class KillMailDetailsWidget extends FrameLayout {

    private ImageView iconView;
    private TextView nameView;
    private TextView corporationView;
    private TextView allianceView;
    private TextView damageView;
    private TextView locationView;
    private TextView timeView;
    private TextView valueView;
    private TextView shipView;

    public KillMailDetailsWidget(Context context) {
        super(context);
        init();
    }

    public KillMailDetailsWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KillMailDetailsWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setKillMail(final ZKillMail killMail) {

        locationView.setText(killMail.getSolarSystemName());
        timeView.setText(EveFormat.DateTime.MEDIUM(killMail.getKillTime()));
        valueView.setText(EveFormat.Currency.MEDIUM(killMail.getValue()));

        final KillMailVictim blapped = killMail.getVictim();
        EveImages.loadCharacterIcon(blapped.getCharacterID(), iconView);
        nameView.setText(blapped.getCharacterName());
        corporationView.setText(blapped.getCorporationName());
        allianceView.setText(blapped.getAllianceName());
        shipView.setText(blapped.getShipTypeName());
        damageView.setText(EveFormat.Number.LONG(blapped.getDamageTaken()));
    }

    private void init() {
        inflate(getContext(), R.layout.jeeves_view_killmail_details, this);
        this.nameView = (TextView) findViewById(R.id.j_killmail_Character);
        this.corporationView = (TextView) findViewById(R.id.j_killmail_Corporation);
        this.allianceView = (TextView) findViewById(R.id.j_killmail_Alliance);
        this.damageView = (TextView) findViewById(R.id.j_killmail_Damage);
        this.locationView = (TextView) findViewById(R.id.j_killmail_DetailsLocation);
        this.timeView = (TextView) findViewById(R.id.j_killmail_DetailsTime);
        this.valueView = (TextView) findViewById(R.id.j_killmail_DetailsValue);
        this.shipView = (TextView) findViewById(R.id.j_killmail_Ship);
        this.iconView = (ImageView) findViewById(R.id.j_killmail_CharacterIcon);
    }
}
