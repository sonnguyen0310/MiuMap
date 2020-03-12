package com.sng.miumap.ui.login

import android.app.Activity
import android.content.Intent

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sng.miumap.R
import kotlinx.android.synthetic.main.activity_register.*

class Register_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

    }


        fun Register(view: View) {
            val data = Intent()
            val user = User(
                editText.text.toString(),
                etLastName.text.toString(),
                etEmail.text.toString(),
                etPassword.text.toString()
            )
            //---set the data to pass back
            data.putExtra("user", user)
            setResult(Activity.RESULT_OK, data)
            //---close the activity---
            finish()
        }
    }

