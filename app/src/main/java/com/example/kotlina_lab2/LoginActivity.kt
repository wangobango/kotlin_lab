package com.example.kotlina_lab2

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.jk.developers.firstkotlinapp.DatabaseHelper

class LoginActivity : AppCompatActivity() {
    var _usernameText: EditText? = null
    var _passwordText: EditText? = null
    var _loginButton: Button? = null
    var _signupLink: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        _loginButton = findViewById(R.id.signin) as Button
        _signupLink = findViewById(R.id.signup) as TextView
        _passwordText = findViewById(R.id.password) as EditText
        _usernameText = findViewById(R.id.username) as EditText
        _loginButton!!.setOnClickListener { login() }

        _signupLink!!.setOnClickListener {
            // Start the Signup activity
            val intent = Intent(applicationContext, Register::class.java)
            startActivityForResult(intent, REQUEST_SIGNUP)
            finish()
        }

    }

    fun login(){
        val helper = DatabaseHelper(this)
        helper.createDataBase()
        helper.openDatabase()
        if(helper.userLogin(_usernameText!!.text.toString(),_passwordText!!.text.toString())) {
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
        } else {
            Toast.makeText(applicationContext, "User with given login/password doesn't exist !!!", Toast.LENGTH_SHORT).show()
        }
    }

    fun validate(): Boolean {
        var valid = true

        val email = _usernameText!!.text.toString()
        val password = _passwordText!!.text.toString()

        if (email.isEmpty()) {
            _usernameText!!.error = "enter a valid email address"
            valid = false
        } else {
            _usernameText!!.error = null
        }

        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            _passwordText!!.error = "between 4 and 10 alphanumeric characters"
            valid = false
        } else {
            _passwordText!!.error = null
        }

        return valid
    }

    companion object {
        private val TAG = "LoginActivity"
        private val REQUEST_SIGNUP = 0
    }
}
