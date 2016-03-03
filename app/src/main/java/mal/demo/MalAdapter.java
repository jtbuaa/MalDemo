package mal.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import java.util.List;

/**
 * Created by jingtao on 2/28/16.
 */
public class MalAdapter extends ArrayAdapter<MalMethod> {
    private int mResourceId;
    private LayoutInflater mInflater;

    public MalAdapter(Context context, int resId, List<MalMethod> methods) {
        super(context, resId, methods);
        mResourceId = resId;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MalMethod malMethod = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(mResourceId, null);
        }
        CheckedTextView methodView = (CheckedTextView) convertView.findViewById(R.id.list_row_ctv);
        methodView.setText(malMethod.name);
        methodView.setChecked(malMethod.checked);

        return convertView;
    }
}
