package com.example.mygeodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jatin Gupte, Dheeraj Mirashi
 * @Group No: 50
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);


    private Marker mPerth;
    private Marker mSydney;
    private Marker mBrisbane;

    List<LatLng> coordintesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.coordintesList = getCoordinatesList(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addPolyline(new PolylineOptions().clickable(false).addAll(this.coordintesList));
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (LatLng latLng : coordintesList) {
            builder.include(latLng);
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 800,800,10));
        mMap.addMarker(new MarkerOptions().position(coordintesList.get(0)).title("Start"));
        mMap.addMarker(new MarkerOptions().position(coordintesList.get(coordintesList.size()-1)).title("End"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 1, 1, 1));
    }


    List<LatLng> getCoordinatesList(Context context) {
        ArrayList<LatLng> coordinates = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(loadJSONFromAsset(context));
            JSONArray data = root.getJSONArray("points");

            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonObjectson = data.getJSONObject(i);
                coordinates.add(new LatLng(jsonObjectson.getDouble("latitude"), jsonObjectson.getDouble("longitude")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coordinates;
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.trip);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

}
