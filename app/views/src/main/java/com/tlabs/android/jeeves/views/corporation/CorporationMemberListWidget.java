package com.tlabs.android.jeeves.views.corporation;

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
import com.tlabs.eve.api.corporation.CorporationMember;

import org.apache.commons.lang3.StringUtils;

//I18N
public class CorporationMemberListWidget extends AbstractListRecyclerView<CorporationMember>  {

    private static class MemberHolder extends AbstractListRowHolder<CorporationMember> {
        public MemberHolder(final View view) {
            super(view);
            this.crestImage.setVisibility(GONE);
            this.text5.setVisibility(GONE);
        }

        @Override
        public void render(final CorporationMember member) {
            EveImages.loadCharacterIcon(member.getCharacterID(), this.portraitImage);
            this.text1.setText(member.getName());

            final String location = member.getLocation();
            if (StringUtils.isBlank(location)) {
                this.text2.setText("Unknown location");
            }
            else {
                this.text2.setText(location);
            }

            final long startDateTime = member.getStartDateTime();
            if (startDateTime <= 0) {
                this.text3.setText("Unknown start date.");
            }
            else {
                final int sinceDays = (int)((System.currentTimeMillis() - startDateTime) / (1000L * 60L * 60L * 24L));
                final String sinceText = "Member since " + EveFormat.Date.MEDIUM(startDateTime) + " (" + sinceDays + " days).";
                this.text3.setText(sinceText);
            }
            final long lastLoginDateTime = member.getLogonDateTime();
            if (lastLoginDateTime <= 0) {
                this.text4.setText("No login information.");
            }
            else {
                final long lastLogoffDateTime = member.getLogoffDateTime();
                if ((lastLogoffDateTime > 0) && (lastLogoffDateTime < lastLoginDateTime)) {
                    this.text4.setText("Online since " + EveFormat.DateTime.MEDIUM(lastLogoffDateTime));
                    this.text4.setTextColor(Color.GREEN);
                }
                else {
                    this.text4.setText("Last logged on " + EveFormat.DateTime.MEDIUM(lastLogoffDateTime));
                    this.text4.setTextColor(Color.GRAY);
                }
            }
        }
    }

    public CorporationMemberListWidget(Context context) {
        super(context);
    }

    public CorporationMemberListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CorporationMemberListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<CorporationMember> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MemberHolder(LayoutInflater.from(getContext()).inflate(R.layout.row_item, parent, false));
    }

    @Override
    protected long getItemId(CorporationMember item) {
        return item.getCharacterID();
    }
}
