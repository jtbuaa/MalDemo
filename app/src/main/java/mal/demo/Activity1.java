
package mal.demo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class Activity1 extends Activity {
    String TAG = "================";
    String name = "activity1 ";
    Context mContext;
    CheckBox cbx1, cbx2, cbx3, cbx4, cbx5, cbx6;
    private SharedPreferences mPrefs;

    private LocationManager mLocationManager;
    private AlarmManager mAlarmManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, name + "onCreate() executed");
        mContext = this;
        setContentView(R.layout.main);
        setTitle(name);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final int triggerAtTime = (int) (SystemClock.elapsedRealtime() + 20 * 1000);
        final int interval = 60 * 1000;

        Intent intent = new Intent("jt.action.locationChange");
        intent.setClass(getApplicationContext(), LocationReceiver.class);
        final PendingIntent pIntentBroadCast = PendingIntent.getBroadcast(mContext, 111, intent, 0);

        intent = new Intent(Intent.ACTION_MAIN);
        intent.setClass(mContext, Activity1.class);
        final PendingIntent pIntentActivity = PendingIntent.getActivity(mContext, 111, intent, 0);

        intent = new Intent(mContext, MyLocationService.class);
        final PendingIntent pIntentService = PendingIntent.getService(mContext, 111, intent, 0);

        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cbx = (CheckBox)v;
                SharedPreferences.Editor editor = mPrefs.edit();
                switch (cbx.getId()) {
                    case R.id.cbx1:
                        editor.putBoolean("key1", cbx.isChecked());
                        if (cbx.isChecked()) {
                            // should not set minTime and minDistance to 0 which will drain the battery. just for demo here.
                            mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, pIntentActivity);
                        } else {
                            mLocationManager.removeUpdates(pIntentActivity);
                        }
                        break;
                    case R.id.cbx2:
                        editor.putBoolean("key2", cbx.isChecked());
                        if (cbx.isChecked()) {
                            // should not set minTime and minDistance to 0 which will drain the battery. just for demo here.
                            mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, pIntentBroadCast);
                        } else {
                            mLocationManager.removeUpdates(pIntentBroadCast);
                        }
                        break;
                    case R.id.cbx3:
                        editor.putBoolean("key3", cbx.isChecked());
                        if (cbx.isChecked()) {
                            // should not set minTime and minDistance to 0 which will drain the battery. just for demo here.
                            mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, pIntentService);
                        } else {
                            mLocationManager.removeUpdates(pIntentService);
                        }
                    case R.id.cbx4:
                        editor.putBoolean("key4", cbx.isChecked());
                        if (cbx.isChecked()) {
                            mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                    triggerAtTime, interval, pIntentActivity);
                        } else {
                            mAlarmManager.cancel(pIntentActivity);
                        }
                        break;
                    case R.id.cbx5:
                        editor.putBoolean("key5", cbx.isChecked());
                        if (cbx.isChecked()) {
                            mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                    triggerAtTime, interval, pIntentBroadCast);
                        } else {
                            mAlarmManager.cancel(pIntentBroadCast);
                        }
                        break;
                    case R.id.cbx6:
                        editor.putBoolean("key6", cbx.isChecked());
                        if (cbx.isChecked()) {
                            mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                    triggerAtTime, interval, pIntentService);
                        } else {
                            mAlarmManager.cancel(pIntentService);
                        }
                        break;
                }
                editor.commit();
            }
        };
        cbx1 = (CheckBox) findViewById(R.id.cbx1);
        cbx1.setOnClickListener(listener);
        cbx2 = (CheckBox) findViewById(R.id.cbx2);
        cbx2.setOnClickListener(listener);
        cbx3 = (CheckBox) findViewById(R.id.cbx3);
        cbx3.setOnClickListener(listener);
        cbx4 = (CheckBox) findViewById(R.id.cbx4);
        cbx4.setOnClickListener(listener);
        cbx5 = (CheckBox) findViewById(R.id.cbx5);
        cbx5.setOnClickListener(listener);
        cbx6 = (CheckBox) findViewById(R.id.cbx6);
        cbx6.setOnClickListener(listener);
        if (savedInstanceState != null) {
            cbx1.setChecked(savedInstanceState.getBoolean("key1", false));
            cbx2.setChecked(savedInstanceState.getBoolean("key2", false));
            cbx3.setChecked(savedInstanceState.getBoolean("key3", false));
            cbx4.setChecked(savedInstanceState.getBoolean("key4", false));
            cbx5.setChecked(savedInstanceState.getBoolean("key5", false));
            cbx6.setChecked(savedInstanceState.getBoolean("key6", false));
        } else {
            cbx1.setChecked(mPrefs.getBoolean("key1", false));
            cbx2.setChecked(mPrefs.getBoolean("key2", false));
            cbx3.setChecked(mPrefs.getBoolean("key3", false));
            cbx4.setChecked(mPrefs.getBoolean("key4", false));
            cbx5.setChecked(mPrefs.getBoolean("key5", false));
            cbx6.setChecked(mPrefs.getBoolean("key6", false));
        }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, name + "onSaveInstanceState() executed");
        outState.putBoolean("key1", cbx1.isChecked());
        outState.putBoolean("key2", cbx2.isChecked());
        outState.putBoolean("key3", cbx3.isChecked());
        outState.putBoolean("key4", cbx4.isChecked());
        outState.putBoolean("key5", cbx5.isChecked());
        outState.putBoolean("key6", cbx6.isChecked());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        Log.d(TAG, name + "onRestoreInstanceState() executed");
        super.onRestoreInstanceState(inState);
    }
}
