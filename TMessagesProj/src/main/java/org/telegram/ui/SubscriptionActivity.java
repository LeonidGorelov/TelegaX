package org.telegram.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;

public class SubscriptionActivity extends Activity {

    public static SubscriptionActivity instance;
    private static final long CHANNEL_ID = 3982213462L;

    private View loadingView;
    private View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        setContentView(createView());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (instance == this) instance = null;
    }


    private View createView() {
        Context context = this;

        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(
                AndroidUtilities.dp(32),
                AndroidUtilities.dp(32),
                AndroidUtilities.dp(32),
                AndroidUtilities.dp(32)
        );
        root.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));

        root.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        LinearLayout content = new LinearLayout(context);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER);
        contentView = content;

        ImageView logo = new ImageView(context);
        logo.setImageDrawable(Theme.dialogs_archiveAvatarDrawable);
        LinearLayout.LayoutParams lpLogo = new LinearLayout.LayoutParams(
                AndroidUtilities.dp(96),
                AndroidUtilities.dp(96)
        );
        lpLogo.bottomMargin = AndroidUtilities.dp(24);
        content.addView(logo, lpLogo);

        TextView title = new TextView(context);
        title.setText(LocaleController.getString("SubscribeTitle", R.string.SubscribeTitle));
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
        title.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        title.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        title.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lpTitle = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lpTitle.bottomMargin = AndroidUtilities.dp(12);
        content.addView(title, lpTitle);

        TextView subtitle = new TextView(context);
        subtitle.setText(LocaleController.getString("SubscribeSubtitle", R.string.SubscribeSubtitle));
        subtitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        subtitle.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
        subtitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lpSubtitle = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lpSubtitle.bottomMargin = AndroidUtilities.dp(32);
        content.addView(subtitle, lpSubtitle);

        Button buttonJoin = new Button(context);
        buttonJoin.setText(LocaleController.getString("SubscribeButton", R.string.SubscribeButton));
        buttonJoin.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
        buttonJoin.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        buttonJoin.setBackground(Theme.createSimpleSelectorRoundRectDrawable(
                AndroidUtilities.dp(12),
                Theme.getColor(Theme.key_featuredStickers_addButton),
                Theme.getColor(Theme.key_featuredStickers_addButtonPressed)
        ));
        buttonJoin.setPadding(
                AndroidUtilities.dp(32),
                AndroidUtilities.dp(12),
                AndroidUtilities.dp(32),
                AndroidUtilities.dp(12)
        );
        buttonJoin.setOnClickListener(v -> openChannel());
        content.addView(buttonJoin);

        root.addView(content);

        return root;
    }

    private void openChannel() {
        try {
            /*LaunchActivity.isSubscribed = true;
            MessagesController.getGlobalMainSettings().edit().putBoolean("is_subscribed", true).apply();*/
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=TelegaX_Ru"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Не удалось открыть канал", Toast.LENGTH_LONG).show();
        }
    }
}
