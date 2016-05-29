package com.tlabs.android.jeeves.views.corporation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.jeeves.model.EveCorporation;
import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.EveImages;

import org.apache.commons.lang3.StringUtils;

public class CorporationMainWidget extends FrameLayout implements CorporationWidget {

    private ImageView ceoImage;
    private TextView ceoNameText;
    private TextView locationText;
    private TextView memberCountText;
    private TextView balanceText;
    private TextView shareText;
    private TextView taxText;
    private TextView tickerText;

    private View bossBox;


    public CorporationMainWidget(Context context) {
        super(context);
        init();
    }

    public CorporationMainWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CorporationMainWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setCorporation(EveCorporation corporation) {
        if (tickerText.getVisibility() == View.VISIBLE) {
            tickerText.setText(corporation.getTicker());
        }

        if (taxText.getVisibility() == View.VISIBLE) {
            taxText.setText(r(R.string.jeeves_corporation_tax_rate, corporation.getTaxRate()));
        }

        if (shareText.getVisibility() == View.VISIBLE) {
            shareText.setText(r(R.string.jeeves_corporation_shares, (int)corporation.getShares()));
        }

        if (balanceText.getVisibility() == View.VISIBLE) {
            if (corporation.getBalance() < 0) {
                balanceText.setText(R.string.jeeves_corporation_balance_unknown);
            }
            else {
                balanceText.setText(r(R.string.jeeves_corporation_balance, EveFormat.Currency.LONG(corporation.getBalance(), true)));
            }
        }

        if (corporation.getMemberCount() == 1) {
            memberCountText.setText(R.string.jeeves_corporation_members_one);
        }
        else {
            memberCountText.setText(r(R.string.jeeves_corporation_members_many, corporation.getMemberCount()));
        }

        if (StringUtils.isBlank(corporation.getStationName())) {
            locationText.setText(R.string.jeeves_corporation_no_office);
        }
        else {
            locationText.setText(corporation.getStationName());
        }

        if (bossBox.getVisibility() != View.VISIBLE) {
            return;
        }

        ceoNameText.setText(corporation.getCeoName());
        EveImages.loadCharacterIcon(corporation.getCeoID(), ceoImage);
    }

    private void init() {
        inflate(getContext(), R.layout.f_corporation_main, this);

        this.ceoImage = findView(R.id.corporationCEOImage);
        this.ceoNameText = findView(R.id.corporationCEOName);
        this.locationText = findView(R.id.corporationLocationText);
        this.memberCountText = findView(R.id.corporationMembersCountText);
        this.balanceText = findView(R.id.corporationBalanceText);
        this.shareText = findView(R.id.corporationShareText);
        this.taxText = findView(R.id.corporationTaxText);
        this.tickerText = findView(R.id.corporationTickerText);

        this.bossBox = findView(R.id.corporationBossSelectionBox);
    }

    private <T extends View> T findView(final int id) {
        return (T)findViewById(id);
    }

    private final String r(int id, Object... format) {
        String s = getResources().getString(id);
        if ((null == format) || format.length == 0) {
            return s;
        }
        return String.format(s, format);
    }

}
