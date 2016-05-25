package com.tlabs.android.jeeves.views.character;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.R;

import org.apache.commons.lang3.StringUtils;

public class CharacterInfoWidget extends FrameLayout implements CharacterWidget {

    public interface Listener {
        void onMoreInfoSelected();
    }

    private TextView balanceText;
    private TextView jumpFatigueText;
    private TextView spText;
    private TextView locationText;
    private TextView securityText;

    private Listener listener;

    public CharacterInfoWidget(Context context) {
        super(context);
        init();
    }

    public CharacterInfoWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CharacterInfoWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setCharacter(EveCharacter character) {
        if (null == character) {
            return;
        }
        balanceText.setText(EveFormat.Currency.LONG(character.getBalance()));

        final long fatigue = character.getJumpFatigue() - System.currentTimeMillis();
        if (fatigue > 0) {
            jumpFatigueText.setText("Jump Fatigue " + EveFormat.Duration.MEDIUM(fatigue));
            jumpFatigueText.setTextColor(Color.WHITE);
        }
        else {
            jumpFatigueText.setText("No Jump Fatigue");
            jumpFatigueText.setTextColor(Color.LTGRAY);
        }

        spText.setText(EveFormat.SkillPoint.LONG(character.getTraining().getSkillPoints()));

        if (StringUtils.isBlank(character.getLocation().getLocationName())) {
            locationText.setText("No location information.");
        }
        else {
            locationText.setText(character.getLocation().getLocationName());
        }

        String status = "Security Status " + EveFormat.Number.FLOAT(character.getSecurityStatus());
        if (character.getSecurityStatus() > -2.0) {

        }
        else if (character.getSecurityStatus() > -2.5) {
            status = status + " - will be attacked in 1.0";
        }
        else if (character.getSecurityStatus() > -3.0) {
            status = status + " - will be attacked in 0.9";
        }
        else if (character.getSecurityStatus() > -3.5) {
            status = status + " - will be attacked in 0.8";
        }
        else if (character.getSecurityStatus() > -4.0) {
            status = status + " - will be attacked in 0.7";
        }
        else if (character.getSecurityStatus() > -4.5) {
            status = status + " - will be attacked in 0.6";
        }
        else {
            status = status + " - will be attacked in 0.5";
        }
        securityText.setText(status);
        securityText.setTextColor(EveFormat.getSecurityStatusColor(character.getSecurityStatus()));
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.jeeves_view_character_info, this);

        this.balanceText = (TextView)findViewById(R.id.j_character_infoBalanceText);
        this.jumpFatigueText = (TextView)findViewById(R.id.j_character_infoJumpFatigueText);
        this.spText = (TextView)findViewById(R.id.j_character_infoSkillPointsText);
        this.locationText = (TextView)findViewById(R.id.j_character_infoLocationText);
        this.securityText = (TextView)findViewById(R.id.j_character_infoSecurityStatusText);

        setOnClickListener(v -> {
            if (null != listener) {
                listener.onMoreInfoSelected();
            }
        });

    }
}
