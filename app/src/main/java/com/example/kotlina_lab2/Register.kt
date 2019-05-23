package com.example.kotlina_lab2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jk.developers.firstkotlinapp.DatabaseHelper
import kotlinx.android.synthetic.main.activity_register.*


class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        button4.setOnClickListener {
            val dbHelper = DatabaseHelper(this)
            dbHelper.createDataBase()
            dbHelper.openDatabase()
            dbHelper.userSignup(register_login.text.toString(),register_password.text.toString())
            dbHelper.closeDataBase()
        }



    }
}
