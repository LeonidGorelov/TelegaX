/*
 * This is the source code of Telegram for Android v. 5.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2018.
 */

package org.telegram.messenger;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.telegram.tgnet.ConnectionsManager;

import java.util.Map;

public class GcmPushListenerService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
        //String from = message.getFrom();
        Map<String, String> data = message.getData();
        //long time = message.getSentTime();

        if (BuildVars.LOGS_ENABLED) {
            //FileLog.d("FCM received data: " + data + " from: " + from);
            FileLog.d("FCM received data");
        }

        ApplicationLoader.postInitApplication();

        for (int i = 0; i < UserConfig.MAX_ACCOUNT_COUNT; i++) {
            if (UserConfig.getInstance(i).isClientActivated()) {
                ConnectionsManager.getInstance(i).resumeNetworkMaybe();
                MessagesController.getInstance(i).getDifference();
            }
        }

        //PushListenerController.processRemoteMessage(PushListenerController.PUSH_TYPE_FIREBASE, data.get("p"), time);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        AndroidUtilities.runOnUIThread(() -> {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("Refreshed FCM token: " + token);
            }

            ApplicationLoader.postInitApplication();
            PushListenerController.sendRegistrationToServer(PushListenerController.PUSH_TYPE_FIREBASE, token);
        });
    }
}
