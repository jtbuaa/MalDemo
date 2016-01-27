
package mal.demo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Activity1 extends Activity {
    String TAG = "================";
    String name = "activity1 ";
    Context context;

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
        intent.setClass(context, LocationReceiver.class);
        PendingIntent pintent = PendingIntent.getBroadcast(this, 111, intent, 0);
        // should not set minTime and minDistance to 0 which will drain the battery. just for demo here.
        mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, pintent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, name + "onCreate() executed");
        context = this;
        setContentView(R.layout.main);
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, name + "start service");
                Intent intent = new Intent(context, MyLocationService.class);
                startService(intent);
                //updateLocation();
            }
        });
        this.setTitle(name);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, name + "onStart() executed");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, name + "onRestart() executed");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, name + "onResume() executed");
    }

    @Override
    protected void onPause() {
        Log.d(TAG, name + "onPause() executed");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, name + "onStop() executed");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, name + "onDestroy() executed");
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, name + "onNewIntent() executed");

        super.onNewIntent(intent);
    }
}
