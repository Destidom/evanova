package com.tlabs.android.evanova.app.character;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.jeeves.model.EveCharacter;

public interface CharacterView extends ActivityView {

    void showMainView(final EveCharacter character);

    void showDetails(final EveCharacter character);

    void showTraining(final EveCharacter character);

    void showSkills(final EveCharacter character);

    void showMails(final EveCharacter character);

    void showAssets(final EveCharacter character);

    void showWallet(final EveCharacter character);

    void showMarketOrders(final EveCharacter character);

    void showIndustry(final EveCharacter character);

    void showContracts(final EveCharacter character);

    void showSocial(final EveCharacter character);

    void showCalendar(final EveCharacter character);
}
