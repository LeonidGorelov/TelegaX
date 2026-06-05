package org.telegram.messenger;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.telegram.ui.LaunchActivity;

import java.util.Random;

public class NotificationsService extends Service {

    /*private static final String CHANNEL_ID = "telegax_push";
    private static final String[] cuteTexts = new String[]{
            "⊂(◉‿◉)つ",
            "(｡◕‿‿◕｡)",
            "(｡◕‿◕｡)",
            "¯\\_(ツ)_/¯",
            "\\(^-^)/",
            "＼(＾O＾)／",
            "\\(ᵔᵕᵔ)/",
            "ԅ(≖‿≖ԅ)",
            "(⊃｡•́‿•̀｡)⊃",
            "•ᴗ•",
            "(~‾▿‾)~",
            "｡^‿^｡",
            "(⁎˃ᆺ˂)",
            "(≧◡≦)",
            "\\(★ω★)/",
            "(✿◠‿◠)",
            "＼(٥⁀▽⁀ )／",
            "(*^.^*)",
            "( ` ω ´ )",
            "｡ﾟ･ (>﹏<) ･ﾟ｡",
            "╮(︶▽︶)╭",
            "(￣～￣;)",
            "(＾• ω •＾)"
    };*/

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationLoader.postInitApplication();

        /* if (MessagesController.getGlobalNotificationsSettings().getBoolean("useNotificationServiceTelegaX", false) &&
               Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannelCompat channel = new NotificationChannelCompat.Builder(
                    CHANNEL_ID,
                    NotificationManagerCompat.IMPORTANCE_DEFAULT
            )
                    .setName("TelegaX Push Service")
                    .setLightsEnabled(false)
                    .setVibrationEnabled(false)
                    .setSound(null, null)
                    .setShowBadge(false)
                    .build();

            NotificationManagerCompat.from(this).createNotificationChannel(channel);

            Notification notification;

            Intent startIntent = new Intent(this, LaunchActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    startIntent,
                    PendingIntent.FLAG_MUTABLE
            );

            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_transparent)
                    .setShowWhen(false)
                    .setOngoing(true)
                    .setContentTitle(null)
                    .setContentText(null)
                    .setPriority(NotificationCompat.PRIORITY_MIN)
                    .setCategory(NotificationCompat.CATEGORY_SERVICE)
                    .build();

            startForeground(9999, notification);
        } else {
            Notification notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_transparent)
                    .setShowWhen(false)
                    .setOngoing(true)
                    .setContentTitle(null)
                    .setContentText(null)
                    .setCategory(NotificationCompat.CATEGORY_SERVICE)
                    .setPriority(NotificationCompat.PRIORITY_MIN)
                    .build();

            startForeground(9999, notification);
        }*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        SharedPreferences preferences = MessagesController.getGlobalNotificationsSettings();
        if (preferences.getBoolean("pushService", true)) {
            Intent intent = new Intent("org.telegram.start");
            intent.setPackage(getPackageName());
            sendBroadcast(intent);
        }
    }
}
