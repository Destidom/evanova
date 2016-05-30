package com.tlabs.android.jeeves.views.corporation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.jeeves.model.EveCorporation;
import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.Strings;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;

import org.apache.commons.lang3.StringUtils;

public class CorporationListWidget extends AbstractListRecyclerView<EveCorporation> {

    public static class Listener implements AbstractListRecyclerView.Listener<EveCorporation> {
        @Override
        public void onItemClicked(EveCorporation corporation) {

        }

        @Override
        public void onItemSelected(EveCorporation corporation, boolean selected) {

        }

        @Override
        public void onItemMoved(EveCorporation corporation, int from, int to) {

        }
    }

    private static class CorporationHolder extends AbstractListRowHolder<EveCorporation> {

        public CorporationHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void render(EveCorporation corp) {
            progress.setVisibility(updating ? View.VISIBLE : View.GONE);
            EveImages.loadCorporationIcon(corp.getID(), portraitImage);

            text1.setText(corp.getName());

            final int memberCount = corp.getMemberCount();
            if (memberCount > 1) {
                final String sMemberCount = text2.getResources().getString(R.string.jeeves_corporations_members_many);
                text3.setText(String.format(sMemberCount, memberCount));
            }
            else {
                text3.setText(R.string.jeeves_corporations_members_one);
            }

            final String alliance = corp.getAllianceName();
            if (StringUtils.isBlank(alliance)) {
                Strings.r(text4, R.string.jeeves_corporations_alliance_unavailable);
            }
            else {
                text4.setText(alliance);
            }
            final double balance = corp.getBalance();
            if (balance == 0) {
                Strings.r(text2, R.string.jeeves_corporations_balance_unavailable);
            }
            else {
                text2.setText(EveFormat.Currency.LONG(balance));
            }

            boolean hasSSO = corp.hasCrest();
            boolean hasApi = corp.hasApiKey();
            if (hasSSO && hasApi) {
                this.crestImage.setImageResource(R.drawable.ic_crest_enabled);
                this.crestImage.setVisibility(VISIBLE);
            }
            else if (hasSSO) {
                this.crestImage.setImageResource(R.drawable.ic_crest_disabled);
                this.crestImage.setVisibility(VISIBLE);
            }
            else {
                this.crestImage.setVisibility(INVISIBLE);
            }
        }
    }

    public CorporationListWidget(Context context) {
        super(context);
    }

    public CorporationListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CorporationListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<EveCorporation> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CorporationHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_character, parent, false));

    }

    @Override
    protected long getItemId(EveCorporation item) {
        return item.getID();
    }
}
