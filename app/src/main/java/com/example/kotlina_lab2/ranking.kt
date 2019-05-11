package com.example.kotlina_lab2

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
import java.net.HttpURLConnection

class ranking : AppCompatActivity() {
    private val ADD_TASK_REQUEST = 1
    private val recordList: MutableList<String> = mutableListOf()
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
        return URL("http://hufiecgniezno.pl/br/record.php?f=get").readText()
    }

    fun setRecordList(list: Set<String>){
        val loginShared = this.getSharedPreferences("com.example.kotlina_lab2.prefs",0)
        val editor = loginShared!!.edit()
        editor.putStringSet("recordList",list)
        editor.apply()
    }


    fun readRecordListFromCache():MutableSet<String>?{
        val loginShared = this.getSharedPreferences("com.example.kotlina_lab2.prefs",0)
        return loginShared.getStringSet("recordList",null)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ranking)
        progress = findViewById(R.id.progressBar)

        listView = findViewById<ListView>(R.id.listView)
        //download records from server TODO

        val listItems = arrayOfNulls<String>(10)
//        progress = findViewById(R.id.progressBar)
//        progress.visibility = View.VISIBLE
//        Timer().schedule(3000){
//            progress.visibility = View.INVISIBLE
//        }

//        doAsync {
//            val data = loadData()
//            uiThread {
//                for(i in 0..10){
//                    listItems[i] = data
//                }
//                val adapter = ArrayAdapter(this@ranking ,android.R.layout.simple_list_item_1, listItems)
//                this@ranking.listView.adapter = adapter
//            }
//        }



    }


}
