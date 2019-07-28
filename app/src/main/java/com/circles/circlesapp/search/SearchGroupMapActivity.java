package com.circles.circlesapp.search;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.circles.circlesapp.R;
import com.circles.circlesapp.addgroup.GroupPasscodeDialog;
import com.circles.circlesapp.group.GroupActivity;
import com.circles.circlesapp.helpers.kotlin.MapZoomHelper;
import com.circles.circlesapp.nearby.JoinGroupReqBody;
import com.circles.circlesapp.nearby.JoinGroupResponse;
import com.circles.circlesapp.nearby.NearbyRepo;

public class SearchGroupMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public static void start(FragmentActivity activity, SearchResult item) {
        Intent i = new Intent(activity, SearchGroupMapActivity.class);
        i.putExtra("item", item);

        activity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_group_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public static void start(Context x, LatLng location, int groupId, String groupName) {
        Intent i = new Intent(x, SearchGroupMapActivity.class);
        i.putExtra("location", location);
        i.putExtra("groupId", groupId);
        i.putExtra("groupName", groupName);
        x.startActivity(i);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (getIntent() != null) {
            Intent intent = getIntent();
            SearchResult item = intent.getParcelableExtra("item");
            LatLng location = item.getLocation();
            int groupId = item.getId();
            String groupName = item.getName();
            String type = item.getType();
            if (type.equals("public")) {
                mMap.addMarker(new MarkerOptions().
                        position(location).title(groupName).
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.p_group_icon_big)));
            } else if (type.equals("event")) {
                mMap.addMarker(new MarkerOptions().
                        position(location).title(groupName).
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.event_icon_big)));
            } else if (type.equals("private")) {
                mMap.addMarker(new MarkerOptions().
                        position(location).title(groupName).
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.small_private_group)));
            }
            mMap.setOnMarkerClickListener(marker -> {
                onGroupClicked(item);
                return false;
            });
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            mMap.setMinZoomPreference(new MapZoomHelper().calculateZoomLevel(Resources.getSystem().getDisplayMetrics().widthPixels));
        }
    }

    private void onGroupClicked(SearchResult item) {
        String type = item.getType();
        JoinGroupReqBody body = new JoinGroupReqBody();
        body.groupName = item.getName();
        body.id = item.getId() + "";
        body.latitude = Double.parseDouble(item.getLatitude());
        body.longitude = Double.parseDouble(item.getLongitude());
        body.password = Integer.parseInt(item.getPassword());
        if (type.equals("public")) {
            joinGroupApi(body);
        } else if (type.equals("event") || type.equals("private")) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.add(android.R.id.content, GroupPasscodeDialog.newInstance(body), "rageComicList").commit();
        }

    }

    private void joinGroupApi(JoinGroupReqBody body) {
        new NearbyRepo().RequestJoinGroup(body).observe(this, resp -> {
            if (resp != null) {
                switch (resp.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (resp.getData() != null) {
                            JoinGroupResponse groupResponse = resp.getData().data;
                            groupResponse.latitude = body.latitude;
                            groupResponse.longitude = body.longitude;
                            groupResponse.groupName = body.groupName;
                            if (groupResponse.channel != null) {
                                Intent intent = new Intent(SearchGroupMapActivity.this, GroupActivity.class);
                                intent.putExtra("data", groupResponse);
                                startActivity(intent);
                            }
                        }
                        break;
                    case ERROR:
                        Toast.makeText(this, "you can not join this group", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
