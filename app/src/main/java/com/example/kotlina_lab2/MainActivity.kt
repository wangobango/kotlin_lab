package com.example.kotlina_lab2

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.example.kotlina_lab2.DB.DatabaseHelper
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

    fun getRecord(){
        val loginShared = this.getSharedPreferences("com.example.kotlina_lab2.prefs",0)
        record = loginShared.getInt("recordValue",21)
    }

    fun dialogShow(){
        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle("Wygrałeś!")
        builder.setMessage("Udało ci się za "+steps.toString()+" podejściem")

        builder.setPositiveButton("Super") { dialog, which ->
            Toast.makeText(applicationContext, "Wyslosowałeś nową liczbę", Toast.LENGTH_SHORT).show()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun setRecord(){
        if(record>steps){
            record = steps
            dbHelper.updateUserScore(currentLogin,currentPassword,record.toString())
            val loginShared = this.getSharedPreferences("com.example.kotlina_lab2.prefs",0)
            val editor = loginShared!!.edit()
            editor.putInt("recordValue",record)
            editor.apply()
        }
    }

    fun getMaxScore():Int{
        val loginShared = this.getSharedPreferences("com.example.kotlina_lab2.prefs",0)
        return loginShared.getInt("scoreValue",0)
    }

    fun setMaxScore(score:Int){
        if(score>getMaxScore()){
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


        button2.setOnClickListener{
            newGameFlag = 1
            getRecord()
            chance = Random.nextInt(0,20)
            textView.text = "Rekord wynosi: "+record.toString()
            textView4.text = "Wynik wynosi: "+getMaxScore().toString()
        }

        button.setOnClickListener{
            if(newGameFlag>0){
                steps += 1
                val liczba = editText.text.toString().toInt()
                if(liczba == chance){
                    setRecord()
                    setMaxScore(getScoreValue(steps))
                    textView4.text = "Wynik wynosi: "+getMaxScore().toString()
                    textView2.text = "Brawo ziom"
                    dialogShow()
                    steps = 0
                    score = 0
                    newGameFlag = 0
                    doAsync {
                        URL("http://hufiecgniezno.pl/br/record.php?f=add&id="+currentLogin+"&r="+dbHelper.getUserScore(currentLogin,currentPassword))
                    }
                } else if (liczba > chance){
                    textView2.text = "Za dużo ! Mniej! W "+steps.toString()+" krokach"
                } else if (liczba < chance){
                    textView2.text = "Za mało ! Wincyj! W "+steps.toString()+" krokach"
                }
            } else {
                Toast.makeText(applicationContext, "Musisz najpierw rozpocząć nową grę!", Toast.LENGTH_SHORT).show()
            }
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
