package org.telegram.ui;

import android.view.View;
import android.view.ViewGroup;

import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;

import java.util.ArrayList;

public class TelegaXPremiumActivity extends UniversalFragment {

    private static final int ITEM_LOCAL_PREMIUM_ENABLED = 1;
    private static final int ITEM_HIDE_PREMIUM_ICON = 2;

    private boolean localPremiumEnabled;
    private boolean hidePremiumIcon;

    @Override
    protected CharSequence getTitle() {
        String premiumText = LocaleController.getString(R.string.TelegramPremium);
        return premiumText;
    }

    private void loadSettings() {
        localPremiumEnabled = MessagesController.getGlobalMainSettings().getBoolean("local_premium_enabled", true);
        hidePremiumIcon = MessagesController.getGlobalMainSettings().getBoolean("hide_premium_icon", false);
    }

    private void saveSettings() {
        MessagesController.getGlobalMainSettings().edit()
                .putBoolean("local_premium_enabled", localPremiumEnabled)
                .putBoolean("hide_premium_icon", hidePremiumIcon)
                .apply();
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        loadSettings();

        TextCheckCell proxyCell = new TextCheckCell(getContext());
        String useProxyText = LocaleController.getString("LocalPremiumEnabled", R.string.LocalPremiumEnabled);
        proxyCell.setTextAndCheck(useProxyText, localPremiumEnabled, false);
        items.add(UItem.asCustom(proxyCell).setId(ITEM_LOCAL_PREMIUM_ENABLED));

        TextCheckCell ruCell = new TextCheckCell(getContext());
        String useRussianProxy = LocaleController.getString("HidePremiumIcon", R.string.HidePremiumIcon);
        ruCell.setTextAndCheck(useRussianProxy, hidePremiumIcon, false);
        items.add(UItem.asCustom(ruCell).setId(ITEM_HIDE_PREMIUM_ICON));
    }


    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {

        switch (item.id) {

            case ITEM_LOCAL_PREMIUM_ENABLED: {
                localPremiumEnabled = !localPremiumEnabled;
                saveSettings();

                TextCheckCell cell = (TextCheckCell) ((ViewGroup) view).getChildAt(0);
                cell.setChecked(localPremiumEnabled);
                break;
            }

            case ITEM_HIDE_PREMIUM_ICON: {
                hidePremiumIcon = !hidePremiumIcon;
                saveSettings();

                TextCheckCell cell = (TextCheckCell) ((ViewGroup) view).getChildAt(0);
                cell.setChecked(hidePremiumIcon);
                break;
            }
        }
    }

    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        return false;
    }
}
