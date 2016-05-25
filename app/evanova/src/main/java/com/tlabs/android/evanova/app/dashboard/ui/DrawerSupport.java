package com.tlabs.android.evanova.app.dashboard.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.preferences.UserPreferences;
import com.tlabs.android.jeeves.model.EveAccount;

import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.Strings;

import java.util.ArrayList;
import java.util.List;

class DrawerSupport {

    private final Drawer drawer;
    private final List<EveAccount> accounts;

    private final AccountHeader header;

    private final DashboardActivity activity;
    private final UserPreferences userPreferences;

    public DrawerSupport(
            final DashboardActivity activity,
            final Bundle savedInstanceState) {
        this.activity = activity;
        this.accounts = new ArrayList<>();
        this.header = buildHeader(activity);
        this.userPreferences = new UserPreferences(activity.getApplicationContext());

        DrawerBuilder builder =
                new DrawerBuilder()
                .withAccountHeader(this.header)
                .withActivity(activity)
                .withHasStableIds(true)
                //.withItemAnimator(new AlphaCrossFadeAnimator())
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .inflateMenu(R.menu.drawer_accounts)
                .addDrawerItems(new DividerDrawerItem())
                .inflateMenu(R.menu.drawer_common)
                .addDrawerItems(new DividerDrawerItem())
                .inflateMenu(R.menu.drawer_settings)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> onDrawerMenuSelected((int)drawerItem.getIdentifier(), false))
                .withOnDrawerItemLongClickListener((view, position, drawerItem) -> onDrawerMenuSelected((int)drawerItem.getIdentifier(), true));

        final Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        if (null != toolbar) {
            builder = builder.withToolbar(toolbar);
        }

        this.drawer = builder.build();
    }

    public void setSelected(final long account) {
        this.drawer.setSelection(account, false);
    }

    public void setAccounts(final List<EveAccount> accounts) {
        this.accounts.clear();
        this.accounts.addAll(accounts);
        this.header.setProfiles(profiles(accounts));
    }

    public final void openDrawer() {
        this.drawer.openDrawer();
    }

    public final void closeDrawer() {
        this.drawer.closeDrawer();
    }

    public final boolean isDrawerOpened() {
        return this.drawer.isDrawerOpen();
    }

    protected boolean onDrawerMenuSelected(int item, boolean selected) {
        return false;
    }

    protected boolean onDrawerAccountSelected(EveAccount selected, boolean current) {
        return false;
    }

    private boolean onAccountSelected(final long accountId, final boolean current) {
        final EveAccount selected = findAccount(accountId);
        if (current) {
            return this.onDrawerAccountSelected(selected, true);
        }
        else if (this.onDrawerAccountSelected(selected, false)) {
            switch (selected.getType()) {
                case EveAccount.ACCOUNT:
                case EveAccount.CHARACTER:
                    if (selected.getShipID() > 0) {
                        this.header.setHeaderBackground(
                            new ImageHolder(EveImages.getItemImageURL(activity.getApplicationContext(), selected.getShipID())));
                        this.header.setSelectionSecondLine(selected.getShipName());
                    }
                    break;
                case EveAccount.CORPORATION:
                    this.header.setHeaderBackground(new ImageHolder(userPreferences.getBackgroundDefault()));
                    this.header.setSelectionSecondLine(Strings.r(activity.getApplication(), R.string.activity_corp_title));
                    break;
                default:
                    this.header.setHeaderBackground(new ImageHolder(userPreferences.getBackgroundDefault()));
                    this.header.setSelectionSecondLine(selected.getStatusMessage());
                    break;
            }
            return true;
        }
        return false;
    }

    private AccountHeader buildHeader(final Activity activity) {
        final AccountHeaderBuilder header =
            new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.background_planet)
                .withTranslucentStatusBar(true);
        header.withOnAccountHeaderListener((view, profile, current) -> onAccountSelected(profile.getIdentifier(), current));

        return header.build();
    }

    private List<IProfile> profiles(final List<EveAccount> characters) {
        final List<IProfile> profiles = new ArrayList<>(characters.size());
        for (EveAccount c: characters) {
            profiles.add(profileItem(c));
        }
        return profiles;
    }

    private ProfileDrawerItem profileItem(final EveAccount c) {
        ProfileDrawerItem item =
            new ProfileDrawerItem()
            .withIdentifier(c.getId())
            .withName(c.getName())
            .withNameShown(true);
        switch (c.getType()) {
            case EveAccount.ACCOUNT:
            case EveAccount.CHARACTER:
                item = item.withIcon(EveImages.getCharacterIconURL(this.activity, c.getOwnerId()));
                break;
            case EveAccount.CORPORATION:
                item = item.withIcon(EveImages.getCorporationIconURL(this.activity, c.getOwnerId()));
                break;
            default:
                item = item.withIcon(R.drawable.ic_eve_member);
                break;
                //item = item.withEmail(c.g)
        }
            //.withEmail(c.getCorporationName())
            //.withIcon(EveImages.getCharacterIconURL(c.getID()));
        return item;
    }

    private EveAccount findAccount(final long profileId) {
        for (EveAccount a: this.accounts) {
            if (a.getId() == profileId) {
                return a;
            }
        }
        return null;
    }
}
