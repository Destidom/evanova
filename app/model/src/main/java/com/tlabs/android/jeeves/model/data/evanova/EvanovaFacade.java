package com.tlabs.android.jeeves.model.data.evanova;


import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.model.EveCorporation;
import com.tlabs.android.jeeves.model.EveTraining;
import com.tlabs.eve.api.AccountBalance;
import com.tlabs.eve.api.Asset;
import com.tlabs.eve.api.Contract;
import com.tlabs.eve.api.ContractBid;
import com.tlabs.eve.api.ContractItem;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.character.CharacterInfo;
import com.tlabs.eve.api.character.CharacterSheet;
import com.tlabs.eve.api.character.SkillInTraining;
import com.tlabs.eve.api.corporation.CorporationSheet;

import java.util.List;
import java.util.Map;

public interface EvanovaFacade {

    List<Long> listAccounts();

    EveAccount getAccount(final long accountID);

    EveAccount saveAccount(final EveAccount account);

    void deleteAccount(final long accountID);

    EveAccount getOwnerAccount(final long ownerID);

    String hitCharacter(final long charID);

    List<Long> getCharacters();

    List<SkillInTraining> getTrainingQueue(final long charID);

    void saveTrainingQueue(long charID, List<SkillInTraining> training);

    List<SkillInTraining> planTraining(final Skill skill, final int level, final EveTraining training);

    void saveTraining(final EveTraining training);

    @Deprecated
    CharacterSheet getCharacterSheet(final long charID);

    EveCharacter getCharacter(long charID, boolean withSkills);

    void updateCharacter(final CharacterInfo info, final CharacterSheet sheet);

    void updateCharacterSortOrder(final List<Long> ids);

    void deleteCharacter(final long charID);

    void saveAssets(final long ownerID, final List<Asset> assets);

    String hitCorporation(final long corpID);

    List<Long> getCorporations();

    //List<Long> getCorporations(final long apiKeyID);

    @Deprecated
    CorporationSheet getCorporationSheet(final long corpID);

    EveCorporation getCorporation(long corpID);

    void updateCorporation(final CorporationSheet sheet);

    void updateCorporationSortOrder(final List<Long> ids);

    void deleteCorporation(final long corpID);

    void updateCorporationBalance(final long corpID, final List<AccountBalance> balance);

    Contract getContract(final long ownerID, final long contractID);

    List<Contract> getContracts(final long ownerID);

    List<Long> getContracts(final long ownerID, final long expiredAfter);

    void updateContract(final long ownerID, final long contractID, final List<ContractItem> items);

    void updateContracts(final long ownerID, final List<Contract> contracts);

    void updateContractBids(final long ownerID, final List<ContractBid> bids);

    Map<Long, Integer> getAssetGroups(final long ownerID, final String group, final long filterType);

    List<Asset> getAssets(final long ownerID, final long groupID, final String group, final long filterType);

    Asset getAsset(long ownerID, long assetID);
}
