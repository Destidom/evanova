package com.tlabs.android.jeeves.views.mails;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.api.mail.Message;

public class MessageListWidget<T extends Message> extends AbstractListRecyclerView<T> {

    private static class MessageHolder<M extends Message> extends AbstractListRowHolder<M> {

        private final TextView titleView;
        private final TextView senderView;
        private final TextView dateView;

        private MessageHolder(final View view) {
            super(view);
            this.titleView = findView(R.id.mailTitle);
            this.senderView = findView(R.id.mailSenderText);
            this.dateView = findView(R.id.mailDateText);
        }

        @Override
        public void render(final Message message) {

            this.titleView.setText(message.getTitle());
            if (message.getRead()) {
                this.titleView.setTextColor(Color.GRAY);
            }
            else {
                this.titleView.setTextColor(Color.WHITE);
            }
            this.senderView.setText(message.getSenderName());
            this.dateView.setText(EveFormat.DateTime.SHORT.format(message.getSentDate()));
        }
    }

    public MessageListWidget(Context context) {
        super(context);
    }

    public MessageListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageHolder(LayoutInflater.from(getContext()).inflate(R.layout.row_mailbox, parent, false));
    }

    @Override
    protected long getItemId(Message item) {
        return item.getMessageID();
    }
}
