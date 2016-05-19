package com.tlabs.android.jeeves.views.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.R;

final class AccountInfoAdapter extends BaseExpandableListAdapter {

    static final class AccountInfoHolder {
        private TextView createdText;
        private TextView logonText;
        private TextView paidText;
        private TextView paidUntilText;

        public AccountInfoHolder(View v) {
            super();
            this.createdText = (TextView)v.findViewById(R.id.j_viewAccessKeyAccountCreatedText);
            this.logonText = (TextView)v.findViewById(R.id.j_viewAccessKeyAccountLogonText);
            this.paidText = (TextView)v.findViewById(R.id.j_viewAccessKeyAccountPaidText);
            this.paidUntilText = (TextView)v.findViewById(R.id.j_viewAccessKeyAccountUntilText);
        }

        private void render(final EveAccount account) {
            if (null == account || account.getCreationDate() == 0) {
                createdText.setText("getAccount status not available.");
                logonText.setVisibility(View.GONE);
                paidText.setVisibility(View.GONE);
                paidUntilText.setVisibility(View.GONE);
            }
            else {
                logonText.setVisibility(View.VISIBLE);
                paidText.setVisibility(View.VISIBLE);
                paidUntilText.setVisibility(View.VISIBLE);
                createdText.setText(r(R.string.jeeves_account_created_on, EveFormat.Date.LONG(account.getCreationDate())));
                logonText.setText(r(
                        R.string.jeeves_account_login_count,
                        account.getLogonCount(),
                        EveFormat.Duration.MEDIUM(account.getLogonMinutes() * 60000l)));

                paidText.setText(r(R.string.jeeves_account_paid_until, EveFormat.Date.LONG(account.getPaidUntil())));

                final long remaining = account.getPaidUntil() - System.currentTimeMillis();
                paidUntilText.setTextColor(EveFormat.getLongDurationColor(remaining));
                paidUntilText.setText(r(R.string.jeeves_account_paid_remaining, EveFormat.Duration.MEDIUM(remaining)));
            }
        }

        public static View create(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.jeeves_views_access_info, null);
            view.setTag(new AccountInfoHolder(view));
            return view;
        }

        public static void render(final View view, final EveAccount account) {
            AccountInfoHolder h = (AccountInfoHolder)view.getTag();
            h.render(account);
        }

        private String r(int id, Object... format) {
            String s = logonText.getResources().getString(id);
            if (null == format || format.length == 0) {
                return s;
            }
            return String.format(s, format);
        }
    }

    private final AccessListAdapter adapter;
    private EveAccount account;

    public AccountInfoAdapter() {
        super();
        this.adapter = new AccessListAdapter();
        this.account = null;
    }

    public void setAccount(EveAccount account) {
        this.account = account;
        this.adapter.setAccount(account);
        notifyDataSetChanged();
    }

    public EveAccount getAccount() {
        return account;
    }

    @Override
    public int getGroupCount() {
        return adapter.getGroupCount() + 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupPosition == 0 ? 0 : adapter.getChildrenCount(groupPosition - 1);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition == 0 ? null : adapter.getGroup(groupPosition - 1);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupPosition == 0 ? null : adapter.getChild(groupPosition - 1, childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition == 0 ? -1 : adapter.getGroupId(groupPosition -1);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition == 0 ? -1 : adapter.getChildId(groupPosition -1, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (groupPosition > 0) {
            return adapter.getGroupView(groupPosition -1, isExpanded, convertView, parent);
        }

        View view = convertView;
        if (null == view) {
            view = AccountInfoHolder.create(parent.getContext());
        }
        AccountInfoHolder.render(view, account);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (groupPosition > 0) {
            return adapter.getChildView(groupPosition -1, childPosition, isLastChild, convertView, parent);
        }
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
