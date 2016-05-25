package com.tlabs.android.evanova.app.accounts.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.accounts.AccountUseCase;
import com.tlabs.android.evanova.app.accounts.AccountView;
import com.tlabs.android.evanova.app.accounts.ui.AccountActivity;
import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.views.Strings;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observer;

public class AccountPresenter extends EvanovaActivityPresenter<AccountView> {

    private final AccountUseCase useCase;
    private final Observer<EveAccount> importObserver;

    private final String apiLoginURL;
    private final String crestLoginURL;

    @Inject
    public AccountPresenter(
            Context context,
            AccountUseCase useCase,
            @Named("apiURL") String apiLoginURL,
            @Named("crestURL") String crestLoginURL) {
        super(context);

        this.apiLoginURL = apiLoginURL;
        this.crestLoginURL = crestLoginURL;
        this.useCase = useCase;

        this.importObserver = new Observer<EveAccount>() {
            @Override
            public void onCompleted() {
                getView().showLoading(false);
            }

            @Override
            public void onError(Throwable e) {
                getView().showLoading(false);
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
        setBackgroundDefault();

        loadAccounts();
    }

    public void startWith(final Intent intent) {
        final String authCode = intent.getStringExtra(AccountActivity.EXTRA_AUTH_CODE);
        if (StringUtils.isNotBlank(authCode)) {
            getView().showLoading(true);
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

        getView().showLoading(true);
        useCase.importApiKey(apiId, apiKey, keyName, importObserver);
    }

    public void loadAccounts() {
        getView().showLoading(true);
        subscribe(() -> this.useCase.loadAccounts(), accounts -> {
            getView().showLoading(false);
            getView().setAccounts(accounts);
        });
    }

    public void reloadAccounts(final List<Long> ids) {
        final List<Long> accounts = (ids.size() == 0) ? this.useCase.listAccounts() : ids;
        for (Long id: accounts) {
            getView().showLoading(true);
            useCase.reloadAccount(id, new Observer<EveAccount>() {
                @Override
                public void onCompleted() {
                    getView().showLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    getView().showLoading(false);
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
            getView().showLoading(true);
            useCase.deleteAccount(id, new Observer<EveAccount>() {
                @Override
                public void onCompleted() {
                    getView().showLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    getView().showLoading(false);
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
        AccountSupport.startImportEve(activity, this.apiLoginURL);
    }

    public void startSSOImport(final Activity activity) {
        AccountSupport.startImportSSO(activity, this.crestLoginURL);
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
                    getView().showLoading(true);
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