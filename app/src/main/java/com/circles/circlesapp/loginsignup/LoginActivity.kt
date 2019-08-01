package com.circles.circlesapp.loginsignup

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.circles.circlesapp.Home
import com.circles.circlesapp.R
import com.circles.circlesapp.helpers.SharedPrefHelper
import com.circles.circlesapp.helpers.base.BaseActivity
import com.circles.circlesapp.helpers.core.Constants
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor
import com.circles.circlesapp.helpers.ui.GenericTextWatcher
import com.circles.circlesapp.retrofit.RetrofitClient
import com.circles.circlesapp.retrofit.responses.ForgetPassword
import com.circles.circlesapp.retrofit.responses.LoginResponse
import com.circles.circlesapp.retrofit.responses.ResetPassword
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : BaseActivity() {
    //    private val ARG_ACCESS_TOKEN = "ACCESS_TOKEN"
//    private val ARG_TOKEN_TYPE = "TOKEN_TYPE"
    private var mLastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)
        if (SharedPrefHelper().getUserEmail(this) != null && SharedPrefHelper().getUserPassword(this) != null) {
            LoginFromSharedPref(SharedPrefHelper().getUserEmail(this), SharedPrefHelper().getUserPassword(this))
        }
        hideSoftKeyboardListener()
        //on click sign up text view move to sign up activity
        val signupTV = findViewById<TextView>(R.id.signup_textview)
        signupTV.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        //end

        //for changing icon states when edittext is not empty
        input_email.addTextChangedListener(GenericTextWatcher(input_email, 0))
        input_password.addTextChangedListener(GenericTextWatcher(input_password, 6))
        //end


        //show password
        var checkbox = findViewById<View>(R.id.check_password) as CheckBox
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                // show password
                input_password.transformationMethod = PasswordTransformationMethod.getInstance()

            } else {
                // hide password
                input_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        }
        //end showing password


        val forgot_password = findViewById<View>(R.id.forgot_password) as TextView
        forgot_password.setOnClickListener {

            if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()

            val dialogForgotPassowrd = DialogForgotPassowrd(this, false)
            dialogForgotPassowrd.Show_forgot_dialog { isOk, resetModel ->


                if (isOk) {
                    callForgetPassword(resetModel.email, dialogForgotPassowrd)
                } else {

                }

            }
        }

        reset_password.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            val dialogForgotPassowrd = DialogForgotPassowrd(this, true)
            dialogForgotPassowrd.Show_forgot_dialog { isOk, resetModel ->

                if (isOk) {
                    callResetPassword(resetModel, dialogForgotPassowrd)
                } else {

                }

            }
        }


        btnServerLogin.setOnClickListener {
            LoginViaEmail()

        }
    }

    private fun LoginFromSharedPref(email: String, passworde: String) {
//        callLoginApi(email, passworde)
        input_email.setText(email)
        input_password.setText(passworde)
    }

    fun LoginViaEmail() {


        val email = input_email.text.toString().trim()
        val passworde = input_password.text.toString().trim()

        if (passworde.isEmpty()) {
            input_password.setError("Password required")
            input_password.requestFocus()
            return
        }
        if (passworde.length < 8) {
            input_password.setError("Password should be atleast 8 character long")
            input_password.requestFocus()
            return
        }
        if (email.isEmpty()) {
            input_email.setError("Email is required")
            input_email.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("Enter a valid email")
            input_email.requestFocus()
            return
        }

        callLoginApi(email, passworde)
    }

    private fun callLoginApi(email: String, passworde: String) {
        btnServerLogin.startAnimation();
        val deviceToken = SharedPrefHelper(this).deviceToken
        var call: Call<LoginResponse> = RetrofitClient
                .getInstance()
                .api
                .login(email, passworde, deviceToken)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                if (this@LoginActivity != null) {
                    btnServerLogin.revertAnimation()
                }
                Toast.makeText(this@LoginActivity, "network error", Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                if (this@LoginActivity != null) {
                    btnServerLogin.revertAnimation()
                }

                if (response!!.isSuccessful) {

                    if (response.body()!!.isVerified) {

                        var s = response.body()!!.message
//                        Toast.makeText(this@LoginActivity, s, Toast.LENGTH_LONG).show()

                        var loginResponse: LoginResponse = response.body()!!
                        var accessToken = loginResponse.accessToken
                        var tokenType = loginResponse.tokenType
                        MyServiceInterceptor.accessToken = accessToken;
                        MyServiceInterceptor.tokenType = tokenType;
                        MyServiceInterceptor.authentication = tokenType + " " + accessToken;
                        MyServiceInterceptor.userId = response.body()!!.id
                        val prefHelper = SharedPrefHelper(this@LoginActivity)
                        prefHelper.userToken = MyServiceInterceptor.getAuth()
                        prefHelper.profileImage = loginResponse.profile_image
                        prefHelper.saveUserId(MyServiceInterceptor.userId)


                        prefHelper.addData(Constants.is_celebrity , loginResponse.is_celebrity)
                        prefHelper.addData(Constants.islogin , true)
                        var dataIntent = Intent(this@LoginActivity, Home::class.java)
                        dataIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        dataIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        dataIntent.putExtra("ACCESS_TOKEN", accessToken)
                        dataIntent.putExtra("TOKEN_TYPE", tokenType)

                        SharedPrefHelper().saveLogin(this@LoginActivity, email, passworde)
                        startActivity(dataIntent)
                        finish()

                    } else {
                        SharedPrefHelper().saveLogin(this@LoginActivity, email, passworde)
                        var intent = Intent(this@LoginActivity, ConfirmUserActivity::class.java)
                        startActivity(intent)
                        finish()
                    }


                } else {
                    Toast.makeText(this@LoginActivity, "Error happened, Please try again", Toast.LENGTH_LONG).show()
                }


            }

        })
    }


    private fun callForgetPassword(email: String, dialogForgotPassowrd: DialogForgotPassowrd) {


        var call: Call<ForgetPassword> = RetrofitClient
                .getInstance()
                .api
                .forgotPassword(email)
        showDialog()
        call.enqueue(object : Callback<ForgetPassword> {
            override fun onFailure(call: Call<ForgetPassword>?, t: Throwable?) {
                hideDialog()
                Toast.makeText(this@LoginActivity, "Error happened, Please try again", Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<ForgetPassword>?, response: Response<ForgetPassword>?) {
                hideDialog()

                if (response!!.isSuccessful) {

                    if (response!!.body()!!.message.equals("Success.")) {
                        Toast.makeText(this@LoginActivity, "successfully, get your confirmation from mail and reset your password now", Toast.LENGTH_LONG).show()
                        dialogForgotPassowrd.dismiss();
                    }

                } else {
                    Toast.makeText(this@LoginActivity, "Error happened, Please try again", Toast.LENGTH_LONG).show()
                }


            }

        })
    }

    override fun onResume() {
        super.onResume()

        btnServerLogin.revertAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        btnServerLogin.dispose();
    }

    private fun callResetPassword(resetModel: ForgetResetModel?, dialogForgotPassowrd: DialogForgotPassowrd) {

        var call: Call<ResetPassword> = RetrofitClient
                .getInstance()
                .api
                .resetPassword(resetModel!!.email, resetModel!!.confirmationCode, resetModel!!.password, resetModel!!.retryPassword)
        showDialog()
        call.enqueue(object : Callback<ResetPassword> {
            override fun onFailure(call: Call<ResetPassword>?, t: Throwable?) {
                hideDialog()
                Toast.makeText(this@LoginActivity, "Error happened, Please try again", Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<ResetPassword>?, response: Response<ResetPassword>?) {
                hideDialog()

                if (response!!.isSuccessful) {

//                    if (response!!.body()!!.message.equals("Success.")) {
                    Toast.makeText(this@LoginActivity, "successfully, password changed", Toast.LENGTH_LONG).show()

                    var loginResponse: ResetPassword = response.body()!!
                    var accessToken = loginResponse.accessToken
                    var tokenType = loginResponse.tokenType
                    MyServiceInterceptor.accessToken = accessToken;
                    MyServiceInterceptor.tokenType = tokenType;
                    MyServiceInterceptor.authentication = tokenType + " " + accessToken;
                    MyServiceInterceptor.userId = response.body()!!.id
                    val prefHelper = SharedPrefHelper(this@LoginActivity)
                    prefHelper.userToken = MyServiceInterceptor.getAuth()

                    prefHelper.saveUserId(MyServiceInterceptor.userId)
                    var dataIntent = Intent(this@LoginActivity, Home::class.java)
                    dataIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    dataIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    dataIntent.putExtra("ACCESS_TOKEN", accessToken)
                    dataIntent.putExtra("TOKEN_TYPE", tokenType)

                    SharedPrefHelper().saveLogin(this@LoginActivity, resetModel.email, resetModel.password)
                    dialogForgotPassowrd.dismiss();
                    LoginFromSharedPref(resetModel.email, resetModel.password)
//                    }

                } else {
                    Toast.makeText(this@LoginActivity, "Error happened, Please try again", Toast.LENGTH_LONG).show()
                }


            }

        })
    }
}