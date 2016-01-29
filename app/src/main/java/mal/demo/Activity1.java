
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
    Context mContext;

    private LocationManager mLocationManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, name + "onCreate() executed");
        mContext = this;
        setContentView(R.layout.main);
        setTitle(name);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn1:
                        Intent intent = new Intent("jt.action.locationChange");
                        intent.setClass(getApplicationContext(), LocationReceiver.class);
                        PendingIntent pintent = PendingIntent.getBroadcast(mContext, 111, intent, 0);
                        // should not set minTime and minDistance to 0 which will drain the battery. just for demo here.
                        mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, pintent);
                        break;
                    case R.id.btn2:
                        intent = new Intent(Intent.ACTION_MAIN);
                        intent.setClass(mContext, Activity1.class);
                        pintent = PendingIntent.getActivity(mContext, 111, intent, 0);
                        // should not set minTime and minDistance to 0 which will drain the battery. just for demo here.
                        mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, pintent);
                        break;
                    case R.id.btn3:
                        intent = new Intent(mContext, MyLocationService.class);
                        pintent = PendingIntent.getService(mContext, 111, intent, 0);
                        // should not set minTime and minDistance to 0 which will drain the battery. just for demo here.
                        mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, pintent);
                        break;
                }
            }
        };
        Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(listener);
        Button btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(listener);
        Button btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(listener);
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
