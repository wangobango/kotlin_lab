package com.example.kotlina_lab2.DB

import android.R
import android.content.Context
import android.os.AsyncTask
import android.widget.ArrayAdapter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


operator fun JSONArray.iterator(): Iterator<JSONObject> =
    (0 until length()).asSequence().map { get(it) as JSONObject }.iterator()

val TIMEOUT = 10*1000

class HttpTask(callback: (String?) -> Unit) : AsyncTask<String, Unit, String>()  {

    var callback = callback

    override fun doInBackground(vararg params: String): String? {
        val url = URL(params[1])
        val httpClient = url.openConnection() as HttpURLConnection
        httpClient.setReadTimeout(TIMEOUT)
        httpClient.setConnectTimeout(TIMEOUT)
        httpClient.requestMethod = params[0]

        if (params[0] == "POST") {
            httpClient.instanceFollowRedirects = false
            httpClient.doOutput = true
            httpClient.doInput = true
            httpClient.useCaches = false
            httpClient.setRequestProperty("Content-Type", "application/json; charset=utf-8")
        }
        try {
            if (params[0] == "POST") {
                httpClient.connect()
                val os = httpClient.getOutputStream()
                val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                writer.write(params[2])
                writer.flush()
                writer.close()
                os.close()
            }
            if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                val stream = BufferedInputStream(httpClient.inputStream)
                val data: String = readStream(inputStream = stream)
                return data
            } else {
                println("ERROR ${httpClient.responseCode}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            httpClient.disconnect()
        }

        return null
    }

    fun readStream(inputStream: BufferedInputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { stringBuilder.append(it) }
        return stringBuilder.toString()
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        callback(result)
    }
}

class RemoteDBHelper(private val myContext: Context) {


    fun login(login:String, password: String): Boolean {
        val id = URL(url+port+"/user/login/"+login+"/"+password).readText()
        return id == "OK"
    }

    fun register(login: String, password: String): Boolean {
        val result = URL(url+port+"/user/register/"+login+"/"+password).readText()
        return result == "OK"
    }

    fun addNewRecord(login: String, score:Int): Boolean {
        val result = URL(url+port+"/user/newScore/"+login+"/"+score.toString()).readText()
        return result == "OK"
    }

    fun getUserRecord(login: String): String {
        var result = URL(url+ port+"/user/record/"+login).readText()
        var res = result.split(":")[1].replace("}","").replace("\"","")
        println(res)
        return res
    }

    fun getTopScores(): String {
        return URL(url+ port+"/top").readText()
    }

    companion object{
        val url = "http://192.168.1.16"
        val port = ":3000"
    }
}