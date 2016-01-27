package mal.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jingtao on 16-1-25.
 */
public class LocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.setClass(context, Activity1.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i); // start activity as a demo. you can do more thing here as you know.
    }
}