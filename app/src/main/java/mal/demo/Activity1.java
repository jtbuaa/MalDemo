
package mal.demo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Activity1 extends Activity implements OnAccountsUpdateListener {
    private String TAG = "================";
    private String name = "activity1 ";
    private Context mContext;
    private ListView mMalList;
    private static final String A_BY_LOCATION = "activity by location";
    private static final String B_BY_LOCATION = "broadcast by location";
    private static final String S_BY_LOCATION = "service by location";
    private static final String A_BY_ALARM = "activity by alarm";
    private static final String B_BY_ALARM = "broadcast by alarm";
    private static final String S_BY_ALARM = "service by alarm";
    private static final String BY_ACCOUNT = "by account";
    public static final String FAKE_SMS = "test fake sms";
    private static final String BY_TELEPHONY = "by telephony?";
    private static final String BY_JOB_SCHEDULER = "by job Scheduler";
    private static final String[] MALS = new String[] {
            A_BY_LOCATION,
            B_BY_LOCATION,
            S_BY_LOCATION,
            A_BY_ALARM,
            B_BY_ALARM,
            S_BY_ALARM,
            BY_ACCOUNT,
            FAKE_SMS,
            BY_TELEPHONY,
            BY_JOB_SCHEDULER,
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

        mServiceComponent = new ComponentName(this, TestJobService.class);
        Intent startServiceIntent = new Intent(mContext, TestJobService.class);
        startServiceIntent.putExtra("messenger", new Messenger(mHandler));
        startService(startServiceIntent);

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
        mAdapter = new MalAdapter(this, R.layout.list_raw, mMethods);
        mMalList = (ListView) findViewById(R.id.mal_list);
        mMalList.setAdapter(mAdapter);
        mMalList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mMalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                CheckedTextView ctv = (CheckedTextView) view.findViewById(R.id.list_row_ctv);
                boolean isChecked = !ctv.isChecked();
                ctv.setChecked(isChecked);
                mAdapter.getItem(index).checked = isChecked;
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putBoolean(mAdapter.getItem(index).name, isChecked);
                editor.commit();
                if (A_BY_LOCATION.equals(MALS[index])) {
                    if (isChecked) {
                        // should not set minTime and minDistance to 0 which will drain the battery. just for demo here.
                        mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, pIntentActivity);
                    } else {
                        mLocationManager.removeUpdates(pIntentActivity);
                    }
                } else if (B_BY_LOCATION.equals(MALS[index])) {
                    if (isChecked) {
                        // should not set minTime and minDistance to 0 which will drain the battery. just for demo here.
                        mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, pIntentBroadCast);
                    } else {
                        mLocationManager.removeUpdates(pIntentBroadCast);
                    }
                } else if (S_BY_LOCATION.equals(MALS[index])) {
                    if (isChecked) {
                        // should not set minTime and minDistance to 0 which will drain the battery. just for demo here.
                        mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, pIntentService);
                    } else {
                        mLocationManager.removeUpdates(pIntentService);
                    }
                } else if (A_BY_ALARM.equals(MALS[index])) {
                    if (isChecked) {
                        mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                triggerAtTime, interval, pIntentActivity);
                    } else {
                        mAlarmManager.cancel(pIntentActivity);
                    }
                } else if (B_BY_ALARM.equals(MALS[index])) {
                    if (isChecked) {
                        mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                triggerAtTime, interval, pIntentBroadCast);
                    } else {
                        mAlarmManager.cancel(pIntentBroadCast);
                    }
                } else if (S_BY_ALARM.equals(MALS[index])) {
                    if (isChecked) {
                        mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                triggerAtTime, interval, pIntentBroadCast);
                    } else {
                        mAlarmManager.cancel(pIntentBroadCast);
                    }
                } else if (BY_ACCOUNT.equals(MALS[index])) {
                    if (isChecked) {
                        AccountManager.get(mContext).addOnAccountsUpdatedListener(Activity1.this, null, false);
                    } else {
                        AccountManager.get(mContext).removeOnAccountsUpdatedListener(Activity1.this);
                    }
                } else if (BY_JOB_SCHEDULER.equals(MALS[index])) {
                    if (isChecked) {
                        scheduleJob();
                    } else {
                        cancelAllJobs();
                    }
                }
            }
        });
    }

    //http://stackoverflow.com/questions/12335642/create-pdu-for-android-that-works-with-smsmessage-createfrompdu-gsm-3gpp
    public static void createFakeSms(Context context, String sender,
                                      String body) {
        byte[] pdu = null;
        byte[] scBytes = PhoneNumberUtils
                .networkPortionToCalledPartyBCD("0000000000");
        byte[] senderBytes = PhoneNumberUtils
                .networkPortionToCalledPartyBCD(sender);
        int lsmcs = scBytes.length;
        byte[] dateBytes = new byte[7];
        Calendar calendar = new GregorianCalendar();
        dateBytes[0] = reverseByte((byte) (calendar.get(Calendar.YEAR)));
        dateBytes[1] = reverseByte((byte) (calendar.get(Calendar.MONTH) + 1));
        dateBytes[2] = reverseByte((byte) (calendar.get(Calendar.DAY_OF_MONTH)));
        dateBytes[3] = reverseByte((byte) (calendar.get(Calendar.HOUR_OF_DAY)));
        dateBytes[4] = reverseByte((byte) (calendar.get(Calendar.MINUTE)));
        dateBytes[5] = reverseByte((byte) (calendar.get(Calendar.SECOND)));
        dateBytes[6] = reverseByte((byte) ((calendar.get(Calendar.ZONE_OFFSET) + calendar
                .get(Calendar.DST_OFFSET)) / (60 * 1000 * 15)));
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            bo.write(lsmcs);
            bo.write(scBytes);
            bo.write(0x04);
            bo.write((byte) sender.length());
            bo.write(senderBytes);
            bo.write(0x00);
            bo.write(0x00); // encoding: 0 for default 7bit
            bo.write(dateBytes);
            try {
                String sReflectedClassName = "com.android.internal.telephony.GsmAlphabet";
                Class cReflectedNFCExtras = Class.forName(sReflectedClassName);
                Method stringToGsm7BitPacked = cReflectedNFCExtras.getMethod(
                        "stringToGsm7BitPacked", new Class[] { String.class });
                stringToGsm7BitPacked.setAccessible(true);
                byte[] bodybytes = (byte[]) stringToGsm7BitPacked.invoke(null,
                        body);
                bo.write(bodybytes);
            } catch (Exception e) {
            }

            pdu = bo.toByteArray();
        } catch (IOException e) {
        }

        Intent intent = new Intent();
        intent.setClassName("com.android.mms",
                "com.android.mms.transaction.SmsReceiverService");
        intent.setAction("android.provider.Telephony.SMS_RECEIVED");
        intent.putExtra("pdus", new Object[] { pdu });
        intent.putExtra("format", "3gpp");
        try {
            context.startService(intent);
        } catch(SecurityException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private static byte reverseByte(byte b) {
        return (byte) ((b & 0xF0) >> 4 | (b & 0x0F) << 4);
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

    ComponentName mServiceComponent;
    /** Service object to interact scheduled jobs. */
    TestJobService mTestService;

    private static int kJobId = 0;
    public static final int MSG_UNCOLOUR_START = 0;
    public static final int MSG_UNCOLOUR_STOP = 1;
    public static final int MSG_SERVICE_OBJ = 2;
    Handler mHandler = new Handler(/* default looper */) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UNCOLOUR_START:
                    //mShowStartView.setBackgroundColor(defaultColor);
                    break;
                case MSG_UNCOLOUR_STOP:
                    //mShowStopView.setBackgroundColor(defaultColor);
                    break;
                case MSG_SERVICE_OBJ:
                    mTestService = (TestJobService) msg.obj;
                    mTestService.setUiCallback(Activity1.this);
            }
        }
    };

    private boolean ensureTestService() {
        if (mTestService == null) {
            Toast.makeText(Activity1.this, "Service null, never got callback?",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void scheduleJob() {
        if (!ensureTestService()) {
            return;
        }

        JobInfo.Builder builder = new JobInfo.Builder(kJobId++, mServiceComponent);

        boolean requiresUnmetered = false;
        boolean requiresAnyConnectivity = true;
        if (requiresUnmetered) {
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        } else if (requiresAnyConnectivity) {
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        }
        builder.setRequiresDeviceIdle(false);
        builder.setRequiresCharging(false);

        mTestService.scheduleJob(builder.build());
    }

    public void cancelAllJobs() {
        JobScheduler tm =
                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.cancelAll();
    }

    /**
     * UI onclick listener to call jobFinished() in our service.
     */
    public void finishJob(View v) {
        if (!ensureTestService()) {
            return;
        }
        mTestService.callJobFinished();
        Toast.makeText(mContext, "job canceled", Toast.LENGTH_SHORT).show();
    }

    public void onReceivedStartJob(JobParameters params) {
        Toast.makeText(mContext, "Executing: " + params.getJobId() + " " + params.getExtras(), Toast.LENGTH_SHORT).show();
    }

    public void onReceivedStopJob() {
        Toast.makeText(mContext, "job stop", Toast.LENGTH_SHORT).show();
    }
}
