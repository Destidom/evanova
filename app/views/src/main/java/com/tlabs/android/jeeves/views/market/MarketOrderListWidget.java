package com.tlabs.android.jeeves.views.market;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.api.MarketOrder;

//I18N
public class MarketOrderListWidget extends AbstractListRecyclerView<MarketOrder> {

    static class MarketOrderHolder extends AbstractListRowHolder<MarketOrder> {

        private MarketOrderHolder(View view) {
            super(view);
            this.crestImage.setVisibility(GONE);
        }

        //I18N
        @Override
        public void render(MarketOrder order) {
            EveImages.loadItemIcon(order.getItemID(), this.portraitImage);

            final long now = System.currentTimeMillis();

            text1.setText(order.getItemName());

            long initialVolume = order.getInitialVolume();
            long remainingVolume = order.getRemainingVolume();

            float ratio = ((float)remainingVolume / (float)initialVolume);
            text2.setText(remainingVolume + "/" + initialVolume);
            if (ratio == 1f) {
                text2.setTextColor(Color.WHITE);
                text1.setTextColor(Color.WHITE);
            }
            else if (ratio <= 0.25f) {
                text2.setTextColor(Color.GREEN);
                text1.setTextColor(Color.GREEN);
            }
            else {
                text2.setTextColor(Color.YELLOW);
                text1.setTextColor(Color.YELLOW);
            }

            long remainingTime = order.getEndDate() - now;
            if (remainingTime <= 0) {
                text3.setText("Order expired");
                text3.setTextColor(Color.GRAY);
                text1.setTextColor(Color.GRAY);
            }
            else {
                text3.setText("Expires in " + EveFormat.Duration.MEDIUM(remainingTime));
                text3.setTextColor(Color.WHITE);
            }

            float price = order.getPrice();
            float totalPrice = price * initialVolume;
            float soldPrice = price * (initialVolume - remainingVolume);

            if (soldPrice == 0f) {
                text4.setText(" - /" + EveFormat.Currency.MEDIUM(totalPrice, false) + " ISK");
            }
            else {
                text4.setText(EveFormat.Currency.LONG(soldPrice, false) + "/" + EveFormat.Currency.MEDIUM(totalPrice, false) + " ISK");
            }

            text5.setText(order.getStationName());
        }
    }

    public MarketOrderListWidget(Context context) {
        super(context);
    }

    public MarketOrderListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarketOrderListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<MarketOrder> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MarketOrderHolder(LayoutInflater.from(getContext()).inflate(R.layout.jeeves_row_character, parent, false));
    }

    @Override
    protected long getItemId(MarketOrder item) {
        return item.getOrderID();
    }
}
