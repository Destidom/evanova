package com.tlabs.android.jeeves.model.data.social;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tlabs.android.jeeves.model.data.social.entities.ContactEntity;
import com.tlabs.android.jeeves.model.data.social.entities.MailboxEntity;
import com.tlabs.android.jeeves.model.data.social.entities.MessageEntity;
import com.tlabs.android.jeeves.model.data.social.entities.MessageListEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MailDatabase{

    private static final Logger LOG = LoggerFactory.getLogger(MailDatabase.class);

    private Dao<MessageEntity, Long> messageDAO;
    private Dao<MessageListEntity, Long> listDAO;
    private Dao<MailboxEntity, Long> mailboxDAO;

    private Dao<ContactEntity, Long> contactDAO;

    public MailDatabase(final ConnectionSource source) throws SQLException {

        this.messageDAO = DaoManager.createDao(source, MessageEntity.class);
        this.listDAO = DaoManager.createDao(source, MessageListEntity.class);
        this.mailboxDAO = DaoManager.createDao(source, MailboxEntity.class);
        this.contactDAO = DaoManager.createDao(source, ContactEntity.class);
    }

    public void onCreate(ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, ContactEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, MessageEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, MessageListEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, MailboxEntity.class);
        }
        catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public void onUpgrade(ConnectionSource connectionSource, int oldVersion, int newVersion) {
        onDelete(connectionSource);
        onCreate(connectionSource);
    }

    private void onDelete(ConnectionSource connectionSource) {
        try {
            TableUtils.dropTable(connectionSource, ContactEntity.class, true);
            TableUtils.dropTable(connectionSource, MessageListEntity.class, true);
            TableUtils.dropTable(connectionSource, MailboxEntity.class, true);
            TableUtils.dropTable(connectionSource, MessageEntity.class, true);
        }
        catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public void saveMail(final long ownerID, final MessageEntity message) {
        try {
            final MessageEntity existing = messageDAO.queryForId(message.getId());
            if (null == existing) {
                messageDAO.create(message);
                addMailboxes(ownerID, message, message.getMailboxes());
            }
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

    public void saveNotification(final long ownerID, final MessageEntity message) {
        try {
            final MessageEntity existing = messageDAO.queryForId(message.getId());
            if (null == existing) {
                message.setTitle(getNotificationTitle((int)message.getTypeID()));
                messageDAO.create(message);
                addMailboxes(ownerID, message, message.getMailboxes());
            }
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

    private void addMailboxes(final long ownerID, final MessageEntity message, final List<Long> mailboxes) {
        if ((null == mailboxes) || mailboxes.isEmpty()) {
            return;
        }
        try {
            for (long mb: mailboxes) {
                final MessageListEntity list = new MessageListEntity();
                list.setMailboxID(mb);
                list.setOwnerID(ownerID);
                list.setMessageID(message.getId());
                listDAO.create(list);
            }

        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

    public void updateBody(final MessageEntity message) {
        try {

            final UpdateBuilder<MessageEntity, Long> update = messageDAO.updateBuilder();
            update
                    .updateColumnValue("body", message.getBody())
                    .where()
                    .eq("_id", message.getId());
            update.update();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

    public void delete(final long ownerID, final long messageID) {
        try {
            DeleteBuilder<MessageListEntity, Long> delete = listDAO.deleteBuilder();
            delete
                    .where()
                    .eq("owner_id", ownerID)
                    .and()
                    .eq("message_id", messageID);
            delete.delete();
            if (listDAO.queryBuilder().where().eq("message_id", messageID).countOf() == 0) {
                final DeleteBuilder<MessageEntity, Long> deleteM = messageDAO.deleteBuilder();
                deleteM.where().eq("_id", messageID);
                deleteM.delete();
            }
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

    public void save(final ContactEntity contact) {
        try {
            final ContactEntity existing = contactDAO
                .queryBuilder()
                .where()
                .eq("owner_id", contact.getOwnerID())
                .and()
                .eq("contact_id", contact.getContactID())
                .queryForFirst();
            if (null == existing) {
                contactDAO.create(contact);
            }
            else {
                contact.setId(existing.getId());
                contactDAO.update(contact);
            }
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

    public MessageEntity get(final long messageID) {
        try {
            return messageDAO.queryForId(messageID);
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public List<MessageEntity> listMailsSince(final long ownerID, final long since) {
        try {
            return listMessagesSince(ownerID, since).and().eq("type_id", -1).query();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<MessageEntity> listNotificationsSince(final long ownerID, final long since) {
        try {
            return listMessagesSince(ownerID, since).and().ge("type_id", 0).query();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<MessageEntity> listMessages(final long ownerID, final long mailboxID) {
        try {
            final QueryBuilder<MessageListEntity, Long> in = listDAO.queryBuilder();
            in.selectColumns("message_id").where().eq("owner_id", ownerID).and().eq("mailbox_id", mailboxID);

            return messageDAO.queryBuilder()
                    .orderBy("sent_date", false)
                    .selectColumns("_id", "type_id", "sender_id", "sender_name", "sent_date", "title", "read")
                    .where()
                    .in("_id", in)
                    .query();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<MailboxEntity> listMailBoxes(final long ownerID) {
        return listMailboxes(ownerID, MailEntities.INBOX_ID, MailEntities.OTHERS_ID);
    }

    public List<MailboxEntity> listNotificationBoxes(final long ownerID) {
        return listMailboxes(ownerID, MailEntities.NOTIFICATION_AGENTS, MailEntities.NOTIFICATION_INSURANCE);
    }

    public List<MailboxEntity> listMailingListBoxes(final long ownerID) {
        return listMailboxes(ownerID, 1, Long.MAX_VALUE);
    }

    public void setupMailbox(final MailboxEntity mailbox) {
        try {
            if (mailboxDAO.queryBuilder()
                    .where()
                    .eq("mailbox_id", mailbox.getMailboxID())
                    .and()
                    .eq("owner_id", mailbox.getOwnerID())
                    .countOf() == 0) {
                mailboxDAO.create(mailbox);
            }
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

    public List<String> listContactTypes(final long ownerID) {
        try {
            final List<String[]> contacts = contactDAO
                    .queryBuilder()
                    .selectColumns("contact_group")
                    .groupBy("contact_group")
                    .orderBy("contact_group", true)
                    .where()
                    .eq("owner_id", ownerID)
                    .queryRaw()
                    .getResults();
            final List<String> types = new ArrayList<>(contacts.size());
            for (String[] e: contacts) {
                types.add(e[0]);
            }
            return types;
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<ContactEntity> getContacts(final long ownerID, final String group) {
        try {
            return contactDAO.queryBuilder()
                    .orderBy("contact_name", true)
                    .where()
                    .eq("owner_id", ownerID)
                    .and()
                    .eq("contact_group", group)
                    .query();
        }

        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    public void markAsRead(List<Long> messages, boolean read) {
        try {
            final UpdateBuilder<MessageEntity, Long> update = messageDAO.updateBuilder();
            update
                    .where()
                    .in("_id", messages);
            update.updateColumnValue("read", read ? 1 : 0);
            update.update();
        }
        catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

    private List<MailboxEntity> listMailboxes(final long ownerID, long fromID, long toID) {
        try {
            final List<MailboxEntity> entities = mailboxDAO
                    .queryBuilder()
                    .where()
                    .eq("owner_id", ownerID)
                    .and()
                    .between("mailbox_id", fromID, toID)
                    .query();
            for (MailboxEntity e: entities) {
                e.setReadCount(getReadCount(ownerID, e.getMailboxID()));
                e.setTotalCount(getTotalCount(ownerID, e.getMailboxID()));
            }
            return entities;
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    private long getReadCount(final long ownerID, final long mailboxID) {
        try {
            final QueryBuilder<MessageListEntity, Long> in = listDAO.queryBuilder();
            in.selectColumns("message_id").where().eq("owner_id", ownerID).and().eq("mailbox_id", mailboxID);

            return messageDAO.queryBuilder().where().in("_id", in).and().eq("read", 1).countOf();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return 0;
        }
    }

    private long getTotalCount(final long ownerID, final long mailboxID) {
        try {
            final QueryBuilder<MessageListEntity, Long> in = listDAO.queryBuilder();
            in.selectColumns("message_id").where().eq("owner_id", ownerID).and().eq("mailbox_id", mailboxID);

            return messageDAO.queryBuilder().where().in("_id", in).countOf();
        }
        catch (SQLException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return 0;
        }
    }

    //FIXME
    private String getNotificationTitle(Integer typeID) {
       /* if ((typeID < 0) || (typeID >= this.notificationTitles.size())) {
            return "TypeID " + typeID;
        }
        return this.notificationTitles.get(typeID);*/
        return "Notification TypeID " + typeID;
    }

    private Where<MessageEntity, Long> listMessagesSince(final long ownerID, final long since) throws SQLException {
        final QueryBuilder<MessageListEntity, Long> in = listDAO.queryBuilder();
        in.selectColumns("message_id").where().eq("owner_id", ownerID);

        return messageDAO.queryBuilder()
                .selectColumns("_id", "type_id", "sender_id", "sender_name", "sent_date", "title", "read")
                .orderBy("sent_date", false)
                .where()
                .gt("sent_date", since)
                .and()
                .in("_id", in);
    }

}

