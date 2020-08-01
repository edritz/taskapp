package com.example.helloviewers;

import java.util.List;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ListAdapter extends ArrayAdapter<RunningAppProcessInfo> {
    // List context
    private final Context context;
    // List values
    private final List<RunningAppProcessInfo> values;

    public ListAdapter(Context context, List<RunningAppProcessInfo> values) {
        super(context, R.layout.activity_main, values);
        this.context = context;
        this.values = values;
    }


    /**
     * Constructing list element view
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.c_layout, parent, false);

        TextView appName = (TextView) rowView.findViewById(R.id.appNameText);
        appName.setText(String.format("%s\nPID: %s", values.get(position).processName, values.get(position).pid));

        Button deleteBtn = (Button)rowView.findViewById(R.id.delete_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Toast.makeText(context, String.format("Process %s\n(PID: %s)\nKilled", values.get(position).processName, values.get(position).pid), Toast.LENGTH_LONG).show();
                android.os.Process.killProcess(values.get(position).pid);
                values.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });

        return rowView;
    }


}
