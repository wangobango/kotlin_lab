package com.example.kotlina_lab2

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.example.kotlina_lab2.DB.DatabaseHelper
import com.example.kotlina_lab2.DB.RemoteDBHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import java.net.URL
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    var record = 0
    var chance = 0
    var steps = 0
    var score = 0
    var newGameFlag = 0
    var currentLogin = ""
    var currentPassword = ""
    val dbHelper = DatabaseHelper(this)
    val remoteHelper = RemoteDBHelper(this)

    fun getScoreValue(steps: Int):Int {
        if (steps == 1) {
            return 5
        } else if (steps >= 2 && steps <= 4) {
            return 3
        } else if (steps >= 5 && steps <= 6) {
            return 2
        } else if (steps >= 7 && steps <= 10) {
            return 1
        } else {
            return 0
        }
    }

    fun dialogShow(){
        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle("Lazy award....!")
        builder.setMessage("Congrats or something.... You just clicked a button 100 times because some stupid box told you too.....")

        builder.setPositiveButton("You're so smart ...") { dialog, which ->
            Toast.makeText(applicationContext, "Come again or something...", Toast.LENGTH_SHORT).show()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun getRecord(){
        val loginShared = this.getSharedPreferences("com.example.kotlina_lab2.prefs",0)
        record = loginShared.getInt("recordValue",0)
    }


    fun setRecord(){
        if(score>record){
            record = score
            dbHelper.updateUserScore(currentLogin,currentPassword,record.toString())
            val loginShared = this.getSharedPreferences("com.example.kotlina_lab2.prefs",0)
            val editor = loginShared!!.edit()
            textView.text = score.toString()
            editor.putInt("recordValue",record)
            editor.apply()
            Toast.makeText(applicationContext, "It's a new record! God, you're bored!", Toast.LENGTH_SHORT).show()
            remoteHelper.addNewRecord(getCurrentUser(), score)
        }
    }

    fun getMaxScore():Int{
        val loginShared = this.getSharedPreferences("com.example.kotlina_lab2.prefs",0)
        return loginShared.getInt("scoreValue",0)
    }

    fun getCurrentUser(): String{
        val loginShared = this.getSharedPreferences("com.example.kotlina_lab2.prefs",0)
        val login = loginShared.getString("currentLogin","ramon")!!
        return login
    }

    fun setMaxScore(score:Int){
        if (score>getMaxScore()) {
            val loginShared = this.getSharedPreferences("com.example.kotlina_lab2.prefs",0)
            val editor = loginShared!!.edit()
            editor.putInt("scoreValue",score)
            editor.apply()
        }
    }

    fun removeCurrentUser(){
        val loginShared = this.getSharedPreferences("com.example.kotlina_lab2.prefs",0)
        val editor = loginShared!!.edit()
        editor.putString("currentLogin","")
        editor.putString("currentPassword", "")
        editor.apply()
    }

    fun getCurrentUserIfExists(){
        val loginShared = this.getSharedPreferences("com.example.kotlina_lab2.prefs",0)
        currentLogin = loginShared.getString("currentLogin","")!!
        currentPassword = loginShared.getString("currentPassword","")!!
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(applicationContext, "Click 100 times to get a special reward !!!", Toast.LENGTH_SHORT).show()

        getCurrentUserIfExists()
        doAsync {
            if(currentLogin != "") {
                record = remoteHelper.getUserRecord(currentLogin).toInt()
                textView.text = record.toString()
            }
        }

        button.setOnClickListener{
            score+=1
            setRecord()
            if(score == 100) {
                dialogShow()
            }
            textView4.text = score.toString()
        }

        button3.setOnClickListener{
            val intent = Intent(this, ranking::class.java)
            startActivity(intent)
        }

        button5.setOnClickListener {
            removeCurrentUser()
            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
        }


    }
}
