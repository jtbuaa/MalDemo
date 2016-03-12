package mal.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;

import java.util.List;

/**
 * Created by jingtao on 2/28/16.
 */
public class MalAdapter extends ArrayAdapter<MalMethod> {
    private int mResourceId;
    private LayoutInflater mInflater;
    private Context mContext;

    public MalAdapter(Context context, int resId, List<MalMethod> methods) {
        super(context, resId, methods);
        mResourceId = resId;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MalMethod malMethod = getItem(position);
        boolean isButton = Activity1.FAKE_SMS.equals(malMethod.name);
        if (convertView == null) {
            if (isButton)
                convertView = mInflater.inflate(R.layout.list_button, null);
            else
                convertView = mInflater.inflate(mResourceId, null);
        }
        if (isButton) {
            Button button = (Button) convertView.findViewById(R.id.list_row_button);
            button.setText(malMethod.name);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity1.createFakeSms(mContext, "10086", "wow, you are out of data quota");
                }
            });
        } else {
            CheckedTextView methodView = (CheckedTextView) convertView.findViewById(R.id.list_row_ctv);
            methodView.setText(malMethod.name);
            methodView.setChecked(malMethod.checked);
        }

        return convertView;
    }
}
