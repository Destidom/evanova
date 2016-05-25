package com.tlabs.android.evanova.app.accounts.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.accounts.AccountModule;
import com.tlabs.android.evanova.app.accounts.AccountView;
import com.tlabs.android.evanova.app.accounts.DaggerAccountComponent;
import com.tlabs.android.evanova.app.accounts.presenter.AccountPresenter;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.model.EveAccount;

import java.util.List;

import javax.inject.Inject;

public class AccountActivity extends BaseActivity implements AccountView {

    public static final String EXTRA_KEY_ID = AccountActivity.class.getName() + ".keyID";//long
    public static final String EXTRA_KEY_CODE = AccountActivity.class.getName() + ".keyCode";
    public static final String EXTRA_KEY_NAME = AccountActivity.class.getName() + ".keyName";

    public static final String EXTRA_AUTH_CODE = AccountActivity.class.getName() + ".authCode";

    @Inject
    @Presenter
    AccountPresenter presenter;

    private AccountFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerAccountComponent
                .builder()
                .applicationComponent(Application.getAppComponent())
                .accountModule(new AccountModule())
                .build()
                .inject(this);

        this.fragment = new AccountFragment();
        this.fragment.setListener(account -> this.presenter.onAccountSelected(account));
        setFragment(this.fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.keys, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete: {
                this.presenter.deleteAccounts(this.fragment.getSelectedAccounts());
                return true;
            }
            case R.id.menu_reload: {
                this.presenter.reloadAccounts(this.fragment.getSelectedAccounts());
                return true;
            }
            case R.id.menu_accounts_add_eve_keys: {
                this.presenter.startEveImport(this);
                return true;
            }
            case R.id.menu_accounts_add_eve_sso: {
                this.presenter.startSSOImport(this);
                return true;
            }

            case R.id.menu_accounts_add_text_file: {
                this.presenter.startTextImport(this);
                return true;
            }
            case R.id.menu_accounts_add_evemon_file: {
                this.presenter.startEveMonImport(this);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.presenter.startWith(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.presenter.startWith(getIntent());
    }

    @Override
    public void onBackPressed() {
        if (this.fragment.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void setAccounts(List<EveAccount> accounts) {
        this.fragment.setAccounts(accounts);
    }

    @Override
    public void setAccount(EveAccount account) {
        this.fragment.setAccount(account);
    }

    @Override
    public void addAccount(EveAccount account) {
        this.fragment.addAccount(account);
    }

    @Override
    public void removeAccount(EveAccount account) {
        this.fragment.removeAccount(account);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.presenter.importResult(requestCode, resultCode, data);
    }
}
