package com.circles.circlesapp.phase2.views.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.circles.circlesapp.R
import com.circles.circlesapp.addgroup.AddGroupFragment
import com.circles.circlesapp.fcm.constants.Config
import com.circles.circlesapp.helpers.SharedPrefHelper
import com.circles.circlesapp.helpers.kotlin.ActivityUtilsKotlin
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor
import com.circles.circlesapp.home.AddTextCommentFragment
import com.circles.circlesapp.messaging.view.RecordAudioDialogFragment
import com.circles.circlesapp.nearby.NearbyFragment
import com.circles.circlesapp.notifications.NotificationsActivity
import com.circles.circlesapp.settings.ui.SettingsActivity
import de.hdodenhof.circleimageview.CircleImageView

/*
*
* Created by Kamal Khalaf on 8/30/2019.
* Contact : kamal.khalaf56@gmail.com
*
*/

class Home2 : AppCompatActivity() {

    val accesToken = ""
    val tokenType = ""
    lateinit var selectedFragment: Fragment
    var prefHelper: SharedPrefHelper? = null
    lateinit var imageView: CircleImageView

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_navigation_bar_layout_phase2)
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

        prefHelper = SharedPrefHelper(this@Home2)


        searchAtuo.setTextColor(R.color.black)
        searchAtuo.setHintTextColor(R.color.black)
        toolbar?.findViewById<ImageView>(R.id.notifications)?.setOnClickListener { NotificationsActivity.start(this) }
        searchAtuo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
//                val currentFragment = supportFragmentManager.findFragmentByTag("SearchFragment")
//                if (currentFragment != null && newText.isEmpty()) {
//                    onBackPressed()
//                    navVisabilty(true)
//                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
//                Log.d("onQueryTextChange", query)
//                val currentFragment = supportFragmentManager.findFragmentByTag("SearchFragment")
//                navVisabilty(false)
//                if (currentFragment == null) {
//                    addFragment(SearchFragment.getInstance(query), true, "SearchFragment")
//                } else {
//                    RxBus.get().sendSearchQuery(query)
//                }
                return false
            }

        })


        // Home is the Default selection for the bottom nav
        val home = NearbyFragment.newInstance(tokenType, accesToken)

        ActivityUtilsKotlin.replaceFragmentToActivity(
                supportFragmentManager, home, R.id.content)
        //end

        //bottom Nav on select item change the content
        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation)


        bottomNavigationView!!.setOnNavigationItemSelectedListener { item ->

            //            imageView.setImageDrawable(content.resources.getDrawable(R.drawable.logo))
            when (item.itemId) {
                R.id.home -> {
                    val home = NearbyFragmentPhase2.newInstance(tokenType, accesToken)
                    selectedFragment = home

                }
                R.id.groups -> {
                    selectedFragment = ListOfGroupsFragment.newInstants(tokenType, accesToken)

                }
                R.id.profile -> {
                    selectedFragment = ProfilePhase2Fragment.newInstants(tokenType, accesToken)

                }
                R.id.message -> {

                    selectedFragment = ChatsFragmentPhase2.newInstance()
                    val fragmentManager = supportFragmentManager

                }

                R.id.friends -> {

                    val fragmentManager = supportFragmentManager

                }
            }
            ActivityUtilsKotlin.replaceFragmentToActivity(
                    supportFragmentManager, selectedFragment, R.id.content)
            true
        }
        //end bottom nav
    }

    override fun onBackPressed() {
        navVisabilty(true)
        super.onBackPressed()

    }

    @SuppressLint("RestrictedApi")
    fun navVisabilty(isVisable: Boolean) {

        val activityName = findViewById<TextView>(R.id.actionName)
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation)
        if (isVisable) {
//            activityName.visibility = View.VISIBLE
//            actionButton.visibility = View.VISIBLE
            navigationView.visibility = View.VISIBLE
        } else {
//            activityName.visibility = View.GONE
//            actionButton.visibility = View.GONE
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
        if (MyServiceInterceptor.authentication == "" && prefHelper!!.userToken != null && !prefHelper!!.userToken.equals("")) {
            MyServiceInterceptor.authentication = prefHelper!!.userToken
        }
    }


}
