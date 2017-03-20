package com.example.dell.namaz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Dell on 2/28/2017.
 */

public class CustomAdapter extends ArrayAdapter<String> {
    String[] name = {"Fajar","Zuhar","Asar","Mghrb","Isha"};
    public CustomAdapter(Context context , String[] time) {
        super(context , R.layout.custom_layout , time );
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater buckyinflater = LayoutInflater.from(getContext());
        View customView = buckyinflater.inflate(R.layout.custom_layout , parent , false);

        String the_time = getItem(position);
        String the_name = name[position];

        TextView namazName = (TextView) customView.findViewById(R.id.namaz_name);
        TextView namazTime = (TextView) customView.findViewById(R.id.namaz_time);
        ImageView namazAlarm = (ImageView) customView.findViewById(R.id.namaz_alarm);


        namazName.setText(the_name);
        namazTime.setText(the_time);
        namazAlarm.setImageResource(R.drawable.clock);

        return customView;
    }
}
