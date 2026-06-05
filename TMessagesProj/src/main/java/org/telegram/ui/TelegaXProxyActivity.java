package org.telegram.ui;

import android.view.View;
import android.view.ViewGroup;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ProxyController;
import org.telegram.tgnet.TelegaXProxyVars;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;

import java.util.ArrayList;

public class TelegaXProxyActivity extends UniversalFragment {

    private static final int ITEM_USE_PROXY = 1;
    private static final int ITEM_USE_RU_SERVER = 2;

    private boolean useProxy;
    //private boolean useRuServer;

    private static String currentProxyIp;
    private static int currentProxyPort;
    private static String currentProxySecret;

    @Override
    protected CharSequence getTitle() {
        String proxyText = LocaleController.getString(R.string.Proxy);
        return proxyText;
    }

    private void loadSettings() {
        useProxy = MessagesController.getGlobalMainSettings().getBoolean("proxy_enabled", true);
        //useRuServer = MessagesController.getGlobalMainSettings().getBoolean("proxy_ru_server_enabled", true);
        /*currentProxyIp = MessagesController.getGlobalMainSettings().getString("proxy_ip", null);
        currentProxyPort = MessagesController.getGlobalMainSettings().getInt("proxy_port", 0);
        currentProxySecret = MessagesController.getGlobalMainSettings().getString("proxy_secret", null);*/
    }

    private void saveSettings() {
        MessagesController.getGlobalMainSettings().edit()
                .putBoolean("proxy_enabled", useProxy)
                //.putBoolean("proxy_ru_server_enabled", useRuServer)
                //.putString("proxy_ip", currentProxyIp)
                //.putInt("proxy_port", currentProxyPort)
                //.putString("proxy_secret", currentProxySecret)
                .apply();
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        loadSettings();

        TextCheckCell proxyCell = new TextCheckCell(getContext());
        String useProxyText = LocaleController.getString("UseProxy", R.string.UseProxy);
        proxyCell.setTextAndCheck(useProxyText, useProxy, false);
        items.add(UItem.asCustom(proxyCell).setId(ITEM_USE_PROXY));

        /*TextCheckCell ruCell = new TextCheckCell(getContext());
        String useRussianProxy = LocaleController.getString("UseRussianProxy", R.string.UseRussianProxy);
        String useRussianProxyDescription = LocaleController.getString("UseRussianProxyDescription",
                R.string.UseRussianProxyDescription);
        ruCell.setTextAndValueAndCheck(useRussianProxy, useRussianProxyDescription, useRuServer, true, false);
        items.add(UItem.asCustom(ruCell).setId(ITEM_USE_RU_SERVER));*/
    }


    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {

        switch (item.id) {

            case ITEM_USE_PROXY: {
                useProxy = !useProxy;
                saveSettings();

                TextCheckCell cell = (TextCheckCell) ((ViewGroup) view).getChildAt(0);
                cell.setChecked(useProxy);

                Connect();
                break;
            }

            /*case ITEM_USE_RU_SERVER: {
                useRuServer = !useRuServer;

                currentProxyIp = useRuServer ? TelegaXProxyVars.proxyIpRu : TelegaXProxyVars.proxyIp;
                currentProxyPort = useRuServer ? TelegaXProxyVars.proxyPortRu : TelegaXProxyVars.proxyPort;
                currentProxySecret = useRuServer ? TelegaXProxyVars.proxySecretRu : TelegaXProxyVars.proxySecret;

                saveSettings();

                TextCheckCell cell = (TextCheckCell) ((ViewGroup) view).getChildAt(0);
                cell.setChecked(useRuServer);

                Connect();
                break;
            }*/
        }
    }

    private void Connect() {
        if (!useProxy) {
            ConnectionsManager.setProxySettings(false, null, 0, null, null, null);
            return;
        }

        Utilities.globalQueue.postRunnable(ProxyController::Connect);
    }


    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        return false;
    }
}
