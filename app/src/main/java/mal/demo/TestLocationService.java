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
        Activity1.startActivity(getApplicationContext(), null);

        return START_STICKY;
    }
}
