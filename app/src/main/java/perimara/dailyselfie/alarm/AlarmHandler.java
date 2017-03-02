package perimara.dailyselfie.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by periklismaravelias on 28/02/17.
 */
public class AlarmHandler {

    Context mContext;
    AlarmManager mAlarmManager;
    PendingIntent mPendingIntent;

    /**
     * Constructor used to create an AlarmHandler instance
     * @param context
     */
    public AlarmHandler(Context context){
        mContext = context;
        mAlarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.setAction(AlarmReceiver.ACTION_ALARM_RECEIVER);
        mPendingIntent = PendingIntent.getBroadcast(mContext, AlarmReceiver.REQUEST_CODE_ALARM_RECEIVER, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    /**
     * Used to set an alarm that goes off every x seconds
     * @param x
     */
    public void SetAlarmEveryXSeconds(int x){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, x);
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), x * 1000, mPendingIntent);
    }

}
