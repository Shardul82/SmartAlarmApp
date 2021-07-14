package edu.niu.cs.z1888485.Utils;
/***************************************************************
 *                                                             *
 * CSCI 524      Graduate Semester Project         Spring 2021 *
 *                                                             *
 *                                                             *
 * Programmer:    Shardul Deepak Arjunwadkar(z1888485)         *
 *                Ashwanth Kalaiselvi anandhan(z1886742),      *
 *                                                             *
 *                                                             *
 * Due Date & Time:   04/22/2021 11:59PM                       *
 *                                                             *
 * Class name: AlarmBrodcast                                   *
 * Purpose: This class is used to broadcast the alarm message. *
 *                                                             *
 ***************************************************************/
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import edu.niu.cs.z1888485.R;
import edu.niu.cs.z1888485.activities.CreateAlarm;


import androidx.core.app.NotificationCompat;

import static androidx.core.app.NotificationCompat.DEFAULT_VIBRATE;

public class AlarmBrodcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        int id =  bundle.getInt("id");
        String text = bundle.getString("message");
        String voiceMessagePath = bundle.getString("VoiceMessage");
        String time =  bundle.getString("time");
        String date = bundle.getString("date");
        String time_date = date + " " + time;

        Uri soundUri=Uri.parse(voiceMessagePath);

        //Click on Notification
        Intent intent1 = new Intent(context, CreateAlarm.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra(CreateAlarm.EXTRA_MESSAGE, text);
        intent1.putExtra(CreateAlarm.EXTRA_DATE,date);
        intent1.putExtra(CreateAlarm.EXTRA_TIME, time);
        intent1.putExtra(CreateAlarm.EXTRA_ID, id);
        intent1.putExtra(CreateAlarm.EXTRA_VOICE_MESSAGE,voiceMessagePath );

        //Notification Builder
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_001");

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
       /* PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        contentView.setOnClickPendingIntent(R.id.flashButton, pendingSwitchIntent);*/
        contentView.setTextViewText(R.id.message, text);
        contentView.setTextViewText(R.id.date, time_date);
        mBuilder.setSmallIcon(R.drawable.ic_alarm_white_24dp);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(true);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.build().flags = Notification.FLAG_AUTO_CANCEL | Notification.PRIORITY_HIGH;
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.setContent(contentView);
       mBuilder.setContentIntent(pendingIntent);
        mBuilder.setDefaults(DEFAULT_VIBRATE);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id"+time_date;

            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);
      AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

       channel.setSound(soundUri, audioAttributes);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        Notification notification = mBuilder.build();

        notification.flags = Notification.FLAG_INSISTENT |  Notification.PRIORITY_HIGH  | Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(1, notification);

    }


}
