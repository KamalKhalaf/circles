package com.circles.circlesapp.loginsignup

import android.app.DatePickerDialog
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.CardView
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.circles.circlesapp.R
import com.circles.circlesapp.helpers.SharedPrefHelper
import com.circles.circlesapp.helpers.Utility
import com.circles.circlesapp.helpers.base.BaseActivity
import com.circles.circlesapp.helpers.ui.GenericTextWatcher
import com.circles.circlesapp.retrofit.RetrofitClient
import com.mukesh.countrypicker.Country
import com.mukesh.countrypicker.CountryPicker
import com.mukesh.countrypicker.OnCountryPickerListener
import com.vincent.filepicker.Constant
import com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST
import com.vincent.filepicker.activity.ImagePickActivity
import com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA
import com.vincent.filepicker.filter.entity.ImageFile
import kotlinx.android.synthetic.main.activity_sign_up.*
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class SignUpActivity : BaseActivity(), OnCountryPickerListener {
    override fun onSelectCountry(p0: Country?) {
        input_country.setText(p0!!.name)
        input_phone.prefixText = p0.dialCode
    }

    lateinit var edittext: EditText
    lateinit var myCalendar: Calendar
    lateinit var content: FrameLayout
    lateinit var cardview_birth: CardView
    lateinit var layoutGender: LinearLayout
    private var mFile: File? = null
    var requestFile: RequestBody? = null
    lateinit var upload_image: ImageView
    private var isGenderSelected: Boolean = false
    private var gender: Boolean = false
    //    val sharedPref: MySharedPreferences? = MySharedPreferences(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up)
        hideSoftKeyboardListener()
        val builder = CountryPicker.Builder()
        input_country.setOnClickListener {
            builder.with(this).listener(this).build().showDialog(supportFragmentManager)
        }

        //card view gender selected
        female.setOnClickListener {
            radio_male.isSelected = false
            radio_male.setBackgroundResource(R.drawable.male)
            radio_female.setBackgroundResource(R.drawable.female_selected)
            gender = false
            isGenderSelected = true
            femaleTV.setTextColor(Color.BLACK)

            maleTV.setTextColor(Color.GRAY)

        }
        male.setOnClickListener {
            radio_female.isSelected = false
            radio_female.setBackgroundResource(R.drawable.female)
            radio_male.setBackgroundResource(R.drawable.male_selected)
            gender = true
            isGenderSelected = true
            maleTV.setTextColor(Color.BLACK)

            femaleTV.setTextColor(Color.GRAY)
        }
        //

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

        upload_image = findViewById<View>(R.id.uplad_image) as ImageView
        upload_image.setOnClickListener(View.OnClickListener {
            openImagePicker()
        })
        var checkbox_retype = findViewById<View>(R.id.check_retype_password) as CheckBox
        checkbox_retype.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                // show password
                input_retype_password.transformationMethod = PasswordTransformationMethod.getInstance()

            } else {
                // hide password
                input_retype_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        }
        //end showing password


        //for changing icon states when edittext is not empty
        input_user_firstname.addTextChangedListener(GenericTextWatcher(input_user_firstname, 0))
        input_email.addTextChangedListener(GenericTextWatcher(input_email, 1))
        input_country.addTextChangedListener(GenericTextWatcher(input_country, 2))
        input_city.addTextChangedListener(GenericTextWatcher(input_city, 3))
        input_phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim { it <= ' ' }.isEmpty()) {
                    phoneDrawableLeft.setImageDrawable(resources.getDrawable(R.drawable.phone))
                } else {
                    phoneDrawableLeft.setImageDrawable(resources.getDrawable(R.drawable.phone_selected))

                }
            }
        })
        input_birth.addTextChangedListener(GenericTextWatcher(input_birth, 5))
        input_password.addTextChangedListener(GenericTextWatcher(input_password, 6))
        input_retype_password.addTextChangedListener(GenericTextWatcher(input_retype_password, 7))
        input_user_lastname.addTextChangedListener(GenericTextWatcher(input_user_lastname, 8))
        input_user_name_unique.addTextChangedListener(GenericTextWatcher(input_user_name_unique, 9))

        //end

        //calendar fot the birthday edittext
        myCalendar = Calendar.getInstance()
        edittext = findViewById(R.id.input_birth)
        content = findViewById(R.id.content)
        cardview_birth = findViewById(R.id.cardview_birth)
        layoutGender = findViewById(R.id.layoutGender)
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }
        edittext.setOnClickListener {
            // TODO Auto-generated method stub
            DatePickerDialog(this@SignUpActivity, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        //calendar end


        cardview_signup.setOnClickListener {
            userSignUp()
        }
    }

    //calendar method start
    private fun updateLabel() {
        val myFormat = "dd-MM-yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        edittext.setText(sdf.format(myCalendar.time))
    }
    //calendar method end


    private fun userSignUp() {
        val firstName = input_user_firstname.text.toString().trim()
        val lastName = input_user_lastname.text.toString().trim()
        val email = input_email.text.toString().trim()
        val phone = input_phone.text.toString().trim()

        val city = input_city.text.toString().trim()
        val country = input_country.text.toString().trim()
        val birthdate = input_birth.text.toString().trim()
        val user_unique = input_user_name_unique.text.toString().trim()
        val gender_type = gender
        val password = input_password.text.toString().trim()
        val password_retype = input_retype_password.text.toString().trim()

        if (firstName.isEmpty()) {
            input_user_firstname.setError("name is required")
            input_user_firstname.requestFocus()
            return
        }

        if (lastName.isEmpty()) {
            input_user_lastname.setError("Name required")
            input_user_lastname.requestFocus()
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

        if (password.isEmpty()) {
            input_password.setError("Password required")
            input_password.requestFocus()
            return
        }
        if (password.length < 8) {
            input_password.setError("Password should be atleast 8 character long")
            input_password.requestFocus()
            return
        }

        if (password_retype.isEmpty()) {
            input_retype_password.setError("Password required")
            input_retype_password.requestFocus()
            return
        }
        if (password_retype.length < 8) {
            input_retype_password.setError("Password should be atleast 8 character long")
            input_retype_password.requestFocus()
            return
        }
        if (password_retype != password) {
            input_retype_password.setError("Passwords should match")
            input_password.setError("Passwords should match")
            return
        }

        if(!isGenderSelected){
            Snackbar.make(content, getString(R.string.miss_gender), Snackbar.LENGTH_LONG).setAction(getString(R.string.close)) { view -> }.setActionTextColor(resources.getColor(android.R.color.white)).show()
            Utility.showShakeError(this, layoutGender)
            return
        }

//        if (phone.isEmpty()) {
//            input_phone.setError("Phone required")
//            input_phone.requestFocus()
//            return
//        }
        if (terms_concitions.isChecked == false) {
            Toast.makeText(this@SignUpActivity, "Please confirm our terms and conditions", Toast.LENGTH_SHORT).show()
            return
        }


        showDialog()


        AndroidNetworking.upload(RetrofitClient.BASE_URL + "signup")
                .addHeaders("Accept", "application/json")
                .addMultipartParameter("first_name", firstName)
                .addMultipartParameter("last_name", lastName)
                .addMultipartParameter("email", email)
//                .addMultipartParameter("phone", phone)
//                .addMultipartParameter("city", city)
//                .addMultipartParameter("country", country)
//                .addMultipartParameter("birthdate", birthdate)
                .addMultipartParameter("username", user_unique)
                .addMultipartParameter("gender", "" + gender)
                .addMultipartParameter("password", password)
                .addMultipartParameter("password_confirmation", password_retype)
                .addMultipartFile("profile_image", mFile)
                .build().getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onError(anError: ANError?) {
                        hideDialog()
                        if (anError!!.errorCode == 422) {
                            Toast.makeText(this@SignUpActivity, "The given data was invalid.", Toast.LENGTH_LONG).show()
                        } else if (anError!!.errorCode == 403) {
                            Toast.makeText(this@SignUpActivity, "User doesn't has permission.", Toast.LENGTH_LONG).show()
                        } else if (anError!!.errorCode == 401) {
                            Toast.makeText(this@SignUpActivity, "Unauthorized user", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onResponse(response: JSONObject) {
                        hideDialog()
                        // do anything with response      +      }

//                        Toast.makeText(this@SignUpActivity, "User created successfully.", Toast.LENGTH_LONG).show()
                        SharedPrefHelper().saveLogin(this@SignUpActivity, email, password)

                        var intent = Intent(this@SignUpActivity, ConfirmUserActivity::class.java)
                        startActivity(intent)


                    }
                })
//        var call: Call<SignUpResponse> = RetrofitClient
//                .getInstance()
//                .api
//                .createUser(firstName, lastName, email, phone, city, country, birthdate,
//                        user_unique, gender, password, password_retype, requestFile)
//
//        call.enqueue(object : Callback<SignUpResponse> {
//            override fun onFailure(call: Call<SignUpResponse>?, t: Throwable?) {
//                hideDialog()
//                Toast.makeText(this@SignUpActivity, "OnFailure", Toast.LENGTH_LONG).show()
//
//            }
//
//            override fun onResponse(call: Call<SignUpResponse>?, response: Response<SignUpResponse>?) {
//                if (response!!.isSuccessful && response.code() == 201) {
//                    hideDialog()
//                    var s = response.body()!!.message
//                    Toast.makeText(this@SignUpActivity, s, Toast.LENGTH_LONG).show()
//                    // Check if the email is confirmed and User could sing up first
//
//                    SharedPrefHelper().saveLogin(this@SignUpActivity, email, password)
//
//                    var intent = Intent(this@SignUpActivity, ConfirmUserActivity::class.java)
//                    startActivity(intent)
//
//                } else {
//                    hideDialog()
//                    try {
//                        var signUpResponse: SignUpResponse
//
//                        var gson: Gson = Gson()
//                        signUpResponse = gson.fromJson(response.errorBody()?.string(), SignUpResponse::class.java)
//
//
//                        var validation = ""
//                        if (signUpResponse.errors.email.size != 0) {
//                            validation += signUpResponse.errors.email[0]
//                        }
//                        if (signUpResponse.errors.phone.size != 0) {
//                            validation += signUpResponse.errors.phone[0]
//                        }
//                        if (signUpResponse.errors.username.size != 0) {
//                            validation += signUpResponse.errors.username[0]
//                        }
//                        if (validation != "") {
//                            Toast.makeText(this@SignUpActivity, validation, Toast.LENGTH_LONG).show()
//                        }
//                    } catch (e: Exception) {
//                        hideDialog()
//                        Toast.makeText(this@SignUpActivity, e.message, Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//
//            }
//
//        })


    }

    private fun openImagePicker() {
//        ImagePicker.Builder(this)
//                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
//                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
//                .directory(ImagePicker.Directory.DEFAULT)
//                .extension(ImagePicker.Extension.PNG)
//                .allowMultipleImages(false)
//                .build()

        val intent1 = Intent(this, ImagePickActivity::class.java)
        intent1.putExtra(IS_NEED_CAMERA, true)
        intent1.putExtra(Constant.MAX_NUMBER, 1)
        intent1.putExtra(IS_NEED_FOLDER_LIST, true)
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
//            val mPaths = data!!.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH)
//            mFile = File(mPaths[0])
//            setmFile(mFile!!)
//            Glide.with(this).asBitmap().load(paths.get(0)).into(upload_image)
            var paths: List<String>
            val list = data!!.getParcelableArrayListExtra<ImageFile>(Constant.RESULT_PICK_IMAGE)
            val path: String? = null
            val mPaths = ArrayList<String>()
            paths = ArrayList<String>()
            for (i in list.indices) {
                mPaths.add(list[i].path)
            }

            if (paths == null) paths = ArrayList()
            paths.add(mPaths[list.size - 1])

            Glide.with(this).asBitmap().load(paths.get(0)).into(upload_image)

        }
    }

    fun setmFile(mFile: File) {
        this.mFile = mFile
    }

}