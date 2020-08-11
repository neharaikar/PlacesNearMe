package com.company.placesnearme;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.company.adapters.PlacesListAdapter;
import com.company.api.APIClient;
import com.company.api.GoogleMapAPI;
import com.company.entities.Geometry;
import com.company.entities.OpeningHours;
import com.company.entities.PlacesResults;
import com.company.entities.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity
{

    private EditText editTextKeyword;
    private Button buttonSearch;
    int count = 0;
    int radius = 100;
    private Spinner spinnerType;
    private ListView listViewPlaces;
    int half_minute = 1000000 * 30;
    int one_minute = 1000000 * 60;
    private LocationManager locationManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        loadData();
    }

    private void initView() {
        editTextKeyword = findViewById(R.id.editTextKeyword);
        buttonSearch = findViewById(R.id.buttonSearch);
        listViewPlaces = findViewById(R.id.listViewPlaces);
        spinnerType = findViewById(R.id.spinnerType);
        buttonSearch.setOnClickListener(this::buttonSearch_onClick);

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void buttonSearch_onClick(View view) {
        count = 0;

        System.out.println("Button has been clicked");
        boolean hasForegroundLocationPermission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean hasForegroundLocationPermissionfine = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (hasForegroundLocationPermission||hasForegroundLocationPermissionfine) {
            boolean hasBackgroundLocationPermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
            if (hasBackgroundLocationPermission) {
                if (isLocationEnabled()) {
                    Toast.makeText(this, "Searching for results", Toast.LENGTH_LONG).show();
                    listViewPlaces.setAdapter(null);
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, one_minute, 10, locationListener);
                } else {
                    Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "This application will not work in background unless you allow it all the time", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 12);
            }
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 12);

        }

    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {


            String keyword = editTextKeyword.getText().toString();
            String key = getText(R.string.google_maps_key).toString();

            String currentLocation = location.getLatitude() + "," + location.getLongitude();
            String rad = spinnerType.getSelectedItem().toString();
            if (rad.equals("100m"))
                radius = 100;
            else if (rad.equals("500m"))
                radius = 500;
            else if (rad.equals("1km"))
                radius = 1000;
            else if (rad.equals("5km"))
                radius = 5000;
            count = 0;
            String type = "All";
            GoogleMapAPI googleMapAPI = APIClient.getClient().create(GoogleMapAPI.class);
            googleMapAPI.getNearBy(currentLocation, radius, type, keyword, key).enqueue(new Callback<PlacesResults>() {

                @Override
                public void onResponse(Call<PlacesResults> call, Response<PlacesResults> response) {
                    if (response.isSuccessful()) {
                        List<Result> results = response.body().getResults();

                        int len = results.size();
                        if (!(results.isEmpty())) {
                            PlacesListAdapter placesListAdapter = new PlacesListAdapter(getApplicationContext(), results);

                            listViewPlaces.setAdapter(placesListAdapter);

                            System.out.println("000000000000000000000000000000000000000000000000000");
                            count++;
                            for (int i = 0; i < len; i++) {

                                Result result = results.get(i);
                                System.out.println(count);
                                count++;
                                System.out.println("Name:" + result.getName());
                                System.out.println("Address:" + result.getVicinity());
                                Geometry g = result.getGeometry();
                                com.company.entities.Location l = g.getLocation();
                                System.out.println("Geometry:" + l.getLat() + "," + l.getLng());
                                Double rating = result.getRating();
                                Integer user_rating = result.getUser_rating();
                                if (rating != 0.0) {
                                }
                                OpeningHours op = result.getOpeningHours();

                                if (op != null && op.getOpenNow() != null) {

                                    if (op.getOpenNow()) {
                                        System.out.println("Availability: Open");
                                    } else {
                                        System.out.println("Availability: Closed");
                                    }
                                }
                            }
                            System.out.println("0000000000000000000000000000000000000000000000000000");

                        }
                        if (results.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Sorry, No results found", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<PlacesResults> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            Toast.makeText(getApplicationContext(), "Gps Enables", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }
    };

    private void loadData() {

        List<String> types = new ArrayList<>();
        types.add("100m");
        types.add("500m");
        types.add("1km");
        types.add("5km");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, types);
        spinnerType.setAdapter(arrayAdapter);

    }




}