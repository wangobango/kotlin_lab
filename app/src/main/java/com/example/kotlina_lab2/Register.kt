package com.example.kotlina_lab2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.kotlina_lab2.DB.DatabaseHelper
import com.example.kotlina_lab2.DB.RemoteDBHelper
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class Register : AppCompatActivity() {

    var _usernameText: EditText? = null
    var _passwordText: EditText? = null

    fun validate(): Boolean {
        var valid = true

        val email = _usernameText!!.text.toString()
        val password = _passwordText!!.text.toString()

        if (email.isEmpty()) {
            _usernameText!!.error = "enter a valid login"
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        _passwordText = findViewById(R.id.register_password) as EditText
        _usernameText = findViewById(R.id.register_login) as EditText

        button4.setOnClickListener {
            val remoteDBHelper = RemoteDBHelper(this)
            if(validate()) {
                doAsync {
                    val result = remoteDBHelper.register(register_login.text.toString(),register_password.text.toString())
                    uiThread {
                        if(result) {
                            Toast.makeText(applicationContext, "Użytkownik dodany pomyślnie!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Nie udało się dodać użytkownika!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }


    }
}
