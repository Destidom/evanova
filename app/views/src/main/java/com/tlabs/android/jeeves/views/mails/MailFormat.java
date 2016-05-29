package com.tlabs.android.jeeves.views.mails;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.eve.api.mail.MailMessage;
import com.tlabs.eve.api.mail.NotificationMessage;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class MailFormat {

    private interface AttributeValueFormatter {

        boolean accept(String attrName);

        String format(final Context context, final String attrName, final String attrValue);
    }

    private static final AttributeValueFormatter floatAttributeFormat = new AttributeValueFormatter() {

        @Override
        public boolean accept(String attrName) {
            return (
                    "shieldValue".equalsIgnoreCase(attrName) ||
                    "hullValue".equalsIgnoreCase(attrName) ||
                    "armorValue".equalsIgnoreCase(attrName));
        }

        @Override
        public String format(final Context context, String attrName, String attrValue) {
            final float value = Float.parseFloat(attrValue);
            if (value <= 0) {
                return "N/A";
            }
            return attrValue;
        }
    };

    private static final AttributeValueFormatter iskAttributeFormat = new AttributeValueFormatter() {

        @Override
        public boolean accept(String attrName) {
            return (
                    "cost".equalsIgnoreCase(attrName) ||
                    "amount".equalsIgnoreCase(attrName) ||
                    "payout".equalsIgnoreCase(attrName));
        }
        @Override
        public String format(final Context context, String attrName, String attrValue) {
            final double isk = Double.parseDouble(attrValue);
            if (isk <= 0) {
                return "N/A";
            }
            return EveFormat.Currency.MEDIUM(isk, true);
        }
    };

    private static final AttributeValueFormatter nameAttributeFormat = new AttributeValueFormatter() {
        @Override
        public boolean accept(String attrName) {
            return (
                    "aggressorID".equalsIgnoreCase(attrName) ||//War
                    "aggressorCorpID".equalsIgnoreCase(attrName) ||//War
                    "againstID".equalsIgnoreCase(attrName) ||//War
                    "declaredByID".equalsIgnoreCase(attrName));//War
        }
        @Override
        public String format(final Context context, String attrName, String attrValue) {
            final long nameID = Long.parseLong(attrValue);
            //FIXME FIXME
            //return getContent().getName(nameID);
            return "" + nameID;
        }
    };

    private static final AttributeValueFormatter typeAttributeFormat = new AttributeValueFormatter() {

        @Override
        public boolean accept(String attrName) {
            return (
                    "typeID".equalsIgnoreCase(attrName));
        }
        @Override
        public String format(final Context context, String attrName, String attrValue) {
            //FIXME FIXME
            //final long typeID = Long.parseLong(attrValue);
            //return EveProvider.getItemName(context.getContentResolver(), typeID);
            return attrValue;
        }
    };

    private static final AttributeValueFormatter shipTypeAttributeFormat = new AttributeValueFormatter() {

        @Override
        public boolean accept(String attrName) {
            return (
                    "shipTypeID".equalsIgnoreCase(attrName));
        }
        @Override
        public String format(final Context context, String attrName, String attrValue) {
            //FIXME FIXME
            return attrValue;
        }
    };

    private static final AttributeValueFormatter solarSystemAttributeFormat = new AttributeValueFormatter() {

        @Override
        public boolean accept(String attrName) {
            return (
                    "solarSystemID".equalsIgnoreCase(attrName));
        }
        @Override
        public String format(final Context context, String attrName, String attrValue) {
            final long stationID = Long.parseLong(attrValue);
            //FIXME FIXME return EveProvider.getLocationName(context.getContentResolver(), stationID);
            return attrValue;
        }
    };

    private static final AttributeValueFormatter dateAttributeFormat = new AttributeValueFormatter() {

        @Override
        public boolean accept(String attrName) {
            return attrName.endsWith("Date");
        }

        @Override
        public String format(final Context context, String attrName, String attrValue) {
            //http://wiki.eve-id.net/APIv2_Char_NotificationTexts_XML
            //The time outputed by some of the notifications is formatted to Microsoft's version of epoch time,
            //the formula to convert this to unix epoch is: $x = ($v/10000000)-11644473600;
            //Where $v is the time returned in the notification text.

            //FIXME this doesn't work- it's always 1970 with the above formula and others

            /*final long longValue = Long.parseLong(attrValue);
            final long date = (longValue / 10000000l) - 11644473600l;
            return date + " = " + FormatHelper.DateTime.LONG(date, false);*/
            return attrValue;
        }
    };

    private static final List<AttributeValueFormatter> attributeFormatters;
    private static final Map<String, Integer> attributeTitleResources;

    static {
        attributeTitleResources = new ArrayMap<>();
        attributeTitleResources.put("shipName", R.string.jeeves_notification_attribute_shipName);

        attributeTitleResources.put("shipName", R.string.jeeves_notification_attribute_shipName);
        attributeTitleResources.put("typeID", R.string.jeeves_notification_attribute_typeID);
        attributeTitleResources.put("level", R.string.jeeves_notification_attribute_level);
        attributeTitleResources.put("startDate", R.string.jeeves_notification_attribute_startDate);
        attributeTitleResources.put("endDate", R.string.jeeves_notification_attribute_endDate);
        attributeTitleResources.put("numWeeks", R.string.jeeves_notification_attribute_numWeeks);
        attributeTitleResources.put("hostileState", R.string.jeeves_notification_attribute_hostileState);
        attributeTitleResources.put("againstID", R.string.jeeves_notification_attribute_againstID);
        attributeTitleResources.put("delayHours", R.string.jeeves_notification_attribute_delayHours);
        attributeTitleResources.put("declaredByID", R.string.jeeves_notification_attribute_declaredByID);
        attributeTitleResources.put("againstID", R.string.jeeves_notification_attribute_againstID);
        attributeTitleResources.put("aggressorID", R.string.jeeves_notification_attribute_aggressorID);
        attributeTitleResources.put("aggressorCorpID", R.string.jeeves_notification_attribute_aggressorCorpID);
        attributeTitleResources.put("cost", R.string.jeeves_notification_attribute_cost);
        attributeTitleResources.put("amount", R.string.jeeves_notification_attribute_amount);
        attributeTitleResources.put("payout", R.string.jeeves_notification_attribute_payout);
        //I18N see shipName

        attributeFormatters = new ArrayList<>();
        attributeFormatters.add(dateAttributeFormat);
        attributeFormatters.add(floatAttributeFormat);
        attributeFormatters.add(iskAttributeFormat);
        attributeFormatters.add(nameAttributeFormat);
        attributeFormatters.add(typeAttributeFormat);
        attributeFormatters.add(shipTypeAttributeFormat);
        attributeFormatters.add(solarSystemAttributeFormat);
    }

    private MailFormat() {
    }

    public static String formatMailBody(final Context context, final MailMessage message) {
        return StringUtils.removeStart(StringUtils.removeStart(message.getBody(), "<br/>"), "<br>");
    }

    public static String formatNotificationBody(final Context context, final NotificationMessage message) {
        final Map<String, String> attrs = message.getAttributes();
        String r = "";
        for (String attr: attrs.keySet()) {
            String attrValue = attrs.get(attr);            
            if ((null == attrValue) || ("null".equalsIgnoreCase(attrValue))) {
                attrValue = "";
            }
            else {
                attrValue = formatAttributeValue(context, attr, attrValue);
            }
            
            r = r + "<p>" + formatAttributeName(context, attr) + ": " + attrValue + "</p>";
        }

        return r;
    }    
    
    private static String formatAttributeValue(final Context context, final String attrName, final String attrValue) {
        for (AttributeValueFormatter f: attributeFormatters) {
            if (f.accept(attrName)) {
                return f.format(context, attrName, attrValue);
            }
        }        
        return attrValue;
    }
    
    private static String formatAttributeName(final Context context, final String attrName) {
        Integer rid = attributeTitleResources.get(attrName);
        if (null == rid) {
            return attrName;
        }
        return context.getResources().getString(rid);
    }
}
