package com.circles.circlesapp.helpers.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.circles.circlesapp.R

class GenericTextWatcher internal constructor(private val editText: EditText, val index: Int) : TextWatcher {
    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        if (charSequence.toString().trim { it <= ' ' }.isEmpty()) {
            when (index) {
                0 ->editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0)
                1 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mail, 0, 0, 0)
                2 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flag, 0, 0, 0)
                3 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.houses, 0, 0, 0)
                4 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.phone, 0, 0, 0)
                5 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar, 0, 0, 0)
                6 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.locked, 0, 0, 0)
                7 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.locked, 0, 0, 0)
                8 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar, 0, 0, 0)
                9 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_name_icon, 0, 0, 0)

            }
        }
        else {

            when (index) {
                0 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar_selected, 0, 0, 0)
                1 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mail_selected, 0, 0, 0)
                2 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flag_selected, 0, 0, 0)
                3 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.houses_selected, 0, 0, 0)
                4 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.phone_selected, 0, 0, 0)
                5 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar_selected, 0, 0, 0)
                6 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.locked_selected, 0, 0, 0)
                7 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.locked_selected, 0, 0, 0)
                8 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar_selected, 0, 0, 0)
                9 -> editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_name_icon_selected, 0, 0, 0)

            }



        }
    }
    override fun afterTextChanged(editable: Editable) {

    }
}