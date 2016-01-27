package mal.demo;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MyLocationService extends Service {
    public MyLocationService() {
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
        updateLocation();
        return START_STICKY;
    }

    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location l) {
            Log.d("============", "onLocationChanged");
            mLocation = l;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    private void updateLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Intent intent = new Intent("jt.action.locationChange");
        intent.setClass(getApplicationContext(), LocationReceiver.class);
        PendingIntent pintent = PendingIntent.getBroadcast(this, 111, intent, 0);
        mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, pintent);
    }
}
