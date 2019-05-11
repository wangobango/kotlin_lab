package com.example.kotlina_lab2

import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter
import android.widget.ListView

import kotlinx.android.synthetic.main.activity_ranking.*
import kotlinx.android.synthetic.main.content_ranking.*
import java.net.URL

class getListTask(private var activity: MainActivity) : AsyncTask<Void, Void, Void>() {
    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg params: Void?): Void? {
        val list = URL("http://hufiecgniezno.pl/br/record.php?f=get")
        activity.ranking.text = list.toString()
        return null;
    }

//    override fun onPostExecute(vararg params: Void?): Void?  {
//        super.onPostExecute()
//        // ...
//    }
}

class ranking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)


    }

}
