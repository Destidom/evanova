package com.tlabs.android.jeeves.views.killmail;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;

import com.tlabs.eve.api.mail.KillMailAttacker;

import org.apache.commons.lang3.StringUtils;

public class KillMailAttackersWidget extends AbstractListRecyclerView<KillMailAttacker> {

    private static class AttackerHolder extends AbstractListRowHolder<KillMailAttacker> {

        private ImageView iconView;
        private TextView nameView;
        private TextView corporationView;
        private TextView allianceView;
        private TextView damageView;
        private TextView shipView;

        public AttackerHolder(View itemView) {
            super(itemView);
            this.nameView = (TextView)itemView.findViewById(R.id.j_killmail_Character);
            this.corporationView = (TextView)itemView.findViewById(R.id.j_killmail_Corporation);
            this.allianceView = (TextView)itemView.findViewById(R.id.j_killmail_Alliance);
            this.damageView = (TextView)itemView.findViewById(R.id.j_killmail_Damage);
            this.shipView = (TextView)itemView.findViewById(R.id.j_killmail_Ship);
            this.iconView = (ImageView)itemView.findViewById(R.id.j_killmail_CharacterIcon);
        }

        @Override
        public void render(KillMailAttacker a) {
            EveImages.loadCharacterIcon(a.getCharacterID(), iconView);
            nameView.setText(a.getCharacterName());
            if (a.isFinalBlow()) {
                nameView.setTextColor(Color.GREEN);
            }
            else {
                nameView.setTextColor(Color.WHITE);
            }
            corporationView.setText(a.getCorporationName());

            if (a.getShipTypeID() == a.getWeaponTypeID()) {
                shipView.setText(a.getShipTypeName());
            }
            else {
                shipView.setText(a.getShipTypeName() + " " + a.getWeaponTypeName());
            }

            if (StringUtils.isBlank(a.getAllianceName())) {
                allianceView.setVisibility(View.INVISIBLE);
            }
            else {
                allianceView.setVisibility(View.VISIBLE);
                allianceView.setText(a.getAllianceName());
            }

            if (a.getDamageDone() == 0) {
                damageView.setText("");
                shipView.setTextColor(Color.GRAY);
                nameView.setTextColor(Color.GRAY);
                corporationView.setTextColor(Color.GRAY);
                allianceView.setTextColor(Color.GRAY);
            }
            else {
                damageView.setText(EveFormat.Number.LONG(a.getDamageDone()));
                shipView.setTextColor(Color.WHITE);
                nameView.setTextColor(Color.WHITE);
                corporationView.setTextColor(Color.WHITE);
                allianceView.setTextColor(Color.WHITE);
            }
        }
    }

    public KillMailAttackersWidget(Context context) {
        super(context);
    }

    public KillMailAttackersWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KillMailAttackersWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<KillMailAttacker> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AttackerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_killmail_character, parent, false));
    }

    @Override
    protected long getItemId(KillMailAttacker item) {
        return item.getCharacterID();
    }
}
