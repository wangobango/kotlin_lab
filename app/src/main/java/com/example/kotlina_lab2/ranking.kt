package com.example.kotlina_lab2

import android.accounts.NetworkErrorException
import android.app.DownloadManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

import kotlinx.android.synthetic.main.activity_ranking.*
import kotlinx.android.synthetic.main.ranking.*
import java.net.URL
import android.content.Intent
import android.telecom.Call
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.ranking.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import javax.security.auth.callback.Callback
import kotlin.concurrent.schedule
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.ConnectException
import java.net.HttpURLConnection

class ranking : AppCompatActivity() {
    lateinit var progress: ProgressBar
    private lateinit var listView: ListView

    fun parse(json: String): JSONObject? {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(json)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonObject
    }


    fun loadData(): String{
        try {
            return URL("http://hufiecgniezno.pl/br/record.php?f=get").readText()
        } catch (ex: Exception){
            return readRecordListFromCache()
        }
    }

    fun setRecordList(list: String){
        val loginShared = this.getSharedPreferences("com.example.kotlina_lab2.prefs",0)
        val editor = loginShared!!.edit()
        editor.putString("recordList",list)
        editor.apply()
    }


    fun readRecordListFromCache():String{
        val loginShared = this.getSharedPreferences("com.example.kotlina_lab2.prefs",0)
        return loginShared.getString("recordList","").orEmpty()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ranking)
        progress = findViewById(R.id.progressBar)
        listView = findViewById<ListView>(R.id.listView)
        val listItems = arrayOfNulls<String>(10)
        doAsync {
            var data = loadData()
            var pom = data.replace("[[","").replace("]]","").split("],[")
            uiThread {
                setRecordList(data)
                for(i in 0..pom.size-1){
                    var msg = pom[i].replace("\"","").split(",")
                    listItems[i] = "Gracz: "+msg[1]+", z rekordem: "+msg[2]
                }
                val adapter = ArrayAdapter(this@ranking ,android.R.layout.simple_list_item_1, listItems)
                this@ranking.listView.adapter = adapter
            }
        }
    }
}
