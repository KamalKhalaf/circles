package com.circles.circlesapp.addgroup;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;

import com.circles.circlesapp.R;
import com.circles.circlesapp.helpers.kotlin.MapZoomHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickUpLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    GoogleMap map;
    private LatLng selectedLatLng;

    public PickUpLocationActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pick_up_location);
        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            } else
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if (location != null) {
                            selectedLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).draggable(true));
                            map.moveCamera(CameraUpdateFactory.newLatLng(selectedLatLng));
                            map.setMinZoomPreference(new MapZoomHelper().calculateZoomLevel(Resources.getSystem().getDisplayMetrics().widthPixels));
                            map.getUiSettings().setScrollGesturesEnabled(false);
                        }
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                });
        }
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        supportMapFragment.getMapAsync(this);

        Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(v -> {
            if (selectedLatLng == null) {
                Toast.makeText(this, R.string.please_select_loc, Toast.LENGTH_SHORT).show();
                return;
            }
            MessageEvent messageEvent = new MessageEvent();
            messageEvent.setLatLng(selectedLatLng);
            EventBus.getDefault().postSticky(messageEvent);
            onBackPressed();
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerDragListener(this);
    }


    @Override
    public void onMarkerDragStart(Marker marker) {
        selectedLatLng = marker.getPosition();
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        selectedLatLng = marker.getPosition();
    }
}
