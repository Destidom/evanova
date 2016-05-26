package com.tlabs.android.evanova.app.characters;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.jeeves.model.EveCharacter;

public interface CharacterActivityView extends ActivityView, CharacterView {

    void showMainView(EveCharacter character);

    void showDetails(EveCharacter character);

    void showTraining(EveCharacter character);

    void showSkills(EveCharacter character);

    void showMails(EveCharacter character);

    void showAssets(EveCharacter character);

    void showWallet(EveCharacter character);

    void showMarketOrders(EveCharacter character);

    void showContracts(EveCharacter character);

    void showIndustry(EveCharacter character);

    void showSocial(EveCharacter character);
    
    void showCalendar(EveCharacter character);
}
