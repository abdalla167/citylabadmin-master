package com.muhammed.citylabadmin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        sendNotification(remoteMessage.getNotification().getTitle()
                , remoteMessage.getNotification().getBody());

    }

    private void sendNotification(String title, String messageBody) {


        Spannable sb = new SpannableString(title);
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        Intent intent;
        if (true) {
            intent = new Intent(this, SplashScreen.class);
        }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);



            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            String channelId = getString(R.string.default_notification_channel_id);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Notfication",
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("test channel");
                channel.enableLights(true);
                channel.setLightColor(Color.BLUE);
                channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationManager.createNotificationChannel(channel);
            }

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.logocitylab)
                            .setVibrate(new long[]{0, 1000, 500, 1000})
                            .setStyle(new NotificationCompat.InboxStyle()

                                    .setBigContentTitle(sb)
                                    .addLine(messageBody)
                            ).setContentText(messageBody)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);


            // Since android Oreo notification channel is needed.
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("TAG", "onNewToken: " );
    }
}
