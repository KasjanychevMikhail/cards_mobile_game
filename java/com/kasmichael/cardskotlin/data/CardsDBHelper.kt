package com.kasmichael.cardskotlin.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase

import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class CardsDBHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_CARDS_TABLE =
            ("CREATE TABLE " + CardsContract.CardsTable.TABLE_NAME + " ("
                    + CardsContract.CardsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + CardsContract.CardsTable.COLUMN_IMAGE + " INTEGER NOT NULL DEFAULT 0, "
                    + CardsContract.CardsTable.COLUMN_RARITY + " INTEGER DEFAULT 0, "
                    + CardsContract.CardsTable.COLUMN_TITLE + " STRING, "
                    + CardsContract.CardsTable.COLUMN_TIME + " INTEGER NOT NULL DEFAULT 0);")
        db.execSQL(SQL_CREATE_CARDS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.w("SQLite", "Обновление с версии $oldVersion на версию $newVersion")
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME)
    }

    companion object {
        val LOG_TAG = CardsDBHelper::class.java.simpleName
        private const val DATABASE_NAME = "cards.db"
        private const val DATABASE_VERSION = 1
    }
}