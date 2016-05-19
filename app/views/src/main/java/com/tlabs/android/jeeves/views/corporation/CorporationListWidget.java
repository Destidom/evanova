package com.tlabs.android.jeeves.views.corporation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.jeeves.model.EveCorporation;
import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;

import org.apache.commons.lang.StringUtils;

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
                text2.setText(String.format(sMemberCount, memberCount));
            }
            else {
                text2.setText(R.string.jeeves_corporations_members_one);
            }

            final String alliance = corp.getAllianceName();
            if (StringUtils.isBlank(alliance)) {
                text3.setText("");
            }
            else {
                text3.setText(alliance);
            }
            final double balance = corp.getBalance();
            if (balance == 0) {
                text4.setVisibility(View.GONE);
            }
            else {
                text4.setVisibility(View.VISIBLE);
                text4.setText(EveFormat.Currency.LONG(balance));
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
