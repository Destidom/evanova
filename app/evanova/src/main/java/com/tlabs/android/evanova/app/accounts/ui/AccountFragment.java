package com.tlabs.android.evanova.app.accounts.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.tlabs.android.evanova.mvp.BaseFragment;
import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.views.account.AccountListWidget;
import com.tlabs.android.jeeves.views.account.AccountWidget;

import java.util.Collections;
import java.util.List;

public class AccountFragment extends BaseFragment {

    public interface Listener {
        void onAccountSelected(final EveAccount account);
    }

    private AccountFragment.Listener listener;

    private ViewFlipper flipperView;
    private AccountListWidget listView;
    private AccountWidget accountView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.flipperView = new ViewFlipper(getContext());
        this.listView = new AccountListWidget(getContext());
        this.listView.setListener(new AccountListWidget.Listener() {
            @Override
            public void onItemClicked(EveAccount account) {
                if (null != listener) {
                    listener.onAccountSelected(account);
                }
            }

            @Override
            public void onItemSelected(EveAccount account, boolean selected) {
                if (null != listener) {
                    listener.onAccountSelected(account);
                }
            }
        });
        this.accountView = new AccountWidget(getContext());

        this.flipperView.addView(this.listView);
        this.flipperView.addView(this.accountView);

        return this.flipperView;
    }

    public boolean onBackPressed() {
        if (this.flipperView.getDisplayedChild() == 1) {
            this.flipperView.setDisplayedChild(0);
            return true;
        }
        return false;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setAccount(final EveAccount account) {
        this.accountView.setAccount(account);
        this.flipperView.setDisplayedChild(1);
    }

    public void addAccount(final EveAccount account) {
        this.listView.mergeItem(account);
    }

    public void removeAccount(final EveAccount account) {
        this.listView.removeItem(account.getId());
    }

    public void setAccounts(final List<EveAccount> accounts) {
        this.listView.setItems(accounts);
        this.flipperView.setDisplayedChild(0);
    }

    public List<Long> getSelectedAccounts() {
        if (this.flipperView.getDisplayedChild() == 0) {
            return this.listView.getSelectedItems();
        }
        final EveAccount account = this.accountView.getAccount();
        if (null == account) {
            return Collections.emptyList();
        }
        return Collections.singletonList(account.getId());
    }
}
