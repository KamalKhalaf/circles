package com.circles.circlesapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.circles.circlesapp.helpers.AppConstants.post_from_splash
import com.circles.circlesapp.loginsignup.LoginActivity
import com.circles.circlesapp.loginsignup.LoginSignUpActivity
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import java.util.*


class SplashScreen : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH: Long = 3500

    override fun onCreate(savedInstanceState: Bundle?) {
        //Full screen for no status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true)
                .build()
        Fabric.with(fabric)

        val res = getResources()
        val dm = res.getDisplayMetrics()
        val conf = res.getConfiguration()
        conf.setLocale(Locale("en"))
        res.updateConfiguration(conf, dm)
        val booleanExtra = intent.getBooleanExtra("openNotifications", false)
        Log.d("booleanExtra", booleanExtra.toString())
        super.onCreate(savedInstanceState)

        val intent = intent
        val data = intent.data

        if (data != null) {
            Log.d("data", data.lastPathSegment)
            post_from_splash = data.lastPathSegment.toInt()
        }


        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({
            val mainIntent = Intent(this@SplashScreen, LoginActivity::class.java)
            this@SplashScreen.startActivity(mainIntent)
            this@SplashScreen.finish()
            /* Create an Intent that will start the Menu-Activity. */
        }, SPLASH_DISPLAY_LENGTH)
    }
}

