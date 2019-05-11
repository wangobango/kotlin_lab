package com.example.kotlina_lab2

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import java.net.URL
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    var record = 0
    var chance = 0
    var steps = 0

    fun getRecord(){
        val loginShared = this.getSharedPreferences("com.example.kotlina_lab2.prefs",0)
        record = loginShared.getInt("recordValue",0)
    }

    fun dialogShow(){
        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle("Wygrałeś!")
        builder.setMessage("Udało ci się za "+chance.toString()+" podejściem")

        builder.setPositiveButton("Super") { dialog, which ->
            Toast.makeText(applicationContext, "Wyslosowałeś nową liczbę", Toast.LENGTH_SHORT).show()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun setRecord(){
        if(record>chance){
            record = chance
            val loginShared = this.getSharedPreferences("com.example.kotlina_lab2.prefs",0)
            val editor = loginShared!!.edit()
            editor.putInt("recordValue",record)
            editor.apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button2.setOnClickListener{
            getRecord()
            chance = Random.nextInt(0,20)
            textView.text = "Rekord wynosi: "+record.toString()
        }

        button.setOnClickListener{
            steps += 1
            val liczba = editText.text.toString().toInt()
            if(liczba == chance){
                setRecord()
                textView2.text = "Brawo ziom"
                dialogShow()
                steps = 0
                doAsync {
                    URL("http://hufiecgniezno.pl/br/record.php?f=add&id=132213&r="+record.toString())
                }
            } else if (liczba > chance){
                textView2.text = "Za dużo ! Mniej! W "+steps.toString()+" krokach"
            } else if (liczba < chance){
                textView2.text = "Za mało ! Wincyj! W "+steps.toString()+" krokach"
            }

        }

        button3.setOnClickListener{
            val intent = Intent(this, ranking::class.java)
            startActivity(intent)
        }


    }
}
