package com.tlabs.android.jeeves.views.corporation;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.eve.api.corporation.Starbase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CorporationStarbaseWidget extends FrameLayout {

    private static final class FuelListAdapter extends BaseAdapter {
        private final Map<Long, Long> fuelVolumes;
        private final Map<Long, String> fuelNames;

        private final List<Long> fuelTypes;

        public FuelListAdapter(final Starbase starbase) {
            this.fuelNames = starbase.getFuelTypes();
            this.fuelVolumes = starbase.getFuelMap();

            this.fuelTypes = new ArrayList<>(this.fuelVolumes.size());
            this.fuelTypes.addAll(this.fuelVolumes.keySet());
            Collections.sort(this.fuelTypes);
        }

        @Override
        public int getCount() {
            return this.fuelTypes.size();
        }

        @Override
        public Object getItem(int position) {
            return fuelTypes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return fuelTypes.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (null == view) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_corp_starbase_fuel, null);
            }

            final long typeID = this.fuelTypes.get(position);
            final TextView fuelNameView = (TextView)view.findViewById(R.id.rowStarbaseFuelTypeName);
            fuelNameView.setText(this.fuelNames.get(typeID));

            final TextView fuelVolumeView = (TextView)view.findViewById(R.id.rowStarbaseFuelTypeVolume);
            fuelVolumeView.setText(this.fuelVolumes.get(typeID) + "");

            return view;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    private TextView stateTextView;
    private TextView stateDescriptionView;

    private ListView fuelListView;
    private TextView nameText;
    private TextView typeText;
    private TextView ownerText;

    private ImageView allowedMembersImage;
    private ImageView allowedAllianceImage;

    public CorporationStarbaseWidget(Context context) {
        super(context);
        init();
    }

    public CorporationStarbaseWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CorporationStarbaseWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setStarbase(final Starbase starbase) {
        if (null == starbase) {
            return;
        }

        fuelListView.setAdapter(new FuelListAdapter(starbase));
        nameText.setText(starbase.getLocationName());
        typeText.setText(starbase.getTypeName());
        ownerText.setText(starbase.getStandingOwnerName());
        allowedMembersImage.setImageLevel(starbase.getAllowCorporationMembers() ? 1 : 0);
        allowedAllianceImage.setImageLevel(starbase.getAllowAllianceMembers() ? 1 : 0);

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
                }
                else {
                    renderOnlining(starbase);
                }
                break;
            case REINFORCED:
                final long right = starbase.getStateTimestamp() - System.currentTimeMillis();
                if (right <= 0) {
                    renderOffline(starbase);
                }
                else {
                    renderReinforced(starbase);
                }
                break;
            case UNANCHORED:
                renderOffline(starbase);
                break;
        }
    }

    private void init() {
        inflate(getContext(), R.layout.row_corp_starbase_details, this);
        this.stateTextView = (TextView)findViewById(R.id.rowStarbaseDetailsStateText);
        this.stateDescriptionView = (TextView)findViewById(R.id.rowStarbaseDetailsStateText);

        this.fuelListView = (ListView)findViewById(R.id.rowStarbaseDetailsFuelList);
        this.nameText = (TextView)findViewById(R.id.rowStarbaseDetailsName);
        this.typeText = (TextView)findViewById(R.id.rowStarbaseDetailsTypeText);
        this.ownerText = (TextView)findViewById(R.id.rowStarbaseDetailsStandingOwner);

        this.allowedMembersImage = (ImageView) findViewById(R.id.rowStarbaseDetailsAllowCorporation);
        this.allowedAllianceImage = (ImageView)findViewById(R.id.rowStarbaseDetailsAllowAlliance);

    }

    private void renderAnchored(final Starbase starbase) {
        stateTextView.setTextColor(Color.GRAY);
        stateTextView.setText(R.string.jeeves_corporation_starbase_status_anchored);
        stateDescriptionView.setVisibility(View.GONE);
    }

    private void renderOnline(final Starbase starbase) {
        stateTextView.setTextColor(Color.WHITE);
        stateTextView.setText(R.string.jeeves_corporation_starbase_status_online);

        stateDescriptionView.setVisibility(View.VISIBLE);
        stateDescriptionView.setTextColor(Color.WHITE);

        final long online = System.currentTimeMillis() - starbase.getOnlineTimestamp();
        stateDescriptionView.setText(EveFormat.Duration.MEDIUM(online));
    }

    private void renderOnlining(final Starbase starbase) {
        stateTextView.setTextColor(Color.GREEN);
        stateTextView.setText(R.string.jeeves_corporation_starbase_status_onlining);
        stateDescriptionView.setVisibility(View.VISIBLE);
        stateDescriptionView.setTextColor(Color.WHITE);

        final long left = starbase.getOnlineTimestamp() - System.currentTimeMillis();
        stateDescriptionView.setText(EveFormat.Duration.MEDIUM(left));
    }

    private void renderOffline(final Starbase starbase) {
        stateTextView.setText(R.string.jeeves_corporation_starbase_status_offline);
        stateTextView.setTextColor(Color.RED);
        stateDescriptionView.setVisibility(View.GONE);
    }

    private void renderReinforced(final Starbase starbase) {
        stateTextView.setText(R.string.jeeves_corporation_starbase_status_reinforced);
        stateTextView.setTextColor(Color.YELLOW);
        stateDescriptionView.setVisibility(View.VISIBLE);
        stateDescriptionView.setTextColor(Color.WHITE);

        final long left = starbase.getStateTimestamp() - System.currentTimeMillis();
        final int percent = (int)(left / (24L * 3600L * 1000L) * 100L);
        stateDescriptionView.setText(EveFormat.Duration.MEDIUM(left) + " (" + percent + "%)");
    }
}
