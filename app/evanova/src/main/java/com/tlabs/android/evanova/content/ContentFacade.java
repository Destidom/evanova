package com.tlabs.android.evanova.content;

import android.database.Cursor;
import android.net.Uri;

import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.model.EveCorporation;
import com.tlabs.android.jeeves.model.EveMarketGroup;
import com.tlabs.android.jeeves.model.EveTraining;
import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.eve.api.Asset;
import com.tlabs.eve.api.Contract;
import com.tlabs.eve.api.IndustryJob;
import com.tlabs.eve.api.Item;
import com.tlabs.eve.api.ItemAttribute;
import com.tlabs.eve.api.MarketOrder;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.SkillTree;
import com.tlabs.eve.api.Standing;
import com.tlabs.eve.api.WalletJournalEntry;
import com.tlabs.eve.api.WalletTransaction;
import com.tlabs.eve.api.character.CertificateTree;
import com.tlabs.eve.api.character.CharacterCalendar;
import com.tlabs.eve.api.character.CharacterSheet;
import com.tlabs.eve.api.character.PlanetaryColony;
import com.tlabs.eve.api.character.ResearchJob;
import com.tlabs.eve.api.character.SkillInTraining;
import com.tlabs.eve.api.corporation.CorporationMember;
import com.tlabs.eve.api.corporation.Outpost;
import com.tlabs.eve.api.corporation.Starbase;
import com.tlabs.eve.api.mail.Contact;
import com.tlabs.eve.api.mail.MailMessage;
import com.tlabs.eve.api.mail.NotificationMessage;
import com.tlabs.eve.ccp.EveRSSEntry;
import com.tlabs.eve.zkb.ZKillMail;

import java.util.List;
import java.util.Map;

public interface ContentFacade /*extends EveFacade, MailFacade, EvanovaFacade*/ {

    EveAccount getAccount(final long accountId);

    EveAccount getOwnerAccount(final long ownerId);

    void deleteAccount(final long accountId);

    List<Long> listAccounts();

    List<Long> listCharacters();

    EveCharacter getCharacter(final long charID, boolean withSkills);

    CharacterSheet getCharacterSheet(final long charID);

    CharacterCalendar getCharacterCalendar(final long charID);

    List<PlanetaryColony> getCharacterColonies(final long charID);

    List<SkillInTraining> getTrainingQueue(final long charID);


    List<IndustryJob> getCharacterIndustryJobs(final long charID);

    List<ResearchJob> getCharacterResearch(final long charID);

    void deleteCharacter(final long charID);

    List<Long> listCorporations();

    EveCorporation getCorporation(final long corpID);

    List<MarketOrder> getMarketOrders(final long ownerID);

    List<Outpost> getCorporationOutposts(final long corpID);

    List<Starbase> getCorporationStarbases(final long corpID);

    Starbase getCorporationStarbase(final long corpID, final long starbaeID);

    List<IndustryJob> getCorporationIndustryJobs(final long corpID);

    List<CorporationMember> getCorporationMembers(final long corpID);

    void deleteCorporation(final long corpID);

    List<MailFacade.Mailbox> getMailBoxes(final long ownerID);

    MailMessage getMail(final long messageID);

    List<MailMessage> getMails(final long ownerID, final long mailboxID);

    void deleteMail(final long ownerID, final long mailID);

    NotificationMessage getNotification(final long notificationId);

    List<MailFacade.Mailbox> getNotificationBoxes(final long ownerID);

    List<NotificationMessage> getNotifications(final long ownerID, final long mailboxID);

    void deleteNotification(final long ownerID, final long notificationID);

    void setMessageRead(List<Long> messages, boolean read);

    Contract getContract(final long ownerID, final long contractID);

    List<Contract> getContracts(final long ownerID);

    List<String> getContactTypes(final long ownerID);

    List<Contact> getContacts(final long ownerID, final String group);

    Asset getAsset(long ownerID, long assetID);

    List<WalletJournalEntry> getWalletJournal(long ownerID);

    List<WalletJournalEntry> getWalletJournal(long ownerID, int walletID);

    List<WalletTransaction> getWalletTransactions(long ownerID);

    List<WalletTransaction> getWalletTransactions(long ownerID, int walletID);

    Map<String, List<Standing>> getStandings(final long ownerID);

    List<ZKillMail> getKillMails(final long ownerID);

    String getName(final long someoneID);

    String getLocationName(final long locationID);

    String getCategoryName(final long itemID);

    Item getItem(final long itemID);

    String getItemName(final long itemID);

    List<Item> getItemsRequiredFor(final long skillID, final int level);

    List<Skill> getItemRequirements(final long itemID);

    Map<Long, String> getItemGroups(final long categoryID);

    List<ItemAttribute> getItemAttributes(final long itemID);

    List<EveMarketGroup> getMarketGroups(final long marketGroupID);

    List<Item> getMarketItems(final long marketGroupID);

    Skill getSkill(final long skillID);

    Map<Long, String> getSkillGroups();

    CertificateTree getCertificates();

    SkillTree getSkills();

    List<EveRSSEntry> getEveNews();

    Cursor getAssetGroups(final long ownerID, final String type, final long filterItem);

    Cursor getAssets(final long ownerID, final long groupID, final String type, final long filterItem);

    void saveAccountOrder(final List<Long> order);

    void saveCharacterOrder(final List<Long> order);

    void saveCorporationOrder(final List<Long> order);

    List<SkillInTraining> planTraining(final Skill skill, final int level, final EveTraining training);

    List<SkillInTraining> loadTraining(final Uri from);

    void saveTraining(final EveTraining training);


}
