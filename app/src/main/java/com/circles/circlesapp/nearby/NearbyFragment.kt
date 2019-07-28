package com.circles.circlesapp.nearby

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.circles.circlesapp.R
import com.circles.circlesapp.addgroup.AddGroupFragment
import com.circles.circlesapp.addgroup.GroupPasscodeDialog
import com.circles.circlesapp.addgroup.GroupTypes
import com.circles.circlesapp.addgroup.ThereIsAlreadyGroupDialog
import com.circles.circlesapp.databinding.FragmentNearbyBinding
import com.circles.circlesapp.group.GroupActivity
import com.circles.circlesapp.helpers.AppConstants
import com.circles.circlesapp.helpers.GpsUtils
import com.circles.circlesapp.helpers.SharedPrefHelper
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import de.hdodenhof.circleimageview.CircleImageView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


private const val PERMISSION_REQUEST = 10

class NearbyFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GpsUtils.onGpsListener {

    override fun gpsStatus(isGPSEnable: Boolean) {

        isGPS = isGPSEnable
        if (isGPS) {
            enableView()
        } else {
            Toast.makeText(activity, "Please turn on GPS", Toast.LENGTH_SHORT).show()
        }

    }

    private var isFromProfile: Boolean = false
    lateinit var locationManager: LocationManager
    override fun onMarkerClick(m: Marker?): Boolean {
        if (m == null) return true
        for (g in groupsList) {
            if (g.name == m.title) {
                onGroupClicked(g)
            }
        }
        return true
    }

    private fun onGroupClicked(g: GroupObjectRespModel) {
        if (g.type == "public") {
            val reqBody = JoinGroupReqBody()
            reqBody.id = g.id
            reqBody.latitude = g.latitude
            reqBody.longitude = g.longitude
            reqBody.groupName = g.name
            viewModel.requestJoinGroup(reqBody)
        } else if (g.type == "private" || g.type == "event") {
            val reqBody = JoinGroupReqBody()
            reqBody.id = g.id
            reqBody.latitude = g.latitude
            reqBody.longitude = g.longitude
            reqBody.groupName = g.name
            reqBody.password = g.password
            showFragment(GroupPasscodeDialog.newInstance(reqBody))
        }
    }

    lateinit var viewModel: NearbyViewModel
    lateinit var myLocation: LatLng
    var b: FragmentNearbyBinding? = null
    private var groupsList: MutableList<GroupObjectRespModel> = ArrayList()

    private var mMap: GoogleMap? = null
    private var isContinue = false
    private var isGPS = false
    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0
        with(mMap!!.uiSettings) {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMyLocationButtonEnabled = true
            isIndoorLevelPickerEnabled = true
            isMapToolbarEnabled = true
            isZoomGesturesEnabled = true
            isScrollGesturesEnabled = true
            isTiltGesturesEnabled = true
            isRotateGesturesEnabled = true
        }
        Log.d("test", "on map ready")
//        mMap!!.isMyLocationEnabled = true
        mMap!!.setOnMarkerClickListener(this)

    }

    companion object {
        fun newInstance(tokenType: String, accsToken: String): NearbyFragment {
            val args = Bundle()
            val fragment = NearbyFragment()
            val fromProfile = false
            args.putBoolean("is_fromProfile", fromProfile)
            fragment.setArguments(args)
            return fragment
        }

        fun newInstance2(tokenType: String, accsToken: String): NearbyFragment {
            val args = Bundle()
            val fragment = NearbyFragment()
            val fromProfile = true
            args.putBoolean("is_fromProfile", fromProfile)
            fragment.setArguments(args)
            return fragment
        }
    }

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null


    @SuppressLint("RestrictedApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        b = DataBindingUtil.inflate<FragmentNearbyBinding>(inflater, R.layout.fragment_nearby, container, false)
        viewModel = ViewModelProviders.of(this).get(NearbyViewModel::class.java)


        val mapFragment: SupportMapFragment? = childFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

        locationRequest = LocationRequest.create()
        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest!!.setInterval((10 * 1000).toLong()) // 10 seconds
        locationRequest!!.setFastestInterval((5 * 1000).toLong()) // 5 seconds

        GpsUtils(activity).turnGPSOn(object : GpsUtils.onGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) {
                isGPS = isGPSEnable
            }
        })

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {

                        myLocation = LatLng(location.latitude, location.longitude)
                        viewModel.getNearby(myLocation)
                        mMap!!.addMarker(MarkerOptions().position(myLocation).title("my location"))
                        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
                        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12F));


                        if (!isContinue && mFusedLocationClient != null) {
                            mFusedLocationClient!!.removeLocationUpdates(locationCallback)
                        }
                    }
                }
            }
        }


        if (isGPS) {
            enableView()
        }
        if (arguments != null) {
            isFromProfile = arguments!!.getBoolean("is_fromProfile")
        }

        if (isFromProfile && isGPS) {
            Handler().postDelayed({
                showAddGroupFragment(GroupTypes.PUBLIC_GROUP)
            }, 2500)

        }
        activity!!.findViewById<FloatingActionButton>(R.id.floatingActionButton).isEnabled = true
        val toolbar = activity!!.findViewById<View>(R.id.toolbar) as Toolbar?
        lateinit var profile_img: CircleImageView
        profile_img = toolbar?.findViewById<CircleImageView>(R.id.logo)!!


        val prefHelper = SharedPrefHelper(activity)


        val userImage = toolbar?.findViewById<CircleImageView>(R.id.logo)
        val progressBarload = toolbar?.findViewById<ProgressBar>(R.id.progressBarload)
        if (prefHelper.profileImage != null) {
            Glide.with(this).asBitmap()
                    .load(prefHelper.profileImage)
                    .into(userImage)
        } else {
            Glide.with(this).load(R.drawable.sign_up_profile).into(userImage)
        }


        val fab: FloatingActionButton = activity!!.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        activity!!.findViewById<TextView>(R.id.actionName).visibility = View.VISIBLE
        activity!!.findViewById<TextView>(R.id.actionName).setText(getString(R.string.add_new))
        fab.setImageResource(R.drawable.ic_add)
        fab.visibility = View.VISIBLE
        fab.setOnClickListener {

        }

        fab.setOnClickListener(View.OnClickListener {
            Log.d("test", "adddddd")
            if (b!!.shape.visibility != View.VISIBLE) {
                b!!.shape.visibility = View.VISIBLE
                fab.setImageDrawable(resources.getDrawable(R.drawable.ic_x))
            } else {
                b!!.shape.visibility = View.GONE
                fab.setImageDrawable(resources.getDrawable(R.drawable.ic_add))
            }
        })

        observeOnNearbyResponse()
        b!!.publicGroup.setOnClickListener { v ->
            if (thereIsPublicGroup()) {
                showFragment(ThereIsAlreadyGroupDialog.newInstance(getThePublicGroup()))
            } else
                showAddGroupFragment(GroupTypes.PUBLIC_GROUP)
        }
        b!!.privateGroup.setOnClickListener { v ->
            showAddGroupFragment(GroupTypes.PRIVATE_GROUP)
        }
        b!!.event.setOnClickListener { v ->
            showAddGroupFragment(GroupTypes.EVENT)
        }
        viewModel.joinGroupMutableLD.observe(this, Observer {
            if (it != null) {
                if (it.channel != null) {
                    val intent = Intent(activity, GroupActivity::class.java)
                    intent.putExtra("data", it)
                    startActivity(intent)
                }
            }
        })
        return b!!.root
    }

    private fun showFragment(fragment: Fragment) {
        activity!!.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment, "rageComicList")
                .addToBackStack(null)
                .commit()
    }


    private fun enableView() {
        getLocation()
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    AppConstants.LOCATION_REQUEST)

        } else {
            Log.d("test", "getLocation")
//            mMap!!.isMyLocationEnabled = true
            if (isContinue) {
                mFusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, null)
            } else {
                mFusedLocationClient!!.getLastLocation().addOnSuccessListener(activity!!) { location ->
                    if (location != null) {
                        myLocation = LatLng(location.latitude, location.longitude)
                        viewModel.getNearby(myLocation)
                        mMap!!.addMarker(MarkerOptions().position(myLocation).title("my location"))
                        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
                        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12F));
                    } else {
                        mFusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, null)
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    mMap!!.isMyLocationEnabled = true
                    isGPS = true
                    getLocation()

                } else {
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("onActivityResult", "a")

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.GPS_REQUEST) {
                if (resultCode.equals(Activity.RESULT_OK)) {
                    isGPS = true // flag maintain before get location
                    Log.d("onActivityResult", "b")
//                    mMap!!.isMyLocationEnabled = true
                    if (isGPS && isFromProfile) {
                        Handler().postDelayed({
                            showAddGroupFragment(GroupTypes.PUBLIC_GROUP)
                        }, 2500)
                    } else if (isGPS) {
                        getLocation()
                    }

//                System.out.println("test user has turned the gps back on");
                } else if (resultCode.equals(Activity.RESULT_CANCELED)) {

//                System.out.println("test user has denied the gps to be turned on");
                    Toast.makeText(getActivity(), "Location is required to order stations", Toast.LENGTH_SHORT).show();

                }


            }
        }
    }

    private fun thereIsPublicGroup(): Boolean {
        if (groupsList.size == 0) return false
        for (g in groupsList) {
            if (g.type == "public") return true
        }
        return false
    }

    private fun getThePublicGroup(): GroupObjectRespModel? {
        if (groupsList.size == 0) return null
        for (g in groupsList) {
            if (g.type == "public") return g
        }
        return null
    }


    private fun thereIsPrivateGroup(): Boolean {
        if (groupsList.size == 0) return false
        for (g in groupsList) {
            if (g.type == "private") return true
        }
        return false
    }

    private fun thereIsEventGroup(): Boolean {
        if (groupsList.size == 0) return false
        for (g in groupsList) {
            if (g.type == "event") return true
        }
        return false
    }

    private fun showAddGroupFragment(type: GroupTypes) {
        try {
            if (isFromProfile) {
                showFragment(AddGroupFragment.newInstance(myLocation, type, isFromProfile))
            } else {
                showFragment(AddGroupFragment.newInstance(myLocation, type, isFromProfile))
            }

        } catch (e: Exception) {
        }

    }


    private fun observeOnNearbyResponse() {
        viewModel.nearbyOnMapMutableLD.observe(this, Observer {
            if (it != null) {
                groupsList = it;
                addToMap(groupsList)
            } else {
                groupsList = arrayListOf()
            }
        })
    }

    private fun addToMap(it: MutableList<GroupObjectRespModel>) {
        for (g in it) {
            if (g.type == "public") {
                mMap!!.addMarker(MarkerOptions().position(LatLng(g.latitude, g.longitude))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.p_group_icon_big)).title(g.name));
            } else if (g.type == "private") {
                mMap!!.addMarker(MarkerOptions().position(LatLng(g.latitude, g.longitude))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.small_private_group)).title(g.name));
            } else if (g.type == "event") {
                mMap!!.addMarker(MarkerOptions().position(LatLng(g.latitude, g.longitude))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.event_icon_big)).title(g.name));
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMessageEvent(it: JoinGroupResponse) {
        if (it.channel != null) {
            val intent = Intent(activity, GroupActivity::class.java)
            intent.putExtra("data", it)
            startActivity(intent)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
        Log.d("onAttach", "");


    }

    override fun onDetach() {
        super.onDetach()
        Log.d("onDetach", "");
        EventBus.getDefault().unregister(this)

    }

}
