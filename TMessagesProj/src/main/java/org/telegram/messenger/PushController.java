package org.telegram.messenger;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PushController {

    private static final OkHttpClient client = new OkHttpClient();
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private static final String BACKEND_URL = "http://155.212.246.208:3000/registerToken";

    public static void sendTokenToBackend(String token) {
        int account = UserConfig.selectedAccount;
        long userId = UserConfig.getInstance(account).getClientUserId();

        if (userId == 0) {
            FileLog.d("PushService: userId == 0, skip token send");
            return;
        }

        String json = "{"
                + "\"user_id\":" + userId + ","
                + "\"token\":\"" + token + "\""
                + "}";

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BACKEND_URL)
                .post(body)
                .build();

        executor.execute(() -> {
            try {
                client.newCall(request).execute().close();
                FileLog.d("PushService: token sent");
            } catch (Throwable e) {
                FileLog.e(e);
            }
        });
    }
}
