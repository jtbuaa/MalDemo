package mal.demo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TestLocationService extends Service {
    public TestLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null)
            super.onStartCommand(intent, flags, startId);
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setClass(getApplicationContext(), Activity1.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i); // start activity as a demo. you can do more thing here as you know.

        return START_STICKY;
    }
}
