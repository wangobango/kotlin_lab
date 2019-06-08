package com.example.kotlina_lab2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlina_lab2.DB.DatabaseHelper
import com.example.kotlina_lab2.DB.RemoteDBHelper
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        button4.setOnClickListener {
            val remoteDBHelper = RemoteDBHelper(this)
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
