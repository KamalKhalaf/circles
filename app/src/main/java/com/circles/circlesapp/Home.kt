package com.circles.circlesapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.circles.circlesapp.addgroup.AddGroupFragment
import com.circles.circlesapp.chatlist.ChatsFragment
import com.circles.circlesapp.fcm.constants.Config
import com.circles.circlesapp.helpers.SharedPrefHelper
import com.circles.circlesapp.helpers.kotlin.ActivityUtilsKotlin
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor
import com.circles.circlesapp.home.AddTextCommentFragment
import com.circles.circlesapp.home.HomeFragment
import com.circles.circlesapp.messaging.view.RecordAudioDialogFragment
import com.circles.circlesapp.nearby.NearbyFragment
import com.circles.circlesapp.notifications.NotificationsActivity
import com.circles.circlesapp.profile.ProfileFragment
import com.circles.circlesapp.search.RxBus
import com.circles.circlesapp.search.SearchFragment
import com.circles.circlesapp.settings.ui.SettingsActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.bottom_navigation_bar_layout.*

class Home : AppCompatActivity() {

    //    internal var receiver: Receiver? = Receiver()
//    var isReciverRegistered = false

//    val isConnect : isNetworkConnected = null

    val accesToken = ""
    val tokenType = ""
    lateinit var selectedFragment: Fragment
    var prefHelper : SharedPrefHelper? = null
    lateinit var imageView: CircleImageView
//    public var bottomNavigationView: BottomNavigationView? = null

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_navigation_bar_layout)
        if (intent != null) {
            val b = intent.getBooleanExtra(Config.PUSH_NOTIFICATION, false)
            Log.d("hfidjs", b.toString())
            if (b) SettingsActivity.start(this)
        }

        //toolbar
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar?
        setSupportActionBar(toolbar)
        val searchView: SearchView = findViewById(R.id.search_view)
        val searchAtuo: SearchView.SearchAutoComplete = searchView.findViewById(R.id.search_src_text)

        prefHelper = SharedPrefHelper(this@Home)


        searchAtuo.setTextColor(R.color.black)
        searchAtuo.setHintTextColor(R.color.black)
        toolbar?.findViewById<ImageView>(R.id.notifications)?.setOnClickListener { NotificationsActivity.start(this) }
        searchAtuo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                val currentFragment = supportFragmentManager.findFragmentByTag("SearchFragment")
                if (currentFragment != null && newText.isEmpty()) {
                    onBackPressed()
                    navVisabilty(true)
                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                Log.d("onQueryTextChange", query)
//                ActivityUtilsKotlin.replaceFragmentToActivity(
//                        supportFragmentManager, SearchFragment.getInstance(query), R.id.content)
                val currentFragment = supportFragmentManager.findFragmentByTag("SearchFragment")
                navVisabilty(false)
                if (currentFragment == null) {
                    addFragment(SearchFragment.getInstance(query), true, "SearchFragment")
                } else {
                    RxBus.get().sendSearchQuery(query)
                }
                return false
            }

        })


        // Home is the Default selection for the bottom nav
        val home = HomeFragment.newInstance(tokenType, accesToken)

        ActivityUtilsKotlin.replaceFragmentToActivity(
                supportFragmentManager, home, R.id.content)
        //end

        //bottom Nav on select item change the content
        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation)
        imageView = findViewById<CircleImageView>(R.id.logo)
        val progressBarload = toolbar?.findViewById<ProgressBar>(R.id.progressBarload)
        if (prefHelper!!.profileImage != null) {
            Glide.with(this).asBitmap()
                    .load(prefHelper!!.profileImage)
                    .into(imageView)
        } else {
            Glide.with(this).load(R.drawable.sign_up_profile).into(imageView)
        }


        bottomNavigationView!!.setOnNavigationItemSelectedListener { item ->

            //            imageView.setImageDrawable(content.resources.getDrawable(R.drawable.logo))
            when (item.itemId) {
                R.id.home -> {
                    val home = HomeFragment.newInstance(tokenType, accesToken)
                    selectedFragment = home
                    imageView.isEnabled = false

                }
                R.id.nearby -> {
                    selectedFragment = NearbyFragment.newInstance(tokenType, accesToken)
                    imageView.isEnabled = false

                }
                R.id.profile -> {
                    gotoProfile()
                }
                R.id.chats -> {

                    selectedFragment = ChatsFragment.newInstance()
                    imageView.isEnabled = false
                    val fragmentManager = supportFragmentManager
                    /* val recordAudioFragment = RecordAudioDialogFragment.newInstance()
                     recordAudioFragment.show(fragmentManager, "Dialog")*/
                }
            }
            ActivityUtilsKotlin.replaceFragmentToActivity(
                    supportFragmentManager, selectedFragment, R.id.content)
            true
        }
        //end bottom nav

        //search bar
//        search_bar_edit_text.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
////                performSearch()
//                return@OnEditorActionListener true
//            }
//            false
//        })
        //end search bar

    }

    public fun gotoProfile() {
//        bottomNavigationView!!.setSelectedItemId(R.id.profile)
        imageView.setImageDrawable(content.resources.getDrawable(R.drawable.qr_code))
        selectedFragment = ProfileFragment.newInstance(tokenType, accesToken)
        imageView.isEnabled = true
        ActivityUtilsKotlin.replaceFragmentToActivity(
                supportFragmentManager, selectedFragment, R.id.content)
    }
    public fun gotoNearByMap() {
//        bottomNavigationView!!.setSelectedItemId(R.id.nearby)
        selectedFragment = NearbyFragment.newInstance2(tokenType, accesToken)
        imageView.isEnabled = false

        ActivityUtilsKotlin.replaceFragmentToActivity(
                supportFragmentManager, selectedFragment, R.id.content)
    }

    //replace fragments inside the framelayout
    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }

    fun addFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()

        if (addToBackStack) {
            ft.addToBackStack(null)
        }
        ft.replace(R.id.content, fragment, tag).commit()
    }

    override fun onBackPressed() {
        navVisabilty(true)
        super.onBackPressed()

    }

    @SuppressLint("RestrictedApi")
    fun navVisabilty(isVisable: Boolean) {
        val actionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        val activityName = findViewById<TextView>(R.id.actionName)
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation)
        if (isVisable) {
            activityName.visibility = View.VISIBLE
            actionButton.visibility = View.VISIBLE
            navigationView.visibility = View.VISIBLE
        } else {
            activityName.visibility = View.GONE
            actionButton.visibility = View.GONE
            navigationView.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("onActivityResult", "b")

        val fragments = supportFragmentManager.fragments;
        for (f in fragments) {
            if (f is AddGroupFragment)
                f.onActivityResult(requestCode, resultCode, data)
            else if (f is RecordAudioDialogFragment)
                f.onActivityResult(requestCode, resultCode, data)
            else if (f is AddTextCommentFragment)
                f.onActivityResult(requestCode, resultCode, data)
            else if (f is NearbyFragment)
                f.onActivityResult(requestCode, resultCode, data)
        }

    }

    override fun onResume() {
        super.onResume()
//        if (receiver == null) {
//
//            receiver = Receiver()
//
//            val filter = IntentFilter(BroadcastHelper.ACTION_NAME)
//
//            registerReceiver(receiver, filter)
//
//            isReciverRegistered = true
//
//        }



        if (MyServiceInterceptor.authentication == "" && prefHelper!!.userToken != null && !prefHelper!!.userToken.equals("")) {
            MyServiceInterceptor.authentication = prefHelper!!.userToken
        }
    }
//
//    class Receiver : BroadcastReceiver() {
//        override fun onReceive(arg0: Context?, arg1: Intent?) {
//
//            Log.v("r", "receive " + arg1!!.getStringExtra(BroadcastHelper.BROADCAST_EXTRA_METHOD_NAME))
//
//            val methodName = arg1.getStringExtra(BroadcastHelper.BROADCAST_EXTRA_METHOD_NAME)
//
//            if (methodName != null && methodName!!.length > 0) {
//
//                Log.v("receive", methodName)
//
//                if(methodName.equals("get_order_details")) {
//
//                    val user_id = arg1.getStringExtra(BroadcastHelper.BROADCAST_EXTRA_DATA)
//                    Toast.makeText(getApplicationContext(), user_id, Toast.LENGTH_SHORT).show()
//                }
//
//            }
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//
//        if (isReciverRegistered) {
//
//            if (receiver != null)
//
//                unregisterReceiver(receiver)
//
//        }
//    }


//    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val notConnected = intent.getBooleanExtra(ConnectivityManager
//                    .EXTRA_NO_CONNECTIVITY, false)
//            if (notConnected) {
//
//                isConnect = (isNetworkConnected) getapplication();
//                fragmentCommunicator = application as FragmentCommunicator
//            } else {
//                connected()
//            }
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//
//        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
//    }
//
//    override fun onStop() {
//        super.onStop()
//
//        unregisterReceiver(broadcastReceiver)
//    }


}
