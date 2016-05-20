package com.tlabs.android.jeeves.views.character;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.eve.api.EveAPI;
import com.tlabs.eve.api.character.CharacterSheet;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CharacterAttributesWidget extends FrameLayout implements CharacterWidget {

    private static final String attributeCalcFormat = "(%1$s + %2$d)";
    private static final String attributeTotalFormat = "%1$d";

    private static final Map<String, Integer> attributeIcons;

    static {
        attributeIcons = new ArrayMap<>();
        attributeIcons.put(EveAPI.ATTR_INTELLIGENCE, R.drawable.icon22_32_3);
        attributeIcons.put(EveAPI.ATTR_CHARISMA, R.drawable.icon22_32_1);
        attributeIcons.put(EveAPI.ATTR_WILLPOWER, R.drawable.icon22_32_2);
        attributeIcons.put(EveAPI.ATTR_MEMORY, R.drawable.icon22_32_4);
        attributeIcons.put(EveAPI.ATTR_PERCEPTION, R.drawable.icon22_32_5);
    }

    static final class ImplantHolder {

        private static final int LAYOUT = R.layout.row_item_small;

        private final ImageView implantImage;
        private final TextView implantTitle;
        private final TextView implantDescription;

        private ImplantHolder(final View view) {
            implantImage = (ImageView)view.findViewById(R.id.rowItemIcon);
            implantTitle = (TextView)view.findViewById(R.id.rowItemName);
            implantDescription = (TextView)view.findViewById(R.id.rowItemDescription);
        }

        private void renderView(final CharacterSheet.Implant implant) {
            EveImages.loadItemIcon(implant.getTypeID(), this.implantImage);
            this.implantTitle.setText(implant.getTypeName());
            this.implantDescription.setText(implant.getDescription());
        }

        public static void renderView(final View view, final CharacterSheet.Implant implant) {
            final ImplantHolder h = (ImplantHolder)view.getTag();
            h.renderView(implant);
        }

        public static View createView(final Context context) {
            View view = LayoutInflater.from(context).inflate(LAYOUT, null);
            final ImplantHolder h = new ImplantHolder(view);
            view.setTag(h);
            return view;
        }
    }

    static final class AttributeHolder {

        private static final int LAYOUT = R.layout.jeeves_row_character_attribute;

        private final ImageView attributeImage;
        private final TextView attributeName;
        private final TextView attributeText;
        private final TextView attributeValue;

        private AttributeHolder(final View view) {
            attributeImage = (ImageView)view.findViewById(R.id.j_character_attribute_RowAttributeIcon);
            attributeName = (TextView)view.findViewById(R.id.j_character_attribute_RowAttributeName);
            attributeText = (TextView)view.findViewById(R.id.j_character_attribute_RowAttributeText);
            attributeValue = (TextView)view.findViewById(R.id.j_character_attribute_RowAttributeValue);
        }

        private void renderView(final EveCharacter.Attribute attribute) {
            this.attributeName.setText(StringUtils.capitalize(attribute.getName()));
            this.attributeText.setText(
                    String.format(
                            attributeCalcFormat,
                            attribute.getBaseValue(),
                            attribute.getEnhancerValue()));
            this.attributeValue.setText(
                    String.format(
                            attributeTotalFormat,
                            attribute.getValue()));

            this.attributeImage.setImageResource(attributeIcons.get(attribute.getName()));

        }

        public static void renderView(final View view, final EveCharacter.Attribute attribute) {
            final AttributeHolder h = (AttributeHolder)view.getTag();
            h.renderView(attribute);
        }

        public static View createView(final Context context) {
            View view = LayoutInflater.from(context).inflate(LAYOUT, null);
            final AttributeHolder h = new AttributeHolder(view);
            view.setTag(h);
            return view;
        }
    }

    public CharacterAttributesWidget(Context context) {
        super(context);
        init();
    }

    public CharacterAttributesWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CharacterAttributesWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setCharacter(EveCharacter character) {
        updateView(character);
    }

    private void init() {
        inflate(getContext(), R.layout.jeeves_view_character_attributes, this);
    }

    private void updateView(EveCharacter character) {
        setAttributesView(character);
        setImplantsView(character);
        setDescriptionView(character);
    }

    private void setAttributesView(final EveCharacter character) {
        final ViewGroup attributesView = (ViewGroup)findViewById(R.id.j_character_attribute_AttributesView);
        attributesView.removeAllViews();

        final List<EveCharacter.Attribute> attrs = new ArrayList<>(5);
        attrs.addAll(character.getAttributes().values());
        Collections.sort(attrs, (lhs, rhs) -> lhs.getName().compareTo(rhs.getName()));

        for (EveCharacter.Attribute attr : attrs) {
            addView(attributesView, attr);
        }
    }

    private void setImplantsView(final EveCharacter character) {
        final ViewGroup implantsView = (ViewGroup)findViewById(R.id.j_character_attribute_ImplantsView);
        implantsView.removeAllViews();
        for (CharacterSheet.Implant implant : character.getImplants()) {
            addView(implantsView, implant);
        }
    }

    private void setDescriptionView(final EveCharacter character) {
        final TextView lastRespecText = (TextView)findViewById(R.id.j_character_attribute_AttributeLastRespecDate);
        if (character.getLastRespecDate() == 0) {
            lastRespecText.setText(R.string.jeeves_NA);
        }
        else {
            lastRespecText.setText(EveFormat.DateTime.PLAIN(character.getLastRespecDate()));
        }

        final TextView respecText = (TextView)findViewById(R.id.j_character_attribute_LastTimedRespec);
        if ((character.getLastTimedRespec() == 0) || (character.getLastRespecDate() == character.getLastTimedRespec())) {
            respecText.setText("");
        }
        else {
            respecText.setText(EveFormat.DateTime.PLAIN(character.getLastTimedRespec()));
        }

        final TextView freeRespectText = (TextView)findViewById(R.id.j_character_attribute_FreeRespec);
        if (character.getFreeRespecs() == 0) {
            freeRespectText.setText("None");//I18N
            freeRespectText.setTextColor(Color.LTGRAY);
        }
        else {
            freeRespectText.setText("" + character.getFreeRespecs());//I18N
            freeRespectText.setTextColor(Color.GREEN);
        }
    }

    private void addView(final ViewGroup parent, final EveCharacter.Attribute attr) {
        View attributeView = AttributeHolder.createView(parent.getContext());
        parent.addView(attributeView);
        AttributeHolder.renderView(attributeView, attr);
    }

    private void addView(final ViewGroup parent, final CharacterSheet.Implant implant) {
        View implantView = ImplantHolder.createView(parent.getContext());
        parent.addView(implantView);
        ImplantHolder.renderView(implantView, implant);
    }
}
