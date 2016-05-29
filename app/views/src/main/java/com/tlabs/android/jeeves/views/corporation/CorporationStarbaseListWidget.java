package com.tlabs.android.jeeves.views.corporation;

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
import com.tlabs.eve.api.corporation.Starbase;

public class CorporationStarbaseListWidget extends AbstractListRecyclerView<Starbase>  {

    private static class StarbaseHolder extends AbstractListRowHolder<Starbase> {

        private final TextView stateTextView;
        private final TextView stateDescriptionView;
        private final TextView locationView;
        private final TextView ownerView;
        private final TextView typeView;

        public StarbaseHolder(final View view) {
            super(view);
            this.stateTextView = findView(R.id.rowStarbaseStateText);
            this.stateDescriptionView = findView(R.id.rowStarbaseStateDescription);
            this.locationView = findView(R.id.rowStarbaseLocation);
            this.ownerView = findView(R.id.rowStarbaseOwner);
            this.typeView = findView(R.id.rowStarbaseType);
        }

        @Override
        public void render(final Starbase starbase) {
            this.locationView.setText(starbase.getLocationName());
            this.ownerView.setText(starbase.getStandingOwnerName());
            this.typeView.setText(starbase.getTypeName());

            switch (starbase.getState()) {
                //This is actually a little tricky in some cases.
                case ANCHORED:
                    renderAnchored(starbase);
                    break;
                case ONLINE:
                    renderOnline(starbase);
                    break;
                case ONLINING:
                    final long left = starbase.getOnlineTimestamp() - System.currentTimeMillis();
                    if (left <= 0) {
                        renderOnline(starbase);
                    } else {
                        renderOnlining(starbase);
                    }
                    break;
                case REINFORCED:
                    final long right = starbase.getStateTimestamp() - System.currentTimeMillis();
                    if (right <= 0) {
                        renderOffline(starbase);
                    } else {
                        renderReinforced(starbase);
                    }
                    break;
                case UNANCHORED:
                    renderOffline(starbase);
                    break;
            }
        }

        private void renderAnchored(final Starbase starbase) {
            this.stateTextView.setTextColor(Color.GRAY);
            this.stateTextView.setText(R.string.jeeves_corporation_starbase_status_anchored);
            this.stateDescriptionView.setVisibility(View.GONE);
        }

        private void renderOnline(final Starbase starbase) {
            this.stateTextView.setTextColor(Color.WHITE);
            this.stateTextView.setText(R.string.jeeves_corporation_starbase_status_online);
            this.stateDescriptionView.setVisibility(View.VISIBLE);

            final long online = System.currentTimeMillis() - starbase.getOnlineTimestamp();
            this.stateDescriptionView.setText(EveFormat.Duration.MEDIUM(online));
        }

        private void renderOnlining(final Starbase starbase) {
            this.stateTextView.setTextColor(Color.GREEN);
            this.stateTextView.setText(R.string.jeeves_corporation_starbase_status_onlining);
            this.stateDescriptionView.setVisibility(View.VISIBLE);

            final long left = starbase.getOnlineTimestamp() - System.currentTimeMillis();
            this.stateDescriptionView.setText(EveFormat.Duration.MEDIUM(left));
        }

        private void renderOffline(final Starbase starbase) {
            this.stateTextView.setText(R.string.jeeves_corporation_starbase_status_offline);
            this.stateTextView.setTextColor(Color.RED);
            this.stateDescriptionView.setVisibility(View.GONE);
        }

        private void renderReinforced(final Starbase starbase) {
            this.stateTextView.setText(R.string.jeeves_corporation_starbase_status_reinforced);
            this.stateTextView.setTextColor(Color.YELLOW);
            this.stateDescriptionView.setVisibility(View.VISIBLE);

            final long left = starbase.getStateTimestamp() - System.currentTimeMillis();
            final int percent = (int) (left / (24L * 3600L * 1000L) * 100L);
            this.stateDescriptionView.setText(EveFormat.Duration.MEDIUM(left) + " (" + percent + "%)");
        }
    }

    public CorporationStarbaseListWidget(Context context) {
        super(context);
    }

    public CorporationStarbaseListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CorporationStarbaseListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<Starbase> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StarbaseHolder(LayoutInflater.from(getContext()).inflate(R.layout.row_corp_starbase, parent, false));
    }

    @Override
    protected long getItemId(Starbase item) {
        return item.getItemID() << 32 & item.getLocationID();
    }
}
