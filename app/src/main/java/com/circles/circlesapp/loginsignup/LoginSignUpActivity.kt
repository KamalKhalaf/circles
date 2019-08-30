package com.circles.circlesapp.loginsignup

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.circles.circlesapp.R
import com.circles.circlesapp.helpers.SharedPrefHelper
import com.circles.circlesapp.helpers.core.Constants
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor
import com.circles.circlesapp.phase2.views.ui.Home2
import com.facebook.CallbackManager
import kotlinx.android.synthetic.main.activity_login_sign_up.*

class LoginSignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        var EMAIL = "email"


        if (SharedPrefHelper(this).userToken != "" && SharedPrefHelper(this).getBoolean(Constants.islogin)) {
            val intent = Intent(this, Home2::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            MyServiceInterceptor.authentication = SharedPrefHelper(this).userToken;
            MyServiceInterceptor.userId = SharedPrefHelper(this).userId
            startActivity(intent)
        }
        //Full screen for no status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_sign_up)

        cardview_signup.setOnClickListener {

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        cardview_login.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        var callbackManager = CallbackManager.Factory.create()
    }
}
