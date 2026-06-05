package org.telegram.ui.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.text.style.CharacterStyle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.MessageObject;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.NewsFeedActivity;
import org.telegram.ui.ProfileActivity;

import java.util.ArrayList;

public class NewsFeedAdapter extends RecyclerListView.SelectionAdapter {

    private final Context context;
    private final BaseFragment fragment;
    private final ArrayList<MessageObject> messages;
    private final int currentAccount;

    public NewsFeedAdapter(Context context, BaseFragment fragment, ArrayList<MessageObject> messages, int currentAccount) {
        this.context = context;
        this.fragment = fragment;
        this.messages = messages;
        this.currentAccount = currentAccount;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public boolean isEnabled(RecyclerView.ViewHolder holder) {
        return true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ChatMessageCell cell = new ChatMessageCell(context, currentAccount);

        cell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));

        cell.setDelegate(new ChatMessageCell.ChatMessageCellDelegate() {

            @Override
            public void didPressUrl(ChatMessageCell cell, CharacterStyle url, boolean longPress) {
                Browser.openUrl(cell.getContext(), url.toString());
            }

            @Override
            public void didPressWebPage(ChatMessageCell cell, TLRPC.WebPage webpage, String url, boolean safe) {
                Browser.openUrl(cell.getContext(), url);
            }

            @Override
            public void needOpenWebView(MessageObject message, String url, String title, String description, String originalUrl, int w, int h) {
                Browser.openUrl(fragment.getParentActivity(), url);
            }

            @Override
            public void didLongPress(ChatMessageCell cell, float x, float y) {
                /*MessageObject msg = cell.getMessageObject();
                if (msg == null) return;

                if (fragment instanceof NewsFeedActivity) {
                    ((NewsFeedActivity) fragment).showMessageContextMenu(msg, cell);
                }*/
            }

            @Override
            public void didPressUserAvatar(ChatMessageCell cell, TLRPC.User user,float touchX, float touchY, boolean asForward) {
                if (user == null) return;
                Bundle args = new Bundle();
                args.putLong("user_id", user.id);
                fragment.presentFragment(new ProfileActivity(args));
            }

            @Override
            public void didPressReplyMessage(ChatMessageCell cell, int id, float x, float y, boolean longpress) {
            }

            @Override
            public boolean canPerformActions() {
                return true;
            }
        });

        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cell.setLayoutParams(params);

        return new RecyclerListView.Holder(cell);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessageCell cell = (ChatMessageCell) holder.itemView;
        MessageObject msg = messages.get(position);

        // setMessageObject(MessageObject, GroupedMessages, boolean, boolean, boolean)
        cell.setMessageObject(msg, null, false, false, false);

        cell.setOnClickListener(v -> {
            long dialogId = msg.getDialogId();

            Bundle args = new Bundle();
            args.putLong("dialog_id", dialogId);

            fragment.presentFragment(new ChatActivity(args));
        });
    }
}
