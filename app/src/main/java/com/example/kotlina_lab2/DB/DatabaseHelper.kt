
package com.example.kotlina_lab2.DB

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import org.jetbrains.anko.db.*


class DatabaseHelper(private val myContext: Context) : SQLiteOpenHelper(myContext, DATABASE_NAME, null, DATABASE_VERSION) {

    private var myDataBase: SQLiteDatabase? = null

    @SuppressLint("SimpleDateFormat")
    private val dateformate = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss")

    // Create a empty database on the system
    @Throws(IOException::class)
    fun createDataBase() {

        val dbExist = checkDataBase()

        if (dbExist) {
            Log.v("DB Exists", "db exists")

        } else {

            this.readableDatabase
            try {
                this.close()
//                copyDataBase()
//                onCreate(myDataBase!!)
            } catch (e: IOException) {
                throw Error("Error copying database")
            }

        }

    }

    // Check database already exist or not
    private fun checkDataBase(): Boolean {
        var checkDB = false
        try {
            val myPath = DATABASE_PATH + DATABASE_NAME
            val dbfile = File(myPath)
            checkDB = dbfile.exists()
        } catch (e: SQLiteException) {
            println("delete database file.")
        }

        return checkDB
    }

    // Copies your database from your local assets-folder to the just created
    // empty database in the system folder
    @Throws(IOException::class)
    private fun copyDataBase() {

        val outFileName = DATABASE_PATH + DATABASE_NAME

        val myOutput = FileOutputStream(outFileName)
        val myInput = myContext.assets.open(DATABASE_NAME)

        val buffer = ByteArray(1024)
        var length: Int = myInput.read(buffer)
        while ((length) > 0) {
            myOutput.write(buffer, 0, length)
            length = myInput.read(buffer)
        }
        myInput.close()
        myOutput.flush()
        myOutput.close()
    }

    // delete database
    fun db_delete() {
        val file = File(DATABASE_PATH + DATABASE_NAME)
        if (file.exists()) {
            file.delete()
            println("delete database file.")
        }
    }

    // Open database
    @Throws(SQLException::class)
    fun openDatabase() {
        val myPath = DATABASE_PATH + DATABASE_NAME
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
            SQLiteDatabase.OPEN_READWRITE)
    }

    @Synchronized @Throws(SQLException::class)
    fun closeDataBase() {
        if (myDataBase != null)
            myDataBase!!.close()
        super.close()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion) {
            Log.v("Database Upgrade", "Database version higher than old.")
            db_delete()
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable("Users", true,
            "id" to INTEGER + PRIMARY_KEY + UNIQUE ,
            "name" to TEXT,
            "password" to TEXT,
            "score" to TEXT)

    }

    fun userSignup(email: String, password: String): Boolean
    {
        var add = "insert into ${TABLE_ACCOUNTS} (name,password,score) values('"+email+"','"+password+"','"+0+"')"
        try {
            myDataBase!!.execSQL(add)
            return true
        }
        catch (e: Exception)
        {
            Log.e(TAG, "Error = "+e.toString())
            return false
        }

    }

    fun userLogin(email: String , password: String) : Boolean
    {
        var ret = "select * from ${TABLE_ACCOUNTS} where name = '"+email+"' AND password = '"+password+"'"
        var cursor: Cursor = myDataBase!!.rawQuery(ret, null)
        return cursor.count == 1
    }

    fun getUserScore(email: String, password: String): String {
        var ret = "select score from ${TABLE_ACCOUNTS} where name = '"+email+"' AND password = '"+password+"'"
        var cursor: Cursor = myDataBase!!.rawQuery(ret, null)
        cursor.moveToFirst()
        var res =  cursor.getString(0)
        cursor.close()
        return res
    }

    fun updateUserScore(email: String, password: String, score: String): Boolean {
        var up = "UPDATE ${TABLE_ACCOUNTS} SET score = '"+score+"' where name = '"+email+"' AND password = '"+password+"'"
        try {
            myDataBase!!.execSQL(up)
            return true
        }
        catch (e: Exception)
        {
            Log.e(TAG, "Error = "+e.toString())
            return false
        }

    }

    companion object {
        val DATABASE_NAME = "first.db"// "db.sqlite";
        @SuppressLint("SdCardPath")
        val DATABASE_PATH = "/data/data/com.example.kotlina_lab2/databases/"
        val DATABASE_VERSION = 1
        val TABLE_ACCOUNTS = "Users"
        val TAG = "Error-->>class_dbhelper"
    }


}