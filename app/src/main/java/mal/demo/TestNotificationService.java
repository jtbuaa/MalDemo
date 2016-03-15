package mal.demo;

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
        Activity1.startActivity(getApplicationContext(), Activity1.BY_NOTIFICATION_LISTENER);
    }
}