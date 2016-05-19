package com.tlabs.android.jeeves.model.data.evanova;

import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.model.EveCorporation;
import com.tlabs.android.jeeves.model.EveTraining;
import com.tlabs.android.jeeves.model.data.evanova.entities.AccountEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.AssetEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.CharacterCloneEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.CharacterEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.CharacterSkillEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.CharacterSkillsEntities;
import com.tlabs.android.jeeves.model.data.evanova.entities.ContractBidEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.ContractEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.ContractItemEntity;
import com.tlabs.android.jeeves.model.data.evanova.entities.CorporationEntity;

import com.tlabs.android.jeeves.model.data.sde.EveFacade;
import com.tlabs.eve.api.AccountBalance;
import com.tlabs.eve.api.Asset;
import com.tlabs.eve.api.Contract;
import com.tlabs.eve.api.ContractBid;
import com.tlabs.eve.api.ContractItem;
import com.tlabs.eve.api.EveAPI;
import com.tlabs.eve.api.Item;
import com.tlabs.eve.api.ItemAttribute;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.character.CharacterInfo;
import com.tlabs.eve.api.character.CharacterSheet;
import com.tlabs.eve.api.character.CharacterSkill;
import com.tlabs.eve.api.character.SkillInTraining;
import com.tlabs.eve.api.corporation.CorporationSheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class EvanovaFacadeImpl implements EvanovaFacade {


    private final EvanovaDatabase evanova;
    private final EveFacade eve;

    private final Map<Long, String> flags;

    public EvanovaFacadeImpl(final EvanovaDatabase evanova, final EveFacade eve) {
        this.evanova = evanova;
        this.eve = eve;
        this.flags = this.eve.getInventoryFlags();
    }

    @Override
    public List<Long> listAccounts() {
        return this.evanova.listAccounts();
    }

    @Override
    public EveAccount getAccount(long accountID) {
        return fillAccount(this.evanova.getAccount(accountID));
    }

    @Override
    public EveAccount getOwnerAccount(long ownerID) {
        return fillAccount(this.evanova.getAccountByOwner(ownerID));
    }

    private EveAccount fillAccount(final AccountEntity entity) {
        if (null == entity) {
            return null;
        }

        final EveAccount account =  AccountEntities.map(entity);
        switch (account.getType()) {
            case EveAccount.ACCOUNT:
            case EveAccount.CHARACTER: {
                final CharacterEntity c = this.evanova.getCharacter(account.getOwnerId());
                if (null != c) {
                    account.setShipID(c.getShipTypeID());
                    account.setShipName(c.getShipName());
                }
            }
            break;
            case EveAccount.CORPORATION: {

            }
            break;
            default:
                break;
        }
        return account;
    }

    @Override
    public EveAccount saveAccount(EveAccount account) {
        AccountEntity entity = AccountEntities.map(account);
        this.evanova.updateAccount(entity);
        account.setId(entity.getId());
        return account;
    }

    @Override
    public void deleteAccount(long accountID) {
        this.evanova.deleteAccount(accountID);
    }

    @Override
    public String hitCharacter(long charID) {
        return this.evanova.hitCharacter(charID);
    }

    @Override
    public List<SkillInTraining> getTrainingQueue(long charID) {
        return getTrainingQueue(charID, SkillInTraining.TYPE_QUEUE);
    }

    private List<SkillInTraining> getTrainingQueue(long charID, int status) {
        final List<CharacterSkillEntity> entities = this.evanova.getCharacterTraining(charID, status);
        final List<SkillInTraining> trainings = new ArrayList<>(entities.size());
        for (CharacterSkillEntity e: entities) {
            final Skill skill = this.eve.getSkill(e.getSkillID());
            if (null == skill) {
                continue;
            }
            trainings.add(CharacterSkillsEntities.transform(skill, e));
        }
        return trainings;
    }

    @Override
    public List<Long> getCharacters() {
        return this.evanova.listCharacters();
    }

    @Override
    public CharacterSheet getCharacterSheet(long charID) {
        return CharacterEntities.toCharacterSheet(this.evanova.getCharacter(charID));
    }

    @Override
    public void updateCharacter(CharacterInfo info, CharacterSheet sheet) {
        final CharacterEntity entity = CharacterEntities.transform(info, sheet);
        if (null == entity) {
            return;
        }
        this.evanova.updateCharacter(entity);

        if (null != sheet) {
            this.evanova.updateCharacterSkills(sheet.getCharacterID(), CharacterSkillsEntities.transform(sheet));
            this.evanova.updateCharacterClones(sheet.getCharacterID(), CharacterClonesEntities.transform(sheet));
        }
    }

    @Override
    public void deleteCharacter(long charID) {
        this.evanova.deleteCharacter(charID);
    }

    @Override
    public void saveTrainingQueue(long charID, List<SkillInTraining> training) {
        this.evanova.saveTrainingQueue(charID, CharacterSkillsEntities.transform(charID, training));
    }

    @Override
    public List<SkillInTraining> planTraining(final Skill skill, final int level, final EveTraining training) {
       return training.plan(skill, level, this.eve);
    }

    @Override
    public void saveTraining(final EveTraining training) {
        this.evanova.saveTrainingPlan(
                training.getOwnerID(),
                CharacterSkillsEntities.transform(training.getOwnerID(), training.getAll(SkillInTraining.TYPE_PLAN)));
    }

    @Override
    public void saveAssets(long ownerID, List<Asset> assets) {
        final List<AssetEntity> entities = new ArrayList<>(assets.size());
        for (Asset a: assets) {
            final AssetEntity entity = AssetEntities.transform(this.eve, ownerID, a);
            if (null != entity) {
                entities.add(entity);
            }
        }
        this.evanova.saveAssets(ownerID, entities);
    }

    @Override
    public String hitCorporation(long corpID) {
        return this.evanova.hitCorporation(corpID);
    }

    @Override
    public List<Long> getCorporations() {
        return this.evanova.listCorporations();
    }
/*
    @Override
    public List<Long> getCorporations(long apiKeyID) {
        return this.evanova.listCorporations(apiKeyID);
    }*/

    @Override
    public CorporationSheet getCorporationSheet(long corpID) {
        return CorporationEntities.transform(this.evanova.getCorporation(corpID));
    }

    @Override
    public void updateCorporation(CorporationSheet sheet) {
        this.evanova.updateCorporation(CorporationEntities.transform(sheet));
    }

    @Override
    public void deleteCorporation(long corpID) {
        this.evanova.deleteCorporation(corpID);
    }

    @Override
    public void updateCorporationBalance(long corpID, List<AccountBalance> balance) {
        this.evanova.updateCorporationWallet(corpID, WalletEntities.transform(balance));
    }

    @Override
    public Contract getContract(long ownerID, long contractID) {
        return ContractEntities.transform(this.evanova.getContract(ownerID, contractID));
    }

    @Override
    public List<Contract> getContracts(long ownerID) {
        final List<ContractEntity> entities = this.evanova.getContracts(ownerID);
        final List<Contract> contracts = new ArrayList<>();
        for (ContractEntity e: entities) {
            contracts.add(ContractEntities.transform(e));
        }
        return contracts;
    }

    @Override
    public List<Long> getContracts(long ownerID, final long expiredAfter) {
        return this.evanova.listContracts(ownerID, expiredAfter);
    }

    @Override
    public void updateContract(long ownerID, long contractID, List<ContractItem> items) {
        final List<ContractItemEntity> entities = new ArrayList<>(items.size());
        for (ContractItem c: items) {
            entities.add(ContractEntities.transform(ownerID, contractID, c));
        }
        this.evanova.updateContractItems(ownerID, entities);
    }

    @Override
    public void updateContracts(long ownerID, List<Contract> contracts) {
        final List<ContractEntity> entities = new ArrayList<>(contracts.size());
        for (Contract c: contracts) {
            entities.add(ContractEntities.transform(ownerID, c));
        }
        this.evanova.updateContracts(ownerID, entities);
    }

    @Override
    public void updateContractBids(long ownerID, List<ContractBid> bids) {
        final List<ContractBidEntity> entities = new ArrayList<>(bids.size());
        for (ContractBid c: bids) {
            entities.add(ContractEntities.transform(ownerID, c));
        }
        this.evanova.updateContractBids(ownerID, entities);
    }

    @Override
    public void updateCharacterSortOrder(List<Long> ids) {
        this.evanova.setCharacterSortOrder(ids);
    }

    @Override
    public void updateCorporationSortOrder(List<Long> ids) {
        this.evanova.setCorporationSortOrder(ids);
    }
/*
    @Override
    public void updateAccountSortOrder(List<Long> ids) {
        this.evanova.setAccountSortOrder(ids);
    }
*/
    @Override
    public EveCharacter getCharacter(long charID, boolean withSkills) {
        final CharacterEntity entity = this.evanova.getCharacter(charID);
        if (null == entity) {
            return null;
        }

        final CharacterSheet sheet = CharacterEntities.toCharacterSheet(entity);
        if (withSkills) {
            setSkills(sheet);
        }

        final CharacterInfo info = CharacterEntities.toCharacterInfo(entity);
        final EveCharacter character = new EveCharacter(sheet, info);

        character.setAccessMasks(this.evanova.listAccessMasks(charID));

        character.setBloodline(entity.getBloodLine());
        character.setCorporationJoinedOn(entity.getCorporationJoined());
        character.setLocation(entity.getLocation());
        character.setCorporationRoles(entity.getCorporationRoles());
        character.setCorporationTitles(entity.getCorporationTitles());

        setImplants(character);
        setClones(character);

        setTraining(character);
        return character;
    }

    @Override
    public EveCorporation getCorporation(long corpID) {
        final CorporationEntity entity = this.evanova.getCorporation(corpID);
        if (null == entity) {
            return null;
        }
        final EveCorporation corporation = new EveCorporation(CorporationEntities.transform(entity));
        corporation.setAccessMasks(this.evanova.listAccessMasks(corpID));
        return corporation;
    }

    @Override
    public Map<Long, Integer> getAssetGroups(long ownerID, String type, long filterTypeID) {
        return this.evanova.getAssetGroups(ownerID, type, filterTypeID);
    }

    @Override
    public List<Asset> getAssets(long ownerID, long groupID, String type, long filterTypeID) {
        final List<AssetEntity> assets = this.evanova.getAssetsByGroup(ownerID, type, groupID, filterTypeID);
        final List<Asset> returned = new ArrayList<>(assets.size());

        for (AssetEntity a: assets) {
            final Asset asset = fillAsset(AssetEntities.transform(ownerID, a), ownerID);
            if (null != asset) {
                returned.add(asset);
            }
        }
        return returned;
    }

    @Override
    public Asset getAsset(long ownerID, long assetID) {
        return fillAsset(AssetEntities.transform(ownerID, this.evanova.getAsset(ownerID, assetID)), ownerID);
    }

    private Asset fillAsset(final Asset asset, final long ownerID) {
        if (null == asset) {
            return null;
        }
        final Item item = eve.getItem(asset.getItemID());
        if (null == item) {
            return null;
        }

        asset.setItem(item);
        asset.setInventoryName(item.getName());
        asset.setInventoryFlagName(flags.get((long) asset.getInventoryFlag()));
        if (asset.getItemID() == 27) {
            //Corp hangar locationID is a bit strange and needs conversion
            // see: http://wiki.eve-id.net/APIv2_Corp_AssetList_XML
            long locationID = asset.getLocationID();
            if (locationID >= 66000000 && locationID < 67000000) {
                locationID = locationID - 6000001;
                asset.setLocationID(locationID);
            }
            else if (locationID >= 67000000 && locationID < 68000000) {
                locationID = locationID - 6000000;
                asset.setLocationID(locationID);
            }
        }
        asset.setLocationName(eve.getLocationName(asset.getLocationID()));
        for (AssetEntity child: this.evanova.getChildAsset(ownerID, asset.getAssetID())) {
            final Asset kid = getAsset(ownerID, child.getId());
            if (null != kid) {
                asset.addAsset(kid);
            }
        }

        return asset;
    }

    private void setImplants(EveCharacter character) {
        for (CharacterSheet.Implant implant: character.getImplants()) {
            final Item item = eve.getItem(implant.getTypeID());
            if (null != item) {
                implant.setTypeName(item.getName());
                implant.setDescription(item.getDescription());
            }
            setAttributeBonus(character, implant.getTypeID());
        }
    }

    private void setSkills(final CharacterSheet sheet) {
        final List<CharacterSkillEntity> skills = evanova.getCharacterSkills(sheet.getCharacterID());
        for (CharacterSkillEntity e: skills) {
            final CharacterSkill skill = new CharacterSkill();
            skill.setSkillID(e.getSkillID());
            skill.setSkillLevel(e.getSkillLevel());
            skill.setSkillPoints(e.getEndPoints());
            sheet.addSkill(skill);
        }
    }

    private void setTraining(final EveCharacter character) {
        final long charID = character.getID();
        final List<SkillInTraining> training = new ArrayList<>();
        training.addAll(getTrainingQueue(charID, SkillInTraining.TYPE_QUEUE));
        training.addAll(getTrainingQueue(charID, SkillInTraining.TYPE_PLAN));
        character.setTraining(training);
    }

    private void setAttributeBonus(EveCharacter character, long itemID) {
        final List<ItemAttribute> attrs = eve.getItemAttributes(itemID);
        character.addEnhancerValue(EveAPI.ATTR_CHARISMA, getAttributeValue(attrs, ItemAttribute.BONUS_CHARISMA));
        character.addEnhancerValue(EveAPI.ATTR_INTELLIGENCE, getAttributeValue(attrs, ItemAttribute.BONUS_INTELLIGENCE));
        character.addEnhancerValue(EveAPI.ATTR_MEMORY, getAttributeValue(attrs, ItemAttribute.BONUS_MEMORY));
        character.addEnhancerValue(EveAPI.ATTR_PERCEPTION, getAttributeValue(attrs, ItemAttribute.BONUS_PERCEPTION));
        character.addEnhancerValue(EveAPI.ATTR_WILLPOWER, getAttributeValue(attrs, ItemAttribute.BONUS_WILLPOWER));
    }

    private void setClones(EveCharacter character) {
        final List<CharacterCloneEntity> entities = this.evanova.getCharacterClones(character.getID());
        for (CharacterCloneEntity e: entities) {
            CharacterSheet.JumpClone clone = CharacterClonesEntities.transform(e);
            clone.setLocation(eve.getLocationName(clone.getLocationID()));

            for (CharacterSheet.Implant p: clone.getImplants()) {
                final Item item = eve.getItem(p.getTypeID());
                if (null != item) {
                    p.setDescription(item.getDescription());
                    p.setTypeName(item.getName());
                }
            }
            character.addJumpClone(clone);
        }
    }

    private static int getAttributeValue(final List<ItemAttribute> attributes, final int attributeID) {
        for (ItemAttribute attr: attributes) {
            if (attr.getID() == attributeID) {
                return (int)attr.getValue();
            }
        }
        return 0;
    }
}
