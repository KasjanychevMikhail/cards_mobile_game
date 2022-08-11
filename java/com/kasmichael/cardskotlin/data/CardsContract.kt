package com.kasmichael.cardskotlin.data

import android.provider.BaseColumns

class CardsContract private constructor() {
    object CardsTable : BaseColumns {
        const val TABLE_NAME = "cards"
        const val _ID = BaseColumns._ID
        const val COLUMN_IMAGE = "image"
        const val COLUMN_RARITY = "rarity"
        const val COLUMN_TITLE = "title"
        const val COLUMN_TIME = "time"
    }
}