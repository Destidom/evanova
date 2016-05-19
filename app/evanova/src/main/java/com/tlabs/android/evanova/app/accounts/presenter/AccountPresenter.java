package com.tlabs.android.evanova.app.accounts.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.accounts.AccountUseCase;
import com.tlabs.android.evanova.app.accounts.AccountView;
import com.tlabs.android.evanova.app.accounts.ui.AccountActivity;
import com.tlabs.android.evanova.mvp.ActivityPresenter;
import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.service.EveAPIServicePreferences;

import com.tlabs.android.jeeves.views.Strings;

import org.apache.commons.lang.StringUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;

public class AccountPresenter extends ActivityPresenter<AccountView> {

    private final AccountUseCase useCase;
    private final Observer<EveAccount> importObserver;

    private final EveAPIServicePreferences apiPreferences;

    @Inject
    public AccountPresenter(Context context, AccountUseCase useCase) {
        super(context);
        this.useCase = useCase;
        this.apiPreferences = new EveAPIServicePreferences(context.getApplicationContext());

        this.importObserver = new Observer<EveAccount>() {
            @Override
            public void onCompleted() {
                getView().setLoading(false);
            }

            @Override
            public void onError(Throwable e) {
                getView().setLoading(false);
                showError(e.getLocalizedMessage());
            }

            @Override
            public void onNext(EveAccount account) {
                if (null != account) {
                    getView().addAccount(account);
                }
            }
        };
    }

    @Override
    public void setView(AccountView view) {
        super.setView(view);
        loadAccounts();
    }

    public void startWith(final Intent intent) {
        final String authCode = intent.getStringExtra(AccountActivity.EXTRA_AUTH_CODE);
        if (StringUtils.isNotBlank(authCode)) {
            getView().setLoading(true);
            useCase.importAuthCode(authCode, importObserver);
            return;
        }

        final long apiId = intent.getLongExtra(AccountActivity.EXTRA_KEY_ID, -1l);
        if (apiId == -1l) {
            return;
        }

        final String apiKey = intent.getStringExtra(AccountActivity.EXTRA_KEY_CODE);
        if (StringUtils.isBlank(apiKey)) {
            return;
        }

        final String keyName = intent.getStringExtra(AccountActivity.EXTRA_KEY_NAME);

        getView().setLoading(true);
        useCase.importApiKey(apiId, apiKey, keyName, importObserver);
    }

    public void loadAccounts() {
        getView().setLoading(true);
        subscribe(() -> this.useCase.loadAccounts(), accounts -> {
            getView().setLoading(false);
            getView().setAccounts(accounts);
        });
    }

    public void reloadAccounts(final List<Long> ids) {
        if (ids.size() == 0) {
            return;
        }

        for (Long id: ids) {
            getView().setLoading(true);
            useCase.reloadAccount(id, new Observer<EveAccount>() {
                @Override
                public void onCompleted() {
                    getView().setLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    getView().setLoading(false);
                }

                @Override
                public void onNext(EveAccount account) {
                    getView().addAccount(account);
                }
            });
        }
    }

    public void deleteAccounts(final List<Long> ids) {
        if (ids.size() == 0) {
            return;
        }
        for (Long id: ids) {
            getView().setLoading(true);
            useCase.deleteAccount(id, new Observer<EveAccount>() {
                @Override
                public void onCompleted() {
                    getView().setLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    getView().setLoading(false);
                }

                @Override
                public void onNext(EveAccount account) {
                    getView().removeAccount(account);
                }
            });
        }
    }

    public void onAccountSelected(final EveAccount account) {
        getView().setAccount(account);
    }

    public void startEveImport(final Activity activity) {
        AccountSupport.startImportEve(activity, apiPreferences.getApiKeyInstallUri());
    }

    public void startSSOImport(final Activity activity) {
        AccountSupport.startImportSSO(activity, apiPreferences.getCrestLogin());
    }

    public void startEveMonImport(final Activity activity) {
        AccountSupport.startImportEveMon(activity);
    }

    public void startTextImport(final Activity activity) {
        AccountSupport.startImportText(activity);
    }

    public void importResult(int requestCode, int resultCode, Intent data) {
        subscribe(
            () -> AccountSupport.parseResult(getContext(), requestCode, resultCode, data),
            accounts -> {
                for (EveAccount account: accounts) {
                    getView().setLoading(true);
                    useCase.importApiKey(
                            Long.parseLong(account.getKeyID()),
                            account.getKeyValue(),
                            account.getName(),
                            importObserver);
                }
                if (accounts.size() == 0) {
                    showMessage(R.string.toast_apikey_import_zero);
                }
                else if (accounts.size() == 1) {
                    showMessage(R.string.toast_apikey_import_one);
                }
                else {
                    showMessage(Strings.r(getContext(), R.string.toast_apikey_import_many, accounts.size()));
                }
            });
    }
}