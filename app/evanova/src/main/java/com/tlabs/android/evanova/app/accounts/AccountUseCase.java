package com.tlabs.android.evanova.app.accounts;

import com.tlabs.android.jeeves.model.EveAccount;

import java.util.List;

import rx.Observer;

public interface AccountUseCase {

    List<EveAccount> loadAccounts();

    void importAuthCode(String authCode, Observer<EveAccount> observer);

    void importApiKey(long apiId, String apiKey, String name, Observer<EveAccount> observer);

    void reloadAccount(final long id, Observer<EveAccount> observer);

    void deleteAccount(final long id, Observer<EveAccount> observer);
}
