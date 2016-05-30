package com.tlabs.android.jeeves.views.mails;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.eve.api.mail.MailMessage;
import com.tlabs.eve.api.mail.Message;
import com.tlabs.eve.api.mail.NotificationMessage;

public class EveMessageWidget extends FrameLayout implements MailWidget, NotificationWidget {

    private TextView titleView;
    private TextView fromView;
    private TextView dateView;
    private TextView bodyView;

    public EveMessageWidget(Context context) {
        super(context);
        init();
    }

    public EveMessageWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EveMessageWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setMessage(MailMessage message) {
        setMessageImpl(message);
        bodyView.setText(Html.fromHtml(MailFormat.formatMailBody(getContext(), message)));
    }

    @Override
    public void setMessage(NotificationMessage message) {
        setMessageImpl(message);
        bodyView.setText(Html.fromHtml(MailFormat.formatNotificationBody(getContext(), message)));
    }

    private void setMessageImpl(final Message message) {
        titleView.setText(message.getTitle());
        fromView.setText(message.getSenderName());
        dateView.setText(EveFormat.DateTime.LONG(message.getSentDate(), false));
    }

    private void init() {
        inflate(getContext(), R.layout.f_mail, this);

        this.titleView = (TextView)findViewById(R.id.fMailTitle);
        this.fromView = (TextView)findViewById(R.id.fMailFrom);
        this.dateView = (TextView)findViewById(R.id.fMailTime);
        this.bodyView = (TextView)findViewById(R.id.fMailBody);

        //Clickable links
        this.bodyView.setMovementMethod(LinkMovementMethod.getInstance());
        this.bodyView.setTextIsSelectable(true);
    }

}
