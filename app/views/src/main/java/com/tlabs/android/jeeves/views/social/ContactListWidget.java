package com.tlabs.android.jeeves.views.social;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.api.mail.Contact;

public class ContactListWidget extends AbstractListRecyclerView<Contact> {

    private static class ContactHolder extends ListRecyclerViewAdapter.ViewHolder<Contact> {

        private ImageView portraitImage;

        private ImageView ratingImage;

        private TextView nameView;

        public ContactHolder(View itemView) {
            super(itemView);
            this.portraitImage = (ImageView)itemView.findViewById(R.id.j_rowContactImage);
            this.ratingImage = (ImageView)itemView.findViewById(R.id.j_rowContactRatingImage);
            this.nameView = (TextView)itemView.findViewById(R.id.j_rowContactName);
        }

        @Override
        public void render(Contact contact) {
            this.nameView.setText(contact.getContactName());
            if (contact.getContactTypeID() == Contact.ALLIANCE) {
                EveImages.loadAllianceIcon(contact.getContactID(), this.portraitImage);
            }
            else if (contact.getContactTypeID() == Contact.CORPORATION) {
                EveImages.loadCorporationIcon(contact.getContactID(), this.portraitImage);
            }
            else {
                EveImages.loadCharacterIcon(contact.getContactID(), this.portraitImage);
            }
            if (contact.getStanding() < -5) {
                this.ratingImage.setBackgroundResource(R.drawable.jeeves_bg_standing_terrible);
                this.ratingImage.setImageResource(R.drawable.jeeves_ic_standing_negative);
            }
            else if (contact.getStanding() < 0) {
                this.ratingImage.setBackgroundResource(R.drawable.jeeves_bg_standing_bad);
                this.ratingImage.setImageResource(R.drawable.jeeves_ic_standing_negative);
            }
            else if (contact.getStanding() < 5) {
                this.ratingImage.setBackgroundResource(R.drawable.jeeves_bg_standing_neutral);
                this.ratingImage.setImageResource(R.drawable.jeeves_ic_standing_neutral);
            }
            else if (contact.getStanding() < 10) {
                this.ratingImage.setBackgroundResource(R.drawable.jeeves_bg_standing_good);
                this.ratingImage.setImageResource(R.drawable.jeeves_ic_standing_positive);
            }
            else {
                this.ratingImage.setBackgroundResource(R.drawable.jeeves_bg_standing_excellent);
                this.ratingImage.setImageResource(R.drawable.jeeves_ic_standing_positive);
            }
        }
    }


    public ContactListWidget(Context context) {
        super(context);
    }

    public ContactListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<Contact> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_contact, parent, false));
    }

    @Override
    protected long getItemId(Contact item) {
        return item.getContactID();
    }
}
