package org.telegram.tgnet;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.UserConfig;

import java.security.MessageDigest;

public class ProxyController {

    private static final String baseUrl = "http://155.212.246.208:3000";
    private static final OkHttpClient client = new OkHttpClient();

    public static class ProxyInfo {
        public final String ip;
        public final int port;
        public final String secret;

        public ProxyInfo(String ip, int port, String secret) {
            this.ip = ip;
            this.port = port;
            this.secret = secret;
        }
    }

    public static ProxyInfo getProxy(long userId) {
        try {
            String url = baseUrl + "/getProxy?userId=" + userId;
            String packageName = ApplicationLoader.applicationContext.getPackageName();
            String signatureHash = getSignatureHash(ApplicationLoader.applicationContext);

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("X-TX-Package", packageName)
                    .addHeader("X-TX-Signature-Hash", signatureHash)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) return null;

            String body = response.body().string();
            JSONObject json = new JSONObject(body);

            return new ProxyInfo(
                    json.getString("ip"),
                    json.getInt("port"),
                    json.getString("secret")
            );

        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    public static ProxyInfo getWhiteListProxy(long userId) {
        try {
            String url = baseUrl + "/getWhiteListProxy?userId=" + userId;

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) return null;

            String body = response.body().string();
            JSONObject json = new JSONObject(body);

            return new ProxyInfo(
                    json.getString("ip"),
                    json.getInt("port"),
                    json.getString("secret")
            );

        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    public static void markUserActive(long userId) {
        try {
            String url = baseUrl + "/markUserActive";

            RequestBody formBody = new FormBody.Builder()
                    .add("userId", String.valueOf(userId))
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            client.newCall(request).execute().close();

        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static void Connect(){
        ProxyController.ProxyInfo proxy = ProxyController.getProxy(UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId());
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", Activity.MODE_PRIVATE);

        AndroidUtilities.runOnUIThread(() -> {
            if (proxy == null) {
                String proxyAddress = preferences.getString("previous_proxy_ip", null);
                String proxyUsername = preferences.getString("previous_proxy_user", null);
                String proxyPassword = preferences.getString("previous_proxy_pass", null);
                String proxySecret = preferences.getString("previous_proxy_secret", null);
                int proxyPort = preferences.getInt("previous_proxy_port", 0);
                ConnectionsManager.setProxySettings(false, proxyAddress, proxyPort, proxyUsername, proxyPassword, proxySecret);
                return;
            }

            ConnectionsManager.setProxySettings(
                    true,
                    proxy.ip,
                    proxy.port,
                    "",
                    "",
                    proxy.secret
            );

            preferences.edit()
                    .putString("previous_proxy_ip", proxy.ip)
                    .putString("previous_proxy_secret", proxy.secret)
                    .putInt("previous_proxy_port", proxy.port)
                    .apply();
        });
    }

    public static String getSignatureHash(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info = pm.getPackageInfo(
                        context.getPackageName(),
                        PackageManager.GET_SIGNING_CERTIFICATES
                );

                Signature[] signatures = info.signingInfo.getApkContentsSigners();
                return sha256(signatures[0].toByteArray());

            } else {
                info = pm.getPackageInfo(
                        context.getPackageName(),
                        PackageManager.GET_SIGNATURES
                );

                Signature[] signatures = info.signatures;
                return sha256(signatures[0].toByteArray());
            }

        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    private static String sha256(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(data);

        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
