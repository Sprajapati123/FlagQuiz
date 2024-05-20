package com.example.flagquiz.database

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.flagquiz.models.FlagsModel
import com.techmania.flagquizwithsqlitedemo.DatabaseCopyHelper

class FlagsDao {
    fun getRandomTenRecords(helper: DatabaseCopyHelper): ArrayList<FlagsModel> {

        val recordList = ArrayList<FlagsModel>()
        val database: SQLiteDatabase = helper.writableDatabase
        val cursor: Cursor =
            database.rawQuery("SELECT * FROM flags ORDER BY RANDOM() LIMIT 10", null)

        val idIndex = cursor.getColumnIndex("flag_id")
        val countryNameIndex = cursor.getColumnIndex("country_name")
        val flagNameIndex = cursor.getColumnIndex("flag_name")

        if (idIndex == -1 || countryNameIndex == -1 || flagNameIndex == -1) {
            Log.e("FlagsDao", "Invalid column index")
            cursor.close()
            return recordList
        }

        while (cursor.moveToNext()) {
            val id = cursor.getInt(idIndex)
            val countryName = cursor.getString(countryNameIndex)
            val flagName = cursor.getString(flagNameIndex)

            if (countryName != null && flagName != null) {
                val record = FlagsModel(
                    id,
                    countryName,
                    flagName
                )
                recordList.add(record)
            } else {
                Log.w("FlagsDao", "Null value encountered: id=$id, countryName=$countryName, flagName=$flagName")
            }
        }
        cursor.close()
        return recordList
    }

    fun getRandomThreeRecords(helper: DatabaseCopyHelper, id: Int): ArrayList<FlagsModel> {

        val recordList = ArrayList<FlagsModel>()
        val database: SQLiteDatabase = helper.writableDatabase
        val cursor: Cursor =
            database.rawQuery("SELECT * FROM flags WHERE flag_id != ? ORDER BY RANDOM() LIMIT 3", arrayOf(id.toString()))

        val idIndex = cursor.getColumnIndex("flag_id")
        val countryNameIndex = cursor.getColumnIndex("country_name")
        val flagNameIndex = cursor.getColumnIndex("flag_name")

        if (idIndex == -1 || countryNameIndex == -1 || flagNameIndex == -1) {
            Log.e("FlagsDao", "Invalid column index")
            cursor.close()
            return recordList
        }

        while (cursor.moveToNext()) {
            val flagId = cursor.getInt(idIndex)
            val countryName = cursor.getString(countryNameIndex)
            val flagName = cursor.getString(flagNameIndex)

            if (countryName != null && flagName != null) {
                val record = FlagsModel(
                    flagId,
                    countryName,
                    flagName
                )
                recordList.add(record)
            } else {
                Log.w("FlagsDao", "Null value encountered: id=$flagId, countryName=$countryName, flagName=$flagName")
            }
        }
        cursor.close()
        return recordList
    }
}
