package com.tlabs.android.evanova.app.characters.skills.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.characters.CharacterFragment;
import com.tlabs.android.evanova.app.characters.skills.CharacterSkillsView;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.skills.CertificateListWidget;
import com.tlabs.android.jeeves.views.skills.SkillListWidget;
import com.tlabs.android.jeeves.views.ui.pager.TabPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPagerAdapter;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.SkillTree;

import java.util.List;
import java.util.Map;

public class CharacterSkillsFragment extends CharacterFragment implements CharacterSkillsView {

    private static final class SkillPagerAdapter extends ViewPagerAdapter {

        private SkillListWidget wSkills;
        private CertificateListWidget wCertificates;

        public SkillPagerAdapter(Context context) {
            super(context);
            this.wSkills = new SkillListWidget(context);
            addView(this.wSkills, R.string.skills_pager_skills);

            this.wCertificates = new CertificateListWidget(context);
            addView(this.wCertificates, R.string.skills_pager_certificates);
        }

        public void showSkillTree(Map<SkillTree.SkillGroup, List<Skill>> tree, EveCharacter character) {
            this.wSkills.setSkills(tree);
            this.wSkills.setCharacter(character);
        }
    }

    private TabPager pager;

    @Nullable
    @Override
    public View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.pager = new TabPager(getContext());
        this.pager.setAdapter(new SkillPagerAdapter(getContext()));

        return this.pager;
    }



    @Override
    public void showSkillTree(Map<SkillTree.SkillGroup, List<Skill>> tree, EveCharacter character) {
        final SkillPagerAdapter adapter = (SkillPagerAdapter)this.pager.getAdapter();
        adapter.showSkillTree(tree, character);
    }
}
