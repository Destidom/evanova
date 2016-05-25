package com.tlabs.android.jeeves.views.mails;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;

//I18N
public class MailboxListWidget extends AbstractListRecyclerView<MailFacade.Mailbox> {

    private static class MailboxHolder extends AbstractListRowHolder<MailFacade.Mailbox> {

        private final ImageView imageView;
        private final TextView nameView;
        private final TextView totalCountView;
        private final TextView readCountView;

        public MailboxHolder(final View view) {
            super(view);
            this.imageView = findView(R.id.mailsInboxIcon);
            this.nameView = findView(R.id.mailsInboxText);
            this.totalCountView = findView(R.id.mailsInboxTotalCount);
            this.readCountView = findView(R.id.mailsInboxReadCount);
        }

        @Override
        public void render(final MailFacade.Mailbox mailbox) {
            imageView.setImageLevel(1000 + (int)mailbox.getId());
            nameView.setText(mailbox.getName());

            if (mailbox.getTotalCount() == 0) {
                totalCountView.setText("No message.");
                readCountView.setText("");
            }
            else {
                final long unread = mailbox.getTotalCount() - mailbox.getReadCount();
                totalCountView.setText(mailbox.getTotalCount() + " message(s)");
                if (unread == 0) {
                    readCountView.setText("");
                }
                else {
                    readCountView.setText(unread + " unread");
                }
            }
        }
    }

    public MailboxListWidget(Context context) {
        super(context);
    }

    public MailboxListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MailboxListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<MailFacade.Mailbox> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MailboxHolder(LayoutInflater.from(getContext()).inflate(R.layout.row_mailboxes, parent, false));
    }

    @Override
    protected long getItemId(MailFacade.Mailbox item) {
        return item.getId();
    }
}
