package com.example.dell.namaz;

/**
 * Created by Dell on 2/28/2017.
 */
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

public class Latitude extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.namaz_latitude, container, false);
        return rootView;
    }
}
