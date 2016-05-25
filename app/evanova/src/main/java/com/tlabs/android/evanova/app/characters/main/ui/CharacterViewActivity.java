package com.tlabs.android.evanova.app.characters.main.ui;

import android.os.Bundle;

import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.characters.CharacterComponent;
import com.tlabs.android.evanova.app.characters.CharacterModule;
import com.tlabs.android.evanova.app.characters.DaggerCharacterComponent;
import com.tlabs.android.evanova.app.characters.main.CharacterActivityView;
import com.tlabs.android.evanova.app.skills.SkillDatabaseModule;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.BaseFragment;

public class CharacterViewActivity extends BaseActivity {


    private CharacterComponent component;
    private CharacterViewFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.component =
                DaggerCharacterComponent
                .builder()
                .applicationComponent(Application.getAppComponent())
                .skillDatabaseModule(new SkillDatabaseModule())
                .characterModule(new CharacterModule())
                .build();

        this.fragment = CharacterViewFragment.newInstance(
                getIntent().getLongExtra(CharacterActivityView.EXTRA_CHAR_ID, -1));
        this.fragment.setRetainInstance(true);
        setFragment(this.fragment);
    }

    @Override
    protected void inject(BaseFragment fragment) {
        component.inject((CharacterViewFragment)fragment);
    }

}
