package com.tlabs.android.jeeves.views.character;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.util.ArrayMap;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
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
import com.tlabs.eve.api.character.CharacterInfo;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//FIXME I18N
public class CharacterSummaryWidget extends FrameLayout implements CharacterWidget {

    private static final Map<String, Integer> levelListBloodlines;
    static {
        levelListBloodlines = new ArrayMap<>();
        levelListBloodlines.put("Deteis", 0);
        levelListBloodlines.put("Civire", 1);
        levelListBloodlines.put("Sebiestor", 2);
        levelListBloodlines.put("Brutor", 3);
        levelListBloodlines.put("Amarr", 4);
        levelListBloodlines.put("Ni-Kunni", 5);
        levelListBloodlines.put("Gallente", 6);
        levelListBloodlines.put("Intaki", 7);
        levelListBloodlines.put("Static", 8);
        levelListBloodlines.put("Modifier", 9);
        levelListBloodlines.put("Achura", 10);
        levelListBloodlines.put("Jin-Mei", 11);
        levelListBloodlines.put("Khanid", 12);
        levelListBloodlines.put("Vherokior", 13);
    }

    static class ShipViewHolder implements CharacterWidget {
        private final TextView shipNameView;
        private final TextView shipTypeNameView;
        private final ImageView shipImageView;
        private final View selectionView;

        public ShipViewHolder(final View parent) {
            this.shipNameView = (TextView)parent.findViewById(R.id.j_character_attribute_SheepText);
            this.shipTypeNameView = (TextView)parent.findViewById(R.id.j_character_attribute_SheepTypeText);
            this.shipImageView = (ImageView)parent.findViewById(R.id.j_character_attribute_SheepImage);
            this.selectionView = parent.findViewById(R.id.j_character_attribute_SheepSelectionBox);
        }

        @Override
        public void setCharacter(EveCharacter character) {
            final long shipId = character.getShipTypeID();
            if (shipId <= 0) {
                shipImageView.setImageBitmap(null);
                shipNameView.setText("No ship information.");
                shipTypeNameView.setText("");
                selectionView.setOnClickListener(null);
            }
            else {
                EveImages.loadItemIcon(shipId, shipImageView);
                shipNameView.setText(character.getShipName());
                shipTypeNameView.setText(
                        StringUtils.isBlank(character.getShipTypeName()) ? "Ship ID " + shipId : character.getShipTypeName());
            }
        }
    }

    static class HistoryViewHolder {

        private final ImageView logoView;
        private final TextView corpNameView;
        private final TextView joinedView;

        private final View itemView;

        public HistoryViewHolder(final View parent) {
            this.itemView = parent;
            this.logoView = (ImageView)parent.findViewById(R.id.rowItemIcon);
            this.joinedView = (TextView)parent.findViewById(R.id.rowItemDescription);
            this.corpNameView = (TextView)parent.findViewById(R.id.rowItemName);
        }

        public void setHistory(final CharacterInfo.History h, CharacterInfo.History next) {
            EveImages.loadCorporationIcon(h.getCorporationID(), logoView);
            corpNameView.setText(h.getCorporationName());

            if (null == next) {
                corpNameView.setTextColor(Color.WHITE);
                setCurrent(h);
            }
            else {
                corpNameView.setTextColor(Color.GRAY);
                setPrevious(h, next);
            }
        }

        private void setCurrent(final CharacterInfo.History h) {
            SpannableStringBuilder sp = new SpannableStringBuilder("Member since ");
            int spl = sp.length();
            sp.append(EveFormat.DateTime.SHORT(h.getStartDate(), true));
            sp.setSpan(new StyleSpan(Typeface.BOLD), spl, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.append(" for " + EveFormat.Duration.MEDIUM(System.currentTimeMillis() - h.getStartDate()));
            joinedView.setText(sp);
        }

        private void setPrevious(final CharacterInfo.History h, CharacterInfo.History next) {
            SpannableStringBuilder sp = new SpannableStringBuilder("From ");
            int spl = sp.length();
            sp.append(EveFormat.DateTime.SHORT(h.getStartDate(), true));
            sp.setSpan(new StyleSpan(Typeface.BOLD), spl, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.append(" To ");
            spl = sp.length();
            sp.append(EveFormat.DateTime.SHORT(next.getStartDate(), true));
            sp.setSpan(new StyleSpan(Typeface.BOLD), spl, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.append(" for " + EveFormat.Duration.MEDIUM(next.getStartDate() - h.getStartDate()));
            joinedView.setText(sp);
        }
    }

    static class BirthViewHolder implements CharacterWidget {
        private final ImageView birthImageView;
        private final TextView birthView;
        private final TextView ageView;

        private final TextView locationText;

        public BirthViewHolder(final View parent) {
            this.birthImageView = (ImageView)parent.findViewById(R.id.j_character_attribute_BirthImage);
            this.birthView = (TextView)parent.findViewById(R.id.j_character_attribute_BirthText);
            this.ageView = (TextView)parent.findViewById(R.id.j_character_attribute_AgeText);
            this.locationText = (TextView)parent.findViewById(R.id.j_character_attribute_LocationText);
        }

        @Override
        public void setCharacter(EveCharacter character) {
            if (birthImageView.getVisibility() != View.VISIBLE) {
                return;
            }

            final String bloodline = character.getBloodline();
            if (StringUtils.isBlank(bloodline)) {
                birthImageView.setVisibility(View.INVISIBLE);
            }
            else {
                Integer level = levelListBloodlines.get(bloodline);
                if (null == level) {
                    birthImageView.setVisibility(View.INVISIBLE);
                }
                else {
                    birthImageView.setVisibility(View.VISIBLE);
                    birthImageView.setImageLevel(level);
                }
            }

            birthView.setText(
                    "Created on " + EveFormat.Date.MEDIUM(character.getBirthdate()));
            ageView.setText(
                    EveFormat.Duration.MEDIUM(
                            System.currentTimeMillis() - character.getBirthdate()) + " old.");

            if (StringUtils.isBlank(character.getLocation().getLocationName())) {
                locationText.setText("No location information.");
            }
            else {
                locationText.setText(character.getLocation().getLocationName());
            }
        }
    }

    static class JumpFatigueViewHolder implements CharacterWidget {

        private final TextView activationView;
        private final TextView fatigueView;
        private final TextView lastUpdateView;

        public JumpFatigueViewHolder(final View parent) {
            this.activationView = (TextView)parent.findViewById(R.id.j_character_attribute_JumpActivation);
            this.fatigueView = (TextView)parent.findViewById(R.id.j_character_attribute_JumpFatigue);
            this.lastUpdateView = (TextView)parent.findViewById(R.id.j_character_attribute_JumpLastUpdate);
        }

        @Override
        public void setCharacter(EveCharacter character) {
            if (character.getJumpActivation() > 0) {
                activationView.setText(EveFormat.DateTime.PLAIN(character.getJumpActivation()));
            }
            else {
                activationView.setText("");
            }
            final long fatigue = character.getJumpFatigue() - System.currentTimeMillis();
            if (fatigue > 0) {
                fatigueView.setText(EveFormat.Duration.MEDIUM(fatigue));
                fatigueView.setTextColor(Color.YELLOW);
            }
            else {
                fatigueView.setText("Ready");//I18N
                fatigueView.setTextColor(Color.GREEN);
            }
            if (character.getJumpLastUpdate() > 0) {
                lastUpdateView.setText(EveFormat.DateTime.PLAIN(character.getJumpLastUpdate()));
            }
            else {
                lastUpdateView.setText("");
            }
        }
    }

    private List<CharacterWidget> holders = new ArrayList<>(5);
    private ViewGroup corporationGroup;

    public CharacterSummaryWidget(Context context) {
        super(context);
        init();
    }

    public CharacterSummaryWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CharacterSummaryWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setCharacter(EveCharacter character) {
        updateView(character);
    }

    private void init() {
        inflate(getContext(), R.layout.jeeves_view_character_summary, this);

        this.corporationGroup = (ViewGroup)findViewById(R.id.j_character_attribute_CorporationContainer);

      //  final ImageView infoIcon = (ImageView)findViewById(R.id.characterInfoIcon);
    //    infoIcon.setVisibility(View.GONE);


        this.holders.clear();
        this.holders.add(new ShipViewHolder(this));
        this.holders.add(new BirthViewHolder(this));
        this.holders.add(new JumpFatigueViewHolder(this));
        this.holders.add(new CharacterInfoWidget(getContext()));
    }


    private void updateView(final EveCharacter character) {
        if (null == character) {
            return;
        }
        if (null == this.corporationGroup) {
            return;
        }

        for (CharacterWidget a: this.holders) {
            a.setCharacter(character);
        }

        this.corporationGroup.removeAllViews();
        CharacterInfo.History next = null;

        for (CharacterInfo.History h: character.getHistory()) {
            final HistoryViewHolder historyHolder = newCorpView(this.corporationGroup);
            this.corporationGroup.addView(historyHolder.itemView);
            historyHolder.setHistory(h, next);
            next = h;
        }
    }

    private HistoryViewHolder newCorpView(final ViewGroup parent) {
        final View corpView =
                LayoutInflater.from(this.corporationGroup.getContext()).inflate(R.layout.row_item_small, null, false);
        return new HistoryViewHolder(corpView);
    }
}
