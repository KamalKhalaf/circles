package com.circles.circlesapp.loginsignup

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.circles.circlesapp.R
import com.circles.circlesapp.helpers.SharedPrefHelper
import com.circles.circlesapp.helpers.base.BaseActivity
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor
import com.circles.circlesapp.helpers.ui.GenericTextWatcher
import com.circles.circlesapp.phase2.views.ui.Home2
import com.circles.circlesapp.retrofit.RetrofitClient
import com.circles.circlesapp.retrofit.responses.ConfirmEmailResponse
import com.circles.circlesapp.retrofit.responses.LoginResponse
import kotlinx.android.synthetic.main.activity_confirm_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConfirmUserActivity : BaseActivity() {
    lateinit var accessToken: String
    lateinit var tokenType: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_user)

        input_email.addTextChangedListener(GenericTextWatcher(input_email, 0))
        input_code_confirmation.addTextChangedListener(GenericTextWatcher(input_code_confirmation, 6))
        var checkbox = findViewById<View>(R.id.check_password) as CheckBox
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                // show password
                input_code_confirmation.transformationMethod = PasswordTransformationMethod.getInstance()

            } else {
                // hide password
                input_code_confirmation.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        }


        hideSoftKeyboardListener()
//        accessToken = intent.getStringExtra("ACCESS_TOKEN")
//        tokenType = intent.getStringExtra("TOKEN_TYPE")

        if (SharedPrefHelper().getUserEmail(this) != null && SharedPrefHelper().getUserPassword(this) != null) {
            input_email.setText(SharedPrefHelper().getUserEmail(this))
        }

        cardview_login.setOnClickListener {
            emailConfirmation()
        }
    }

    private fun emailConfirmation() {
        val email = input_email.text.toString().trim()
        val code = input_code_confirmation.text.toString().trim()

        if (email.isEmpty() || email.length < 10) {
            input_email.setError("email is required")
            input_email.requestFocus()
            return
        }

        if (code.isEmpty() || code.length < 4) {
            input_code_confirmation.setError("code required")
            input_code_confirmation.requestFocus()
            return
        }

        var call: Call<ConfirmEmailResponse> = RetrofitClient
                .getInstance()
                .api
                .confirmEmail(email, code)

        showDialog()
        call.enqueue(object : Callback<ConfirmEmailResponse> {
            override fun onFailure(call: Call<ConfirmEmailResponse>?, t: Throwable?) {
                hideDialog()
                Toast.makeText(this@ConfirmUserActivity, "Error happened, Please try again", Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<ConfirmEmailResponse>?, response: Response<ConfirmEmailResponse>?) {

                if (response!!.isSuccessful) {

//                    var s = response.body()!!.message
//                    Toast.makeText(this@ConfirmUserActivity, s, Toast.LENGTH_LONG).show()

//                    var dataIntent = Intent(this@ConfirmUserActivity, Home::class.java)
//                    dataIntent.putExtra("ACCESS_TOKEN", accessToken)
//                    dataIntent.putExtra("TOKEN_TYPE", tokenType)
//                    startActivity(dataIntent)

                    callLoginApi(SharedPrefHelper().getUserEmail(this@ConfirmUserActivity), SharedPrefHelper().getUserPassword(this@ConfirmUserActivity))
//
//                    var loginResponse :LoginResponse = response.body()!!
//
//                    var accesTokeString = loginResponse.accessToken
//                    var tokenType = loginResponse.tokenType
//
//                    var dataIntent = Intent(this@LoginActivity, Home::class.java)
//                    dataIntent.putExtra("ACCESS_TOKEN", accesTokeString)
//                    dataIntent.putExtra("TOKEN_TYPE", tokenType)
//                    startActivity(dataIntent)
//
//                    val bundle = Bundle()
//                    bundle.putString("ACCESS_TOKEN", accesTokeString)
//                    bundle.putString("TOKEN_TYPE", tokenType)
//                    val homeFragment = HomeFragment()


                } else if (response.code() == 401) {

                    hideDialog()
                    Toast.makeText(this@ConfirmUserActivity, "Check your email and password then try again", Toast.LENGTH_LONG).show()
                } else if (response.code() == 406) {
                    hideDialog()
                    Toast.makeText(this@ConfirmUserActivity, "User code is not acceptable.", Toast.LENGTH_LONG).show()


                }


            }

        })

    }


    private fun callLoginApi(email: String, passworde: String) {
        val deviceToken = SharedPrefHelper(this).deviceToken
        var call: Call<LoginResponse> = RetrofitClient
                .getInstance()
                .api
                .login(email, passworde, deviceToken)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                hideDialog()
                Toast.makeText(this@ConfirmUserActivity, "Error happened, Please try again", Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                hideDialog()

                if (response!!.isSuccessful) {

                    var s = response.body()!!.message
                    Toast.makeText(this@ConfirmUserActivity, s, Toast.LENGTH_LONG).show()

                    var loginResponse: LoginResponse = response.body()!!
                    var accessToken = loginResponse.accessToken
                    var tokenType = loginResponse.tokenType
                    MyServiceInterceptor.accessToken = accessToken;
                    MyServiceInterceptor.tokenType = tokenType;
                    MyServiceInterceptor.authentication = tokenType + " " + accessToken;
                    MyServiceInterceptor.userId = response.body()!!.id
                    val prefHelper = SharedPrefHelper(this@ConfirmUserActivity)
                    prefHelper.userToken = MyServiceInterceptor.getAuth()
                    prefHelper.saveUserId(MyServiceInterceptor.userId)
                    var dataIntent = Intent(this@ConfirmUserActivity, Home2::class.java)
                    dataIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    dataIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    dataIntent.putExtra("ACCESS_TOKEN", accessToken)
                    dataIntent.putExtra("TOKEN_TYPE", tokenType)

                    SharedPrefHelper().saveLogin(this@ConfirmUserActivity, email, passworde)

                    startActivity(dataIntent)
                    finish()
                } else {
                    Toast.makeText(this@ConfirmUserActivity, "Error happened, Please try again", Toast.LENGTH_LONG).show()
                }


            }

        })
    }

}
