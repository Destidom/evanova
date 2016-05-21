package com.tlabs.android.jeeves.model.data.social;

import com.tlabs.android.jeeves.model.data.social.entities.ContactEntity;
import com.tlabs.android.jeeves.model.data.social.entities.MailboxEntity;
import com.tlabs.android.jeeves.model.data.social.entities.MessageEntity;
import com.tlabs.eve.api.mail.Contact;
import com.tlabs.eve.api.mail.MailMessage;
import com.tlabs.eve.api.mail.MailingList;
import com.tlabs.eve.api.mail.Message;
import com.tlabs.eve.api.mail.NotificationMessage;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class MailEntities {

    private MailEntities() {}

    public static MessageEntity transform(final MailMessage message, final long ownerID, final long ownerCorp) {
        final MessageEntity entity = transformImpl(message);
        entity.setId(message.getMessageID());
        setMailboxes(message, entity, ownerID, ownerCorp);
        return entity;
    }

    public static MessageEntity transform(final NotificationMessage message, final long ownerID, final long ownerCorp) {
        final MessageEntity entity = transformImpl(message);
        entity.setId(message.getNotificationID());
        entity.setTypeID(message.getTypeID());
        setMailboxes(message, entity, ownerID, ownerCorp);
        return entity;
    }

    public static List<ContactEntity> transform(final long ownerID, final Contact.Group group) {
        final List<ContactEntity> entities = new ArrayList<>(group.getContacts().size());
        for (Contact c: group.getContacts()) {
            entities.add(transform(ownerID, group, c));
        }
        return entities;
    }

    public static ContactEntity transform(final long ownerID, final Contact.Group group, final Contact contact) {
        final ContactEntity entity = new ContactEntity();
        entity.setContactID(contact.getContactID());
        entity.setContactGroup(group.getName());
        entity.setContactName(contact.getContactName());
        entity.setStanding(contact.getStanding());
        entity.setWatched(contact.getInWatchlist() ? 1 : 0);
        entity.setOwnerID(ownerID);
        return entity;
    }

    public static Contact transform(final ContactEntity entity) {
        final Contact contact = new Contact();
        contact.setContactID(entity.getContactID());
        contact.setContactName(entity.getContactName());
        contact.setContactTypeID(entity.getContactType());
        contact.setInWatchlist(entity.getWatched() == 1);
        contact.setStanding(entity.getStanding());
        return contact;
    }

    public static MailboxEntity transform(final long ownerID, final MailingList list) {
        final MailboxEntity entity = new MailboxEntity();
        entity.setMailboxID(list.getListID());
        entity.setName(list.getDisplayName());
        entity.setOwnerID(ownerID);
        entity.setVisible(1);
        return entity;
    }

    public static List<MailFacade.Mailbox> transform(final List<MailboxEntity> entities) {
        final List<MailFacade.Mailbox> mailboxes = new ArrayList<>();
        for (MailboxEntity e: entities) {
            mailboxes.add(transform(e));
        }
        return mailboxes;
    }

    public static MailFacade.Mailbox transform(final MailboxEntity entity) {
        final MailFacade.Mailbox mailbox = new MailFacade.Mailbox();
        mailbox.setTotalCount(entity.getTotalCount());
        mailbox.setReadCount(entity.getReadCount());
        mailbox.setName(entity.getName());
        mailbox.setId(entity.getMailboxID());
        return mailbox;
    }


    public static MailMessage transformMail(final MessageEntity entity) {
        if (null == entity) {
            return null;
        }
        final MailMessage message = new MailMessage();
        message.setMessageID(entity.getId());
        return transformImpl(entity, message);
    }

    public static NotificationMessage transformNotification(final MessageEntity entity) {
        if (null == entity) {
            return null;
        }
        final NotificationMessage message = new NotificationMessage();
        message.setNotificationID(entity.getId());
        message.setTypeID((int)entity.getTypeID());
        return transformImpl(entity, message);
    }

    private static MessageEntity transformImpl(final Message message) {
        final MessageEntity entity = new MessageEntity();
        entity.setBody(message.getBody());
        entity.setDate(message.getSentDate());
        entity.setSenderID(message.getSenderID());
        entity.setSenderName(message.getSenderName());
        entity.setTitle(message.getTitle());
        return entity;
    }

    public static <T extends Message> T transformImpl(final MessageEntity entity, final T message) {
        if (null == entity) {
            return null;
        }
        message.setSenderName(entity.getSenderName());
        message.setBody(entity.getBody());
        message.setSentDate(entity.getDate());
        message.setSenderID(entity.getSenderID());
        message.setTitle(entity.getTitle());
        message.setRead(entity.getRead() == 1);
        return message;
    }

    static final long INBOX_ID = -999;
    static final long OUTBOX_ID = -998;
    static final long CORP_ID = -997;
    static final long ALLIANCE_ID = -996;
    static final long OTHERS_ID = -995;
    static final long TRASHBOX_ID = -900;

    static final long NOTIFICATION_AGENTS = -890;
    static final long NOTIFICATION_BOUNTIES = -889;
    static final long NOTIFICATION_BILLS = -888;
    static final long NOTIFICATION_CONTACTS = -887;
    static final long NOTIFICATION_CORPORATE = -886;
    static final long NOTIFICATION_MISC = -885;
    static final long NOTIFICATION_OLD = -884;
    static final long NOTIFICATION_SOV = -883;
    static final long NOTIFICATION_STRUCTURES = -882;
    static final long NOTIFICATION_WAR = -881;
    static final long NOTIFICATION_INSURANCE = -880;

    private static void setMailboxes(final MailMessage message, final MessageEntity entity, final long ownerID, final long ownerCorp) {
        final List<Long> boxes = new ArrayList<>();
        if (message.getSenderID() == ownerID) {
            boxes.add(OUTBOX_ID);
        }
        if (message.getToListID() > 0) {
            boxes.add(message.getToListID());
        }

        for (String s: StringUtils.split(message.getToCharacterIDs(), ",")) {
            if (s.equals(Long.toString(ownerID))) {
                boxes.add(INBOX_ID);
                break;
            }
        }
        if (message.getToCorpOrAllianceID() > 0) {
            if (message.getSenderID() == ownerCorp || message.getToCorpOrAllianceID() == ownerCorp) {
                boxes.add(CORP_ID);
            }
            else {
                boxes.add(ALLIANCE_ID);
            }
        }
        if (boxes.isEmpty()) {
            boxes.add(OTHERS_ID);
        }
        entity.setMailboxes(boxes);
    }

    private static void setMailboxes(final NotificationMessage message, final MessageEntity entity, final long ownerID, final long ownerCorp) {
        entity.setMailboxes(Collections.singletonList(notificationMailbox(message.getTypeID())));
    }

    private static long notificationMailbox(int typeId) {
        switch (typeId) {

            case 2://Character deleted
            case 3://Give medal to character
                return NOTIFICATION_MISC;

            case 4://Alliance maintenance bill
            case 5://Alliance war declared
            case 6://Alliance war surrender
            case 7://Alliance war retracted
            case 8://Alliance war invalidated by Concord
                return NOTIFICATION_WAR;

            case 9://Bill issued to a character
            case 10://Bill issued to corporation or alliance
            case 11://Bill not paid because there's not enough ISK available
            case 12://Bill, issued by a character, paid
            case 13://Bill, issued by a corporation or alliance, paid
                return NOTIFICATION_BILLS;

            case 14://Bounty claimed
                return NOTIFICATION_BOUNTIES;

            case 15://Clone activated
                return NOTIFICATION_MISC;

            case 16://New corp member application
            case 17://Corp application rejected
            case 18://Corp application accepted
            case 19://Corp tax rate changed
            case 20://Corp news report, typically for shareholders
            case 21://Player leaves corp
            case 22://Corp news, new CEO
            case 23://Corp dividend/liquidation, sent to shareholders
            case 24://Corp dividend payout, sent to shareholders
            case 25://Corp vote created
            case 26://Corp CEO votes revoked during voting
                return NOTIFICATION_CORPORATE;

            case 27://Corp declares war
            case 28://Corp war has started
            case 29://Corp surrenders war
            case 30://Corp retracts war
            case 31://Corp war invalidated by Concord
                return NOTIFICATION_WAR;

            case 32://Container password retrieval
            case 33://Contraband or low standings cause an attack or items being confiscated
            case 34://First ship insurance
            case 35://Ship destroyed, insurance payed
            case 36://Insurance contract invalidated/runs out
                return NOTIFICATION_MISC;

            case 37://Sovereignty claim fails (alliance)
            case 38://Sovereignty claim fails (corporation)
                return NOTIFICATION_SOV;

            case 39://Sovereignty bill late (alliance)
            case 40://Sovereignty bill late (corporation)
                return NOTIFICATION_BILLS;

            case 41://Sovereignty claim lost (alliance)
            case 42://Sovereignty claim lost (corporation)
            case 43://Sovereignty claim acquired (alliance)
            case 44://Sovereignty claim acquired (corporation)
                return NOTIFICATION_SOV;

            case 45://Alliance anchoring alert
            case 46://Alliance structure turns vulnerable
            case 47://Alliance structure turns invulnerable
            case 48://Sovereignty disruptor anchored
            case 49://Structure won/lost
                return NOTIFICATION_STRUCTURES;

            case 50://Corp office lease expiration notice
                return NOTIFICATION_CORPORATE;

            case 51://Clone contract revoked by station manager
            case 52://Corp member clones moved between stations
            case 53://Clone contract revoked by station manager
            case 54://Insurance contract expired
            case 55://Insurance contract issued
            case 56://Jump clone destroyed
            case 57://Jump clone destroyed
                return NOTIFICATION_MISC;

            case 58://Corporation joining factional warfare
            case 59://Corporation leaving factional warfare
            case 60://Corporation kicked from factional warfare on startup because of too low standing to the faction
            case 61://Character kicked from factional warfare on startup because of too low standing to the faction
            case 62://Corporation in factional warfare warned on startup because of too low standing to the faction
            case 63://Character in factional warfare warned on startup because of too low standing to the faction
            case 64://Character loses factional warfare rank
            case 65://Character gains factional warfare rank
                return NOTIFICATION_WAR;

            case 66://Agent has moved
                return NOTIFICATION_AGENTS;

            case 67://Mass transaction reversal message
            case 68://Reimbursement message
                return NOTIFICATION_MISC;

            case 69://Agent locates a character
            case 70://Research mission becomes available from an agent
            case 71://Agent mission offer expires
            case 72://Agent mission times out
            case 73://Agent offers a storyline mission
                return NOTIFICATION_AGENTS;

            case 74://Tutorial message sent on character creation
                return NOTIFICATION_MISC;

            case 75://Tower alert
            case 76://Tower resource alert
            case 77://Station aggression message
            case 78://Station state change message
            case 79://Station conquered message
            case 80://Station aggression message
                return NOTIFICATION_STRUCTURES;

            case 81://Corporation requests joining factional warfare
            case 82://orporation requests leaving factional warfare
            case 83://Corporation withdrawing a request to join factional warfare
            case 84://Corporation withdrawing a request to leave factional warfare
                return NOTIFICATION_WAR;

            case 85://Corporation liquidation
                return NOTIFICATION_CORPORATE;

            case 86://Territorial Claim Unit under attack
            case 87://Sovereignty Blockade Unit under attack
            case 88://Infrastructure Hub under attack
                return NOTIFICATION_SOV;

            case 89://Contact notification
            case 90://Contact notification
                return NOTIFICATION_CONTACTS;
            case 91://Incursion Completed
                return NOTIFICATION_MISC;
            case 92://Corp Kicked
                return NOTIFICATION_CORPORATE;
            case 93://Customs office has been attacked
            case 94://Customs office has entered reinforced
            case 95://Customs office has been transferred
                return NOTIFICATION_STRUCTURES;
            case 96://FW Alliance Warning
            case 97://FW Alliance Kick
                return NOTIFICATION_CORPORATE;
            case 98://AllWarCorpJoined Msg
            case 99://Ally Joined Defender
            case 100://Ally Has Joined a War Aggressor
            case 101://Ally Joined War Ally
            case 102://New war system: entity is offering assistance in a war.
            case 103://War Surrender Offer
            case 104://War Surrender Declined
                return NOTIFICATION_WAR;
            case 105://FacWar LP Payout Kill
            case 106://FacWar LP Payout Event
            case 107://107	FacWar LP Disqualified Eventd
            case 108://108	FacWar LP Disqualified Kill
                return NOTIFICATION_MISC;
            case 109://109	Alliance Contract Cancelled
                return NOTIFICATION_CORPORATE;
            case 110://110	War Ally Declined Offer
                return NOTIFICATION_WAR;
            case 111://111	Your Bounty Claimed
            case 112://112	Bounty Placed (Char)
            case 113://113	Bounty Placed (Corp)
            case 114://114	Bounty Placed (Alliance)
            case 115://115	Kill Right Available
            case 116://116	Kill Right Available Open
            case 117://117	Kill Right Earned
            case 118://118	Kill Right Used
            case 119://119	Kill Right Unavailable
            case 120://120	Kill Right Unavailable Open
                return NOTIFICATION_BOUNTIES;
            case 121://121	Declare War
            case 122://122	Offered Surrender
            case 123://123	Accepted Surrender
            case 124://124	Made War Mutual
            case 125://125	Retracts War
            case 126://126	Offered To Ally
            case 127://127	Accepted Ally
                return NOTIFICATION_WAR;
            case 128://128 Character Application Accept Message
            case 129://129	Character Application Reject Message
            case 130://130	Character Application Withdraw Message
                return NOTIFICATION_CORPORATE;
            default:
                return NOTIFICATION_MISC;
        }
    }
}
