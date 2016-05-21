package com.tlabs.android.evanova.content;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.model.EveCorporation;
import com.tlabs.android.jeeves.model.EveMarketGroup;
import com.tlabs.android.jeeves.model.EveTraining;
import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.android.jeeves.model.data.sde.EveFacade;
import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.android.jeeves.service.EveAPIServicePreferences;
import com.tlabs.android.util.Log;
import com.tlabs.eve.api.Agent;
import com.tlabs.eve.api.Asset;
import com.tlabs.eve.api.Contract;
import com.tlabs.eve.api.IndustryJob;
import com.tlabs.eve.api.IndustryJobsResponse;
import com.tlabs.eve.api.Item;
import com.tlabs.eve.api.ItemAttribute;
import com.tlabs.eve.api.MarketOrder;
import com.tlabs.eve.api.MarketOrderResponse;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.Standing;
import com.tlabs.eve.api.StandingsResponse;
import com.tlabs.eve.api.WalletJournalEntry;
import com.tlabs.eve.api.WalletJournalResponse;
import com.tlabs.eve.api.WalletTransaction;
import com.tlabs.eve.api.WalletTransactionsResponse;
import com.tlabs.eve.api.character.CertificateTree;
import com.tlabs.eve.api.character.CharacterCalendar;
import com.tlabs.eve.api.character.CharacterCalendarAttendeesRequest;
import com.tlabs.eve.api.character.CharacterCalendarAttendeesResponse;
import com.tlabs.eve.api.character.CharacterCalendarRequest;
import com.tlabs.eve.api.character.CharacterCalendarResponse;
import com.tlabs.eve.api.character.CharacterIndustryJobsRequest;
import com.tlabs.eve.api.character.CharacterMarketOrderRequest;
import com.tlabs.eve.api.character.CharacterResearchRequest;
import com.tlabs.eve.api.character.CharacterResearchResponse;
import com.tlabs.eve.api.character.CharacterSheet;
import com.tlabs.eve.api.character.CharacterStandingsRequest;
import com.tlabs.eve.api.character.CharacterTrainingQueueResponse;
import com.tlabs.eve.api.character.CharacterWalletJournalRequest;
import com.tlabs.eve.api.character.CharacterWalletTransactionsRequest;
import com.tlabs.eve.api.character.PlanetaryColoniesRequest;
import com.tlabs.eve.api.character.PlanetaryColoniesResponse;
import com.tlabs.eve.api.character.PlanetaryColony;
import com.tlabs.eve.api.character.PlanetaryPinsRequest;
import com.tlabs.eve.api.character.PlanetaryPinsResponse;
import com.tlabs.eve.api.character.ResearchJob;
import com.tlabs.eve.api.character.SkillInTraining;
import com.tlabs.eve.api.corporation.CorporationIndustryJobsRequest;
import com.tlabs.eve.api.corporation.CorporationMarketOrderRequest;
import com.tlabs.eve.api.corporation.CorporationMember;
import com.tlabs.eve.api.corporation.CorporationStandingsRequest;
import com.tlabs.eve.api.corporation.CorporationWalletJournalRequest;
import com.tlabs.eve.api.corporation.CorporationWalletTransactionsRequest;
import com.tlabs.eve.api.corporation.MemberTrackingRequest;
import com.tlabs.eve.api.corporation.MemberTrackingResponse;
import com.tlabs.eve.api.corporation.Outpost;
import com.tlabs.eve.api.corporation.OutpostListRequest;
import com.tlabs.eve.api.corporation.OutpostListResponse;
import com.tlabs.eve.api.corporation.Starbase;
import com.tlabs.eve.api.corporation.StarbaseDetailsRequest;
import com.tlabs.eve.api.corporation.StarbaseDetailsResponse;
import com.tlabs.eve.api.corporation.StarbaseListRequest;
import com.tlabs.eve.api.corporation.StarbaseListResponse;
import com.tlabs.eve.api.evemon.EveMonSkillPlanParser;
import com.tlabs.eve.api.mail.Contact;
import com.tlabs.eve.api.mail.MailMessage;
import com.tlabs.eve.api.mail.NotificationMessage;
import com.tlabs.eve.ccp.EveRSSEntry;
import com.tlabs.eve.ccp.EveRSSRequest;
import com.tlabs.eve.ccp.EveRSSResponse;
import com.tlabs.eve.zkb.ZKillCharacterLogRequest;
import com.tlabs.eve.zkb.ZKillCorporationLogRequest;
import com.tlabs.eve.zkb.ZKillMail;
import com.tlabs.eve.zkb.ZKillResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public final class ContentFacadeImpl implements ContentFacade {

    private final EveFacade eveFacade;
    private final EvanovaFacade evanovaFacade;
    private final MailFacade mailFacade;
    private final CacheFacade cacheFacade;

    private final Context context;

    @Inject
    public ContentFacadeImpl(
            EveFacade eveFacade,
            EvanovaFacade evanovaFacade,
            MailFacade mailFacade,
            CacheFacade cacheFacade,
            Context context) {
        this.eveFacade = eveFacade;
        this.evanovaFacade = evanovaFacade;
        this.mailFacade = mailFacade;
        this.cacheFacade = cacheFacade;
        this.context = context;
    }

    @Override
    public EveAccount getAccount(long accountId) {
        return this.evanovaFacade.getAccount(accountId);
    }

    @Override
    public EveAccount getOwnerAccount(long accountId) {
        return this.evanovaFacade.getOwnerAccount(accountId);
    }

    @Override
    public List<Long> listAccounts() {
        return this.evanovaFacade.listAccounts();
    }

    @Override
    public void deleteCharacter(long charID) {
        evanovaFacade.deleteCharacter(charID);
    }

    @Override
    public void deleteCorporation(long corpID) {
        evanovaFacade.deleteCorporation(corpID);
    }

    @Override
    public void deleteAccount(long accountID) {
        evanovaFacade.deleteAccount(accountID);
    }

    @Override
    public EveCharacter getCharacter(long charID, boolean withSkills) {
        return evanovaFacade.getCharacter(charID, withSkills);
    }

    @Override
    public EveCorporation getCorporation(long corpID) {
       return evanovaFacade.getCorporation(corpID);
    }

    @Override
    public List<MarketOrder> getCharacterMarketOrders(long charID) {
        final CharacterMarketOrderRequest request = new CharacterMarketOrderRequest(Long.toString(charID));
        final MarketOrderResponse response = cacheFacade.getCached(request);
        if (null == response) {
            return Collections.emptyList();
        }
        final List<MarketOrder> orders = new ArrayList<>();
        orders.addAll(response.getBuyOrders());
        orders.addAll(response.getSellOrders());
        for (MarketOrder o: orders) {
            o.setItemName(getItemName(o.getItemID()));
            o.setStationName(getLocationName(o.getStationID()));
        }
        return orders;
    }

    @Override
    public List<MarketOrder> getCorporationMarketOrders(long corpID) {
        final CorporationMarketOrderRequest request = new CorporationMarketOrderRequest(Long.toString(corpID));
        final MarketOrderResponse response = cacheFacade.getCached(request);
        if (null == response) {
            return Collections.emptyList();
        }
        final List<MarketOrder> orders = new ArrayList<>();
        orders.addAll(response.getBuyOrders());
        orders.addAll(response.getSellOrders());
        for (MarketOrder o: orders) {
            o.setItemName(getItemName(o.getItemID()));
            o.setStationName(getLocationName(o.getStationID()));
        }
        return orders;
    }

    @Override
    public List<IndustryJob> getCharacterIndustryJobs(long charID) {
        final CharacterIndustryJobsRequest request = new CharacterIndustryJobsRequest(Long.toString(charID));
        final IndustryJobsResponse response = cacheFacade.getCached(request);
        if (null == response) {
            return Collections.emptyList();
        }
        return fillJobs(response.getJobs());
    }

    @Override
    public List<ResearchJob> getCharacterResearch(long charID) {
        final CharacterResearchRequest request = new CharacterResearchRequest(Long.toString(charID));
        final CharacterResearchResponse response = cacheFacade.getCached(request);
        if (null == response) {
            return Collections.emptyList();
        }

        for (ResearchJob j: response.getJobs()) {
            final Agent agent = eveFacade.getAgent(j.getAgentID());
            if (null != agent) {
                j.setAgentName(agent.getName());
                j.setLocationName(getLocationName(agent.getLocationID()));
                j.setAgentLevel(agent.getLevel());
                j.setSkillTypeName(eveFacade.getSkillName(j.getSkillTypeID()));
            }
        }
        return response.getJobs();
    }

    @Override
    public List<PlanetaryColony> getCharacterColonies(long charID) {
        final PlanetaryColoniesRequest request = new PlanetaryColoniesRequest(Long.toString(charID));
        final PlanetaryColoniesResponse response = cacheFacade.getCached(request);
        if (null == response) {
            return Collections.emptyList();
        }
        return fillColonies(response.getColonies());
    }

    private List<PlanetaryColony> fillColonies(final List<PlanetaryColony> colonies) {
        for (PlanetaryColony c: colonies) {
            PlanetaryPinsRequest pr = new PlanetaryPinsRequest(Long.toString(c.getOwnerID()), Long.toString(c.getPlanetID()));
            PlanetaryPinsResponse ps = cacheFacade.getCached(pr);
            if (null != ps) {
                c.setPins(ps.getPins());
            }
        }
        return colonies;
    }
    
    @Override
    public List<WalletTransaction> getWalletTransactions(long charID) {
        final CharacterWalletTransactionsRequest request = new CharacterWalletTransactionsRequest(Long.toString(charID));
        final WalletTransactionsResponse response = cacheFacade.getCached(request);
        if (null == response) {
            return Collections.emptyList();
        }
        return response.getTransactions();
    }

    @Override
    public List<WalletTransaction> getWalletTransactions(long corporationID, int walletID) {
        final CorporationWalletTransactionsRequest request = new CorporationWalletTransactionsRequest(Long.toString(corporationID), walletID);
        final WalletTransactionsResponse response = cacheFacade.getCached(request);
        if (null == response) {
            return Collections.emptyList();
        }
        return response.getTransactions();
    }

    @Override
    public List<WalletJournalEntry> getWalletJournal(long charID) {
        final CharacterWalletJournalRequest request = new CharacterWalletJournalRequest(Long.toString(charID));
        final WalletJournalResponse response = cacheFacade.getCached(request);
        if (null == response) {
            return Collections.emptyList();
        }

        return fill(response.getTransactions());
    }

    @Override
    public List<WalletJournalEntry> getWalletJournal(long corporationID, int walletID) {
        final CorporationWalletJournalRequest request = new CorporationWalletJournalRequest(Long.toString(corporationID), walletID);
        final WalletJournalResponse response = cacheFacade.getCached(request);
        if (null == response) {
            return Collections.emptyList();
        }
        return fill(response.getTransactions());
    }

    private List<WalletJournalEntry> fill(final List<WalletJournalEntry> journal) {
        final Map<Long, String> ref = eveFacade.getReferenceTypes();
        for (WalletJournalEntry e: journal) {
            e.setRefTypeName(ref.get(e.getRefTypeID()));
        }
        return journal;
    }

    @Override
    public List<Long> listCorporations() {
        return evanovaFacade.getCorporations();
    }

    @Override
    public List<CorporationMember> getCorporationMembers(long corpID) {
        final MemberTrackingRequest request = new MemberTrackingRequest(Long.toString(corpID), true);
        final MemberTrackingResponse response = cacheFacade.getCached(request);
        if (null == response) {
            return Collections.emptyList();
        }
        return response.getCorpMembers();
    }

    @Override
    public List<IndustryJob> getCorporationIndustryJobs(long corpID) {
        final CorporationIndustryJobsRequest request = new CorporationIndustryJobsRequest(Long.toString(corpID));
        final IndustryJobsResponse response = cacheFacade.getCached(request);
        if (null == response) {
            return Collections.emptyList();
        }
        return fillJobs(response.getJobs());
    }

    @Override
    public List<Outpost> getCorporationOutposts(long corporationId) {
        OutpostListRequest rq = new OutpostListRequest(Long.toString(corporationId));
        OutpostListResponse rs = cacheFacade.getCached(rq);
        if (null == rs) {
            return Collections.emptyList();
        }
        final List<Outpost> outposts = rs.getOutposts();
        for (Outpost o : outposts) {
            fillOutpost(o);
        }
        return outposts;
    }

    @Override
    public List<Starbase> getCorporationStarbases(long corporationId) {
        StarbaseListRequest rq = new StarbaseListRequest(Long.toString(corporationId));
        StarbaseListResponse rs = cacheFacade.getCached(rq);
        if (null == rs) {
            return Collections.emptyList();
        }

        final List<Starbase> starbases = rs.getStarbases();
        for (Starbase s : starbases) {
            fillStarbase(s);
        }
        return starbases;
    }

    @Override
    public Starbase getCorporationStarbase(long corporationId, long starbaseID) {
        StarbaseDetailsRequest rq = new StarbaseDetailsRequest(Long.toString(corporationId), Long.toString(starbaseID));
        StarbaseDetailsResponse rs = cacheFacade.getCached(rq);
        if (null == rs) {
            return null;
        }
        //TODO this is BAD and should not be done...
        //but then we need to store starbase lists somewhere because the API doesn't provide all the info in the details
        final List<Starbase> starbases = getCorporationStarbases(corporationId);
        Starbase starbase = null;
        for (Starbase s : starbases) {
            if (s.getItemID() == starbaseID) {
                starbase = s;
                break;
            }
        }
        if (null == starbase) {
            return null;
        }
        Starbase details = rs.getStarbase();
        details.setLocationID(starbase.getLocationID());
        details.setStandingOwnerID(starbase.getStandingOwnerID());
        details.setStandingOwnerName(getName(starbase.getStandingOwnerID()));
        details.setTypeID(starbase.getTypeID());
        //end of bad

        return fillStarbase(details);
    }

    @Override
    public List<ZKillMail> getKillMails(long ownerID) {
        if (!StringUtils.isBlank(evanovaFacade.hitCharacter(ownerID))) {
            return getCharacterKillMails(ownerID);
        }
        if (!StringUtils.isBlank(evanovaFacade.hitCorporation(ownerID))) {
            return getCorporationKillMails(ownerID);
        }
        return Collections.emptyList();
    }


    @Override
    public Map<String, List<Standing>> getStandings(long ownerID) {
        if (!StringUtils.isBlank(evanovaFacade.hitCharacter(ownerID))) {
            final CharacterStandingsRequest request = new CharacterStandingsRequest(Long.toString(ownerID));
            return getStandings(cacheFacade.getCached(request));
        }
        if (!StringUtils.isBlank(evanovaFacade.hitCorporation(ownerID))) {
            final CorporationStandingsRequest request = new CorporationStandingsRequest(Long.toString(ownerID));
            return getStandings(cacheFacade.getCached(request));
        }
        return Collections.emptyMap();
    }

    @Override
    public CharacterCalendar getCharacterCalendar(long charID) {
        final CharacterCalendarRequest request = new CharacterCalendarRequest(Long.toString(charID));
        final CharacterCalendarResponse response = cacheFacade.getCached(request);
        if (null == response) {
            return new CharacterCalendar();
        }
        final CharacterCalendar calendar = response.getCalendar();

        for (CharacterCalendar.Entry e: calendar.getEntries()) {
            final CharacterCalendarAttendeesRequest ra = new CharacterCalendarAttendeesRequest(Long.toString(charID), e.getEventID());
            final CharacterCalendarAttendeesResponse rs = cacheFacade.getCached(ra);
            if (null != rs) {
                e.setAttendees(rs.getAttendees());
            }
        }
        return calendar;
    }

    @Override
    public List<EveRSSEntry> getEveNews() {
        final EveAPIServicePreferences preferences = new EveAPIServicePreferences(context);
        final List<EveRSSEntry> news = new ArrayList<>();

        final EveRSSRequest rq1 = new EveRSSRequest();
        final EveRSSResponse rs1 = cacheFacade.getCached(rq1);
        if (null != rs1) {
            news.addAll(rs1.getEntries());
        }
        for (EveRSSEntry e: news) {
            if (StringUtils.isBlank(e.getLink())) {
                e.setLink(preferences.getEveRSSURL());
            }
        }
        Collections.sort(news, (lhs, rhs) -> Long.compare(rhs.getDateUpdated(), lhs.getDateUpdated()));
        return news;
    }

    @Override
    public List<Skill> getItemRequirements(long itemID) {
        return eveFacade.getRequirements(itemID);
    }

    @Override
    public void setMessageRead(List<Long> messages, boolean read) {
        mailFacade.setRead(messages, read);
    }

    @Override
    public void deleteNotification(long ownerID, long messageID) {
        mailFacade.deleteNotification(ownerID, messageID);
    }

    @Override
    public void deleteMail(long ownerID, long messageID) {
        mailFacade.deleteMail(ownerID, messageID);
    }

    @Override
    public NotificationMessage getNotification(long messageID) {
        return mailFacade.getNotification(messageID);
    }

    @Override
    public MailMessage getMail(long messageID) {
        return mailFacade.getMail(messageID);
    }

    @Override
    public List<NotificationMessage> getNotifications(long ownerID, long mailboxID) {
        return mailFacade.getNotifications(ownerID, mailboxID);
    }

    @Override
    public List<MailMessage> getMails(long ownerID, long mailboxID) {
        return mailFacade.getMails(ownerID, mailboxID);
    }


    @Override
    public List<Contact> getContacts(long ownerID, String contactGroup) {
        return mailFacade.getContacts(ownerID, contactGroup);
    }

    @Override
    public List<String> getContactTypes(long ownerID) {
        return mailFacade.getContactTypes(ownerID);
    }

    @Override
    public List<MailFacade.Mailbox> getMailBoxes(long ownerID) {
        return mailFacade.getMailBoxes(ownerID);
    }

    @Override
    public List<MailFacade.Mailbox> getNotificationBoxes(long ownerID) {
        return mailFacade.getNotificationBoxes(ownerID);
    }

    @Override
    public Contract getContract(long ownerID, long contractID) {
        return fillContract(evanovaFacade.getContract(ownerID, contractID));
    }

    @Override
    public List<Contract> getContracts(long ownerID) {
        final List<Contract> contracts = evanovaFacade.getContracts(ownerID);
       // CollectionUtils.forAllDo(evanovaFacade.getContracts(ownerID), c -> fillContract(c));
        return contracts;
    }

    @Override
    public List<Long> listCharacters() {
        return evanovaFacade.getCharacters();
    }

    @Override
    public CharacterSheet getCharacterSheet(long charID) {
        return evanovaFacade.getCharacterSheet(charID);
    }

    @Override
    public Skill getSkill(long skillID) {
        return eveFacade.getSkill(skillID);
    }

    @Override
    public String getName(long someoneID) {
        /*String name = eveFacade.getName(someoneID);
        if (!(StringUtils.isBlank(name) || Long.toString(someoneID).equals(name))) {
            return name;
        }*///FIXME
        String name = evanovaFacade.hitCharacter(someoneID);
        if (!StringUtils.isBlank(name)) {
            return name;
        }
        return evanovaFacade.hitCorporation(someoneID);
    }

    @Override
    public String getLocationName(long locationId) {
        String name =  eveFacade.getLocationName(locationId);
        if (null == name) {
            name = cacheFacade.getName(locationId);
        }
        if (null == name) {
            name = Long.toString(locationId);
        }
        return name;
    }

    @Override
    public String getItemName(long itemID) {
        return eveFacade.getItemName(itemID);
    }

    @Override
    public Item getItem(long itemID) {
        return eveFacade.getItem(itemID);
    }

    @Override
    public List<Item> getItemsRequiredFor(long itemID, int level) {
        return eveFacade.getItemsRequiredFor(itemID, level);
    }

    @Override
    public List<SkillInTraining> planTraining(Skill skill, int level, EveTraining training) {
        return evanovaFacade.planTraining(skill, level, training);
    }

    @Override
    public List<SkillInTraining> loadTraining(Uri from) {
        InputStream in = null;
        try {
            in = context.getContentResolver().openInputStream(from);
            final CharacterTrainingQueueResponse response = new EveMonSkillPlanParser().parse(in);
            if (response.getTrainingQueue().isEmpty()) {
                return Collections.emptyList();
            }

            final List<SkillInTraining> planning = new ArrayList<>(response.getTrainingQueue().size());
            for (SkillInTraining q : response.getTrainingQueue()) {
                final Skill skill = eveFacade.getSkill(q.getSkillID());
                if (null != skill) {
                    SkillInTraining imported = new SkillInTraining(skill, q.getSkillLevel());
                    imported.setTrainingType(q.getTrainingType());
                    planning.add(imported);
                }
            }
            return planning;
        }
        catch (IOException e) {
            Log.w("Evanova", e.getLocalizedMessage());
            return null;
        }
        finally {
            IOUtils.closeQuietly(in);
        }
    }

    @Override
    public void saveTraining(EveTraining training) {
        evanovaFacade.saveTraining(training);
    }

    @Override
    public CertificateTree getCertificates() {
        return eveFacade.getCertificates();
    }

   /* @Override
    public Map<Long, String> getSkillGroups() {
        return eveFacade.getSkillGroups();
    }*/

    @Override
    public String getCategoryName(long categoryId) {
        return eveFacade.getCategoryName(categoryId);
    }

    @Override
    public List<ItemAttribute> getItemAttributes(long itemID) {
        return eveFacade.getItemAttributes(itemID);
    }

    private Outpost fillOutpost(final Outpost outpost) {
        outpost.setOwnerName(getName(outpost.getOwnerID()));
        outpost.setSolarSystemName(getLocationName(outpost.getSolarSystemID()));
        outpost.setStandingOwnerName(getName(outpost.getStandingOwnerID()));
        outpost.setStationName(getLocationName(outpost.getStationID()));
        outpost.setStationTypeName(getItemName(outpost.getStationTypeID()));

        return outpost;
    }

    @Override
    public List<SkillInTraining> getTrainingQueue(long charID) {
        return evanovaFacade.getTrainingQueue(charID);
    }

    @Override
    public Asset getAsset(long ownerID, long assetID) {
        return evanovaFacade.getAsset(ownerID, assetID);
    }

    @Override
    public Map<Long, String> getItemGroups(long categoryID) {
        return eveFacade.getGroups(categoryID);
    }

    @Override
    public List<EveMarketGroup> getMarketGroups(long marketGroupID) {
        return eveFacade.getMarketGroups(marketGroupID);
    }

    @Override
    public List<Item> getMarketItems(long marketGroupID) {
        return eveFacade.getMarketItems(marketGroupID);
    }

    @Override
    public Cursor getAssetGroups(long ownerID, String type, long filterItem) {

        final Map<Long, Integer> groups = evanovaFacade.getAssetGroups(ownerID, type, filterItem);
        final List<Object[]> rows = new ArrayList<>(groups.size());

        for (Map.Entry<Long, Integer> g: groups.entrySet()) {
            String groupName;
            switch (type) {
                case "locationID" :
                    groupName = getLocationName(g.getKey());
                    break;
                case "marketGroupID" :
                    groupName = eveFacade.getMarketGroupName(g.getKey());
                    break;
                case "typeID":
                    groupName = getItemName(g.getKey());
                    break;
                default:
                    groupName = type;
                    break;
            }

            rows.add(new Object[]{g.getKey(), groupName, g.getValue()});
        }

        Collections.sort(rows, (lhs, rhs) -> ((String)lhs[1]).compareTo((String)rhs[1]));
        final MatrixCursor returned = new MatrixCursor(new String[]{"_id", "groupName", "groupCount"});
        for (Object[] row: rows) {
            returned.addRow(row);
        }
        return returned;
    }

    @Override
    public Cursor getAssets(long ownerID, long groupID, String type, long filterItem) {
        final MatrixCursor returned = new MatrixCursor(new String[]{"_id", "typeID", "typeName", "locationName", "groupName", "packaged", "quantity"});
        final List<Asset> assets = evanovaFacade.getAssets(ownerID, groupID, type, filterItem);

        final List<Object[]> rows = new ArrayList<>(assets.size());
        for (Asset a: assets) {
            final Object[] row = new Object[] {
                a.getAssetID(),
                a.getItemID(),
                a.getItem().getName(),
                a.getLocationName(),
                a.getItem().getMarketGroupName(),
                a.isPackaged(),
                a.getQuantity()
            };
            rows.add(row);
        }

        Collections.sort(rows, (lhs, rhs) -> ((String) lhs[2]).compareTo((String) rhs[2]));
        for (Object[] row: rows) {
            returned.addRow(row);
        }

        return returned;
    }

    @Override
    public void saveAccountOrder(List<Long> order) {
        //FIXME
       // this.evanovaFacade.updateAccountSortOrder(order);
    }

    @Override
    public void saveCharacterOrder(List<Long> order) {
        this.evanovaFacade.updateCharacterSortOrder(order);
    }

    @Override
    public void saveCorporationOrder(List<Long> order) {
        this.evanovaFacade.updateCorporationSortOrder(order);
    }

    private Starbase fillStarbase(final Starbase starbase) {
        starbase.setLocationName(getLocationName(starbase.getLocationID()));
        starbase.setStandingOwnerName(getName(starbase.getStandingOwnerID()));
        starbase.setTypeName(getItemName(starbase.getTypeID()));

        final Map<Long, Long> fuelMap = starbase.getFuelMap();
        final Map<Long, String> fuelNames = new HashMap<>();
        for (Long fuelType : fuelMap.keySet()) {
            fuelNames.put(fuelType, getItemName(fuelType));
        }
        starbase.setFuelNames(fuelNames);
        return starbase;
    }

    private ZKillMail fillKillMail(final ZKillMail killMail) {
        killMail.setSolarSystemName(getLocationName(killMail.getSolarSystemID()));
        killMail.getVictim().setShipTypeName(getItemName(killMail.getVictim().getShipTypeID()));
        return killMail;
    }

    private Contract fillContract(final Contract contract){
        if (null == contract) {
            return null;
        }

        contract.setAcceptorName(getName(contract.getAcceptorID()));
        contract.setIssuerCorpName(getName(contract.getIssuerCorpID()));
        contract.setAssigneeName(getName(contract.getAssigneeID()));
        contract.setForCorpName(getName(contract.getForCorpID()));
        contract.setIssuerCorpName(getName(contract.getIssuerCorpID()));
        contract.setIssuerName(getName(contract.getIssuerID()));

        contract.setStartStationName(getLocationName(contract.getStartStationID()));
        contract.setEndStationName(getLocationName(contract.getEndStationID()));
        return contract;
    }

    private Map<String, List<Standing>> getStandings(final StandingsResponse rs) {
        final Map<String, List<Standing>> returned = new HashMap<>();

        if (null == rs) {
            returned.put("corporations", new ArrayList<>(0));
            returned.put("agents", new ArrayList<>(0));
            returned.put("factions", new ArrayList<>(0));
        }
        else {
            returned.put("corporations", rs.getCorporationStandings());
            returned.put("agents", rs.getAgentStandings());
            returned.put("factions", rs.getFactionStandings());
        }
        for (Standing s : returned.get("agents")) {
            Agent agent = eveFacade.getAgent(s.getFromID());
            if (null != agent) {
                s.setLocationID(agent.getLocationID());
                s.setLocationName(agent.getLocation());
            }
        }
        return returned;
    }

    private List<ZKillMail> getCharacterKillMails(long charID) {
        final ZKillCharacterLogRequest request = new ZKillCharacterLogRequest(charID);
        final ZKillResponse response = cacheFacade.getCached(request);
        if (null == response) {
            return Collections.emptyList();
        }
        for (ZKillMail m: response.getKills()) {
            fillKillMail(m);
        }
        return response.getKills();
    }

    private List<ZKillMail> getCorporationKillMails(long corpID) {
        final ZKillCorporationLogRequest request = new ZKillCorporationLogRequest(corpID);
        final ZKillResponse response = cacheFacade.getCached(request);
        if (null == response) {
            return Collections.emptyList();
        }
        for (ZKillMail m: response.getKills()) {
            fillKillMail(m);
        }
        return response.getKills();
    }

    private List<IndustryJob> fillJobs(final List<IndustryJob> jobs) {
        for (IndustryJob j: jobs) {
            j.setOutputLocationName(getLocationName(j.getOutputLocationID()));
        }
        return jobs;
    }
}
