package mal.demo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by jingtao on 16-3-15.
 */
public class TestNotificationService extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i("zpf", "open"+"-----"+sbn.toString());
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("zpf", "shut" + "-----" + sbn.toString());
    }

    @Override
    public void onListenerConnected() {
        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isChecked = prefs.getBoolean(Activity1.BY_NOTIFICATION_LISTENER, false);
        if (isChecked) {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setClass(getApplicationContext(), Activity1.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}