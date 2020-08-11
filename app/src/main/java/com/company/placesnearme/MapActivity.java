package com.company.placesnearme;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.company.entities.Geometry;
import com.company.entities.Result;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends BaseActivity  {
    private Location location;
    LocationManager mLocation_manager;
    LocationListener mLocationListener;

    TextView distance;
    double lat, lng;

    @Override
    protected int getLayoutId() {
        return R.layout.map;
    }

    @Override
    protected void startDemo(boolean isRestore) {


        distance = (TextView) findViewById(R.id.distance);

        Result result;
        result = (Result) getIntent().getSerializableExtra("result");
        Geometry g = result.getGeometry();
        com.company.entities.Location l = g.getLocation();
        lat = l.getLat();
        lng = l.getLng();
        LatLng loc = new LatLng(lat, lng);

        String name = result.getName();
        //  float distance= location.distanceBetween(lat,lng,0.0,0.0);


        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
        Marker TP = getMap().addMarker(new MarkerOptions()
                .position(loc).title(name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
       /* mLocation_manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocation_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000*3, 50, this);*/



    }

/*
Implement location listener
    @Override
    public void onLocationChanged(Location location) {
        double curlat= location.getLatitude();
        double curlng =location.getLongitude();
        LatLng curloc=new LatLng(curlat,curlng);
        Marker cur = getMap().addMarker(new MarkerOptions()
                .position(curloc).title("My location"));
        float[] r = new float[1];
        Location.distanceBetween(lat, lng,
                curlat, curlng, r);
        System.out.println(r[0]);
        distance.setText(String.valueOf(r[0]));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }*/
}
