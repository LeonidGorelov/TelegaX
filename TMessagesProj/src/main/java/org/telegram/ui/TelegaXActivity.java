package org.telegram.ui;

import android.telephony.CellIdentityWcdma;
import android.view.View;

import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;

import java.util.ArrayList;
import java.util.Set;

public class TelegaXActivity extends UniversalFragment {

    @Override
    protected CharSequence getTitle() {
        return "Telega X";
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
    String proxyText = LocaleController.getString(R.string.Proxy);
    String premiumText = LocaleController.getString(R.string.TelegramPremium);
    items.add(SettingsActivity.SettingCell.Factory.of(1, 0, 0, 0, proxyText));
    items.add(SettingsActivity.SettingCell.Factory.of(2, 0, 0, 0,premiumText));
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        switch (item.id){
            case 1:
                presentFragment(new TelegaXProxyActivity());
                break;
            case 2:
                presentFragment(new TelegaXPremiumActivity());
                break;
        }
    }

    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        return false;
    }
}
