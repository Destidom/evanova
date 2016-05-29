package com.tlabs.android.evanova.app.skills.main;

import android.content.Context;
import android.os.Bundle;
import android.widget.ViewFlipper;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.skills.DaggerSkillDatabaseComponent;
import com.tlabs.android.evanova.app.skills.SkillDatabaseModule;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.skills.CertificateListWidget;
import com.tlabs.android.jeeves.views.skills.CertificateViewWidget;
import com.tlabs.android.jeeves.views.skills.SkillListWidget;
import com.tlabs.android.jeeves.views.skills.SkillViewWidget;
import com.tlabs.android.jeeves.views.ui.pager.TabPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPagerAdapter;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.SkillTree;
import com.tlabs.eve.api.character.Certificate;
import com.tlabs.eve.api.character.CertificateTree;

import javax.inject.Inject;

public class SkillDatabaseActivity extends BaseActivity implements SkillDatabaseView {

    private static final int FLIPPER_MAIN = 0;
    private static final int FLIPPER_SKILL = 1;
    private static final int FLIPPER_CERTIFICATE = 2;

    private static class SkillPagerAdapter extends ViewPagerAdapter {

        public SkillPagerAdapter(Context context) {
            super(context);

            final SkillListWidget wSkills = new SkillListWidget(getContext());
            wSkills.setListener((skill, selected) -> {
                if (selected) onSkillSelected(skill);
            });
            addView(wSkills, R.string.skills_pager_skills);

            final CertificateListWidget wCertificates = new CertificateListWidget(getContext());
            wCertificates.setListener(c -> onCertificateSelected(c));
            addView(wCertificates, R.string.skills_pager_certificates);
        }

        protected void onSkillSelected(final Skill skill) {

        }

        protected void onCertificateSelected(final Certificate certificate) {

        }

        public void setCharacter(final EveCharacter character) {
            final SkillListWidget w0 = getView(0);
            w0.setCharacter(character);

            final CertificateListWidget w1 = getView(1);
            w1.setCharacter(character);
        }

        public void setSkills(final SkillTree tree) {
            final SkillListWidget w = getView(0);
            w.setSkills(tree);
        }

        public final void setCertificates(final CertificateTree tree) {
            final CertificateListWidget w = getView(1);
            w.setCertificates(tree);
        }
    }

    @Inject
    @Presenter
    SkillDatabasePresenter presenter;

    private TabPager pager;
    private ViewFlipper flipper;

    private SkillViewWidget wSkill;
    private CertificateViewWidget wCertificate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerSkillDatabaseComponent.builder()
                .applicationComponent(Application.getAppComponent())
                .skillDatabaseModule(new SkillDatabaseModule())
                .build()
                .inject(this);

        this.pager = new TabPager(this);
        this.pager.setAdapter(new SkillPagerAdapter(this) {
            @Override
            protected void onCertificateSelected(Certificate certificate) {
                presenter.onCertificateSelected(certificate);
            }

            @Override
            protected void onSkillSelected(Skill skill) {
                presenter.onSkillSelected(skill);
            }
        });


        this.flipper = new ViewFlipper(this);
        this.flipper.addView(this.pager);

        this.wSkill = new SkillViewWidget(this);
        this.flipper.addView(this.wSkill);

        this.wCertificate = new CertificateViewWidget(this);
        this.flipper.addView(this.wCertificate);
        setView(this.flipper);
    }

    @Override
    public void onBackPressed() {
        switch (this.flipper.getDisplayedChild()) {
            case FLIPPER_MAIN:
                super.onBackPressed();
                break;
            default:
                this.flipper.setDisplayedChild(FLIPPER_MAIN);
                break;
        }
    }

    @Override
    public void setCharacter(EveCharacter character) {
        final SkillPagerAdapter adapter = pager.getAdapter();
        adapter.setCharacter(character);
    }

    @Override
    public void setSkills(SkillTree tree) {
        final SkillPagerAdapter adapter = pager.getAdapter();
        adapter.setSkills(tree);
    }

    @Override
    public void setCertificates(CertificateTree tree) {
        final SkillPagerAdapter adapter = pager.getAdapter();
        adapter.setCertificates(tree);
    }

    @Override
    public void showSkill(Skill skill) {
        this.wSkill.setSkill(skill);
        this.flipper.setDisplayedChild(FLIPPER_SKILL);
    }

    @Override
    public void showCertificate(Certificate certificate) {
        this.wCertificate.setCertificate(certificate);
        this.flipper.setDisplayedChild(FLIPPER_CERTIFICATE);
    }
}

