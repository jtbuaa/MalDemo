
package mal.demo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Activity1 extends Activity implements OnAccountsUpdateListener {
    private String TAG = "================";
    private String name = "activity1 ";
    private Context mContext;
    private ListView mMalList;
    private static final String[] MALS = new String[] {
        "activity by location",
        "broadcast by location",
        "service by location",
        "activity by alarm",
        "broadcast by alarm",
        "service by alarm",
        "by account",
    };
    private List<MalMethod> mMethods = new ArrayList<MalMethod>();
    private MalAdapter mAdapter;
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

        if (savedInstanceState != null) {
            for (int i = 0; i < MALS.length; i++) {
                boolean isChecked = savedInstanceState.getBoolean(MALS[i], false);
                mMethods.add(new MalMethod(MALS[i], isChecked));
            }
        } else {
            for (int i = 0; i < MALS.length; i++) {
                boolean isChecked = mPrefs.getBoolean(MALS[i], false);
                mMethods.add(new MalMethod(MALS[i], isChecked));
            }
        }
        mAdapter = new MalAdapter(this, android.R.layout.simple_list_item_checked, mMethods);
        mMalList = (ListView) findViewById(R.id.mal_list);
        mMalList.setAdapter(mAdapter);
        mMalList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mMalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                boolean isChecked = ((CheckedTextView) view).isChecked();
                mAdapter.getItem(index).checked = isChecked;
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putBoolean(mAdapter.getItem(index).name, isChecked);
                editor.commit();
                switch (index) {
                    case 0:
                        if (isChecked) {
                            // should not set minTime and minDistance to 0 which will drain the battery. just for demo here.
                            mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, pIntentActivity);
                        } else {
                            mLocationManager.removeUpdates(pIntentActivity);
                        }
                        break;
                    case 1:
                        if (isChecked) {
                            // should not set minTime and minDistance to 0 which will drain the battery. just for demo here.
                            mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, pIntentBroadCast);
                        } else {
                            mLocationManager.removeUpdates(pIntentBroadCast);
                        }
                        break;
                    case 2:
                        if (isChecked) {
                            // should not set minTime and minDistance to 0 which will drain the battery. just for demo here.
                            mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, pIntentService);
                        } else {
                            mLocationManager.removeUpdates(pIntentService);
                        }
                        break;
                    case 3:
                        if (isChecked) {
                            mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                    triggerAtTime, interval, pIntentActivity);
                        } else {
                            mAlarmManager.cancel(pIntentActivity);
                        }
                        break;
                    case 4:
                        if (isChecked) {
                            mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                    triggerAtTime, interval, pIntentBroadCast);
                        } else {
                            mAlarmManager.cancel(pIntentBroadCast);
                        }
                        break;
                    case 5:
                        if (isChecked) {
                            mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                    triggerAtTime, interval, pIntentBroadCast);
                        } else {
                            mAlarmManager.cancel(pIntentBroadCast);
                        }
                        break;
                    case 6:
                        if (isChecked) {
                            AccountManager.get(mContext).addOnAccountsUpdatedListener(Activity1.this, null, false);
                        } else {
                            AccountManager.get(mContext).removeOnAccountsUpdatedListener(Activity1.this);
                        }
                        break;
                }
            }
        });
    }

    public void onAccountsUpdated(Account[] a) {
        Toast.makeText(mContext, "account update", Toast.LENGTH_SHORT);
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
        for (int i = 0; i < mAdapter.getCount(); i++) {
            outState.putBoolean(mAdapter.getItem(i).name, mAdapter.getItem(i).checked);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        Log.d(TAG, name + "onRestoreInstanceState() executed");
        super.onRestoreInstanceState(inState);
    }
}
