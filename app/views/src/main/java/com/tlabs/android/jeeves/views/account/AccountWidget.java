package com.tlabs.android.jeeves.views.account;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;

import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.views.ui.list.SingleListGroupDisplayAdapter;

public class AccountWidget extends FrameLayout {

    private AccountInfoAdapter adapter = new AccountInfoAdapter();

    public AccountWidget(Context context) {
        super(context);
        init();
    }

    public AccountWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AccountWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setAccount(final EveAccount account) {
        this.adapter.setAccount(account);
    }

    public EveAccount getAccount() {
        return this.adapter.getAccount();
    }

    private void init() {
        final ExpandableListView listView = new ExpandableListView(getContext());
        listView.setGroupIndicator(null);
        listView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> true);
        //One opened group at a time...
        listView.setOnGroupExpandListener(new SingleListGroupDisplayAdapter(listView));
        addView(listView);

        listView.setAdapter(this.adapter);
    }


}
