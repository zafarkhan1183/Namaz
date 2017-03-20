package com.example.dell.namaz;

/**
 * Created by Dell on 2/28/2017.
 */

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;


public class Location extends Fragment implements LocationListener {


    TextView txtJson;
    ListView buckyListView;
    View rootView;
    LocationManager locationManager;
    String provider;
    String city="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.namaz_location, container, false);

        //Getting Location
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.NETWORK_PROVIDER;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            android.location.Location location = locationManager.getLastKnownLocation(provider);
            if(location != null){
                    Log.i("Location","Achieved");
                    onLocationChanged(location);
            }else{
                txtJson.setText("No City Found");
            }
        }

        txtJson = (TextView) rootView.findViewById(R.id.txtJson);

        return rootView;
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        Double lat = location.getLatitude();
        Double lon = location.getLongitude();

        Geocoder geocoder = new Geocoder(getContext() , Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(lat,lon,1);
            if(listAddresses != null && listAddresses.size()>0){
                city = listAddresses.get(0).getLocality();
                Toast.makeText(getContext(),city,Toast.LENGTH_LONG).show();
                Log.i("City" , city);
                if(city!=""){

                    //Getting Time of Namaz from Api
                    GetTime gettime = new GetTime();
                    gettime.execute("http://muslimsalat.com/"+city+"/weekly.json?key=ed3d60643f756b0d26c2be4000ad0a84");

                }
            }else{
                Log.i("City" , "Nothing Found");
                txtJson.setText("No City Found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Latitude",lat.toString());
        Log.i("Longitude",lon.toString());
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

    public class GetTime extends AsyncTask<String, Void, String> {
        ProgressDialog pd = new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Loading...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            URL url =null;
            HttpURLConnection urlConnection = null;
            String result = "";

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while(data != -1){
                    char current = (char) data;
                    result += current;
                    data=reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result != null){
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("items"));
                    for(int i=0;i<jsonArray.length();i++){
                        if(i==0){
                            pd.dismiss();
                            JSONObject jsonPart = jsonArray.getJSONObject(i);
                            String[] time = {jsonPart.getString("fajr") , jsonPart.getString("dhuhr") , jsonPart.getString("asr") , jsonPart.getString("maghrib") , jsonPart.getString("isha")};
                            ListAdapter buckyadapter = new CustomAdapter(getContext() , time);
                            buckyListView = (ListView) rootView.findViewById(R.id.buckyListView);
                            buckyListView.setAdapter(buckyadapter);


                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else{

                pd.dismiss();
                txtJson.setText("No Internet Connectivity");
            }
        }
    }

}

