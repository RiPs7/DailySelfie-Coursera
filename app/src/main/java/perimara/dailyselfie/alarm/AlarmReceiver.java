package perimara.dailyselfie.alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import perimara.dailyselfie.R;
import perimara.dailyselfie.activities.MainActivity;

/**
 * Created by periklismaravelias on 28/02/17.
 */
public class AlarmReceiver extends BroadcastReceiver{

    public static final String ACTION_ALARM_RECEIVER = "DailySelfieNotificationAction";
    public static final int REQUEST_CODE_ALARM_RECEIVER = 1001;

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = 1234;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_camera)
                .setContentTitle("Daily Selfie")
                .setContentText("Time for another selfie!!!");
        builder.setAutoCancel(true);
        // Creates an explicit intent for MainActivity
        Intent resultIntent = new Intent(context, MainActivity.class);
        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the MainActivity leads out of the application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the MainActivity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        // Adds the Pending Intent to the notification builder content
        builder.setContentIntent(resultPendingIntent);
        // Display the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }
}
