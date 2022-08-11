package com.kasmichael.cardskotlin

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kasmichael.cardskotlin.adapter.CardsAdapter
import com.kasmichael.cardskotlin.adapter.CardsAdapter.OnCardClickListener
import com.kasmichael.cardskotlin.data.CardsContract
import com.kasmichael.cardskotlin.data.CardsDBHelper


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var cardsRecyclerView: RecyclerView? = null
    private var rareCardsRecyclerView: RecyclerView? = null
    private var superRareCardsRecyclerView: RecyclerView? = null
    private var cardsAdapter: CardsAdapter? = null
    private var rareCardsAdapter: CardsAdapter? = null
    private var superRareCardsAdapter: CardsAdapter? = null
    private val cardsList: ArrayList<Card> = ArrayList()
    private val rareCardsList: ArrayList<Card> = ArrayList()
    private val superRareCardsList: ArrayList<Card> = ArrayList()
    private var cardsDBHelper: CardsDBHelper? = null
    private var openChestBtn: Button? = null
    private var deleteAllBtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cardsRecyclerView = findViewById<View>(R.id.nonRareCardsRecyclerView) as RecyclerView
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        cardsRecyclerView!!.layoutManager = layoutManager
        cardsRecyclerView!!.setHasFixedSize(true)
        rareCardsRecyclerView = findViewById<View>(R.id.rareCardsRecyclerView) as RecyclerView
        val layoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rareCardsRecyclerView!!.layoutManager = layoutManager1
        rareCardsRecyclerView!!.setHasFixedSize(true)
        superRareCardsRecyclerView =
            findViewById<View>(R.id.superRareCardsRecyclerView) as RecyclerView
        val layoutManager2 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        superRareCardsRecyclerView!!.layoutManager = layoutManager2
        superRareCardsRecyclerView!!.setHasFixedSize(true)
        openChestBtn = findViewById<View>(R.id.openChestButton) as Button
        openChestBtn!!.setOnClickListener(this)
        deleteAllBtn = findViewById<View>(R.id.deleteAllCardsButton) as Button
        deleteAllBtn!!.setOnClickListener(this)
        cardsDBHelper = CardsDBHelper(this)
    }

    override fun onResume() {
        super.onResume()
        addCardsFromDB()
        if (cardsList.isEmpty()) {
            cardsList.add(Card(R.drawable.ic_empty, -1, "Пусто"))
        }
        val cardClickListener: OnCardClickListener = object : OnCardClickListener {
            override fun onCardClick(position: Int) {
                val card = cardsList[position]
                val cardInfo = CardInfo(card)
                cardInfo.show(supportFragmentManager, "card_info")
            }
        }
        cardsAdapter = CardsAdapter(cardsList, cardClickListener)
        cardsRecyclerView!!.adapter = cardsAdapter
        if (rareCardsList.isEmpty()) {
            rareCardsList.add(Card(R.drawable.ic_empty, -1, "Пусто"))
        }
        val rareCardClickListener: OnCardClickListener = object : OnCardClickListener {
            override fun onCardClick(position: Int) {
                val card = rareCardsList[position]
                val cardInfo = CardInfo(card)
                cardInfo.show(supportFragmentManager, "card_info")
            }
        }
        rareCardsAdapter = CardsAdapter(rareCardsList, rareCardClickListener)
        rareCardsRecyclerView!!.adapter = rareCardsAdapter
        if (superRareCardsList.isEmpty()) {
            superRareCardsList.add(Card(R.drawable.ic_empty, -1, "Пусто"))
        }
        val superRareCardClickListener: OnCardClickListener = object : OnCardClickListener {
            override fun onCardClick(position: Int) {
                val card = superRareCardsList[position]
                val cardInfo = CardInfo(card)
                cardInfo.show(supportFragmentManager, "card_info")
            }
        }
        superRareCardsAdapter = CardsAdapter(superRareCardsList, superRareCardClickListener)
        superRareCardsRecyclerView!!.adapter = superRareCardsAdapter
    }

    override fun onClick(view: View) {
        when (view.getId()) {
            R.id.openChestButton -> {
                val intent = Intent(this, OpenChestActivity::class.java)
                startActivity(intent)
            }
            R.id.deleteAllCardsButton -> deleteAllCards()
            else -> {}
        }
    }

    private fun deleteAllCards() {
        val db = cardsDBHelper!!.readableDatabase
        val clearCount = db.delete(CardsContract.CardsTable.TABLE_NAME, null, null)
        Toast.makeText(this, "Удалено $clearCount карт.", Toast.LENGTH_SHORT).show()
        onResume()
    }

    private fun addCardsFromDB() {
        cardsList.clear()
        rareCardsList.clear()
        superRareCardsList.clear()
        val db = cardsDBHelper!!.readableDatabase
        val projection = arrayOf(
            CardsContract.CardsTable._ID,
            CardsContract.CardsTable.COLUMN_IMAGE,
            CardsContract.CardsTable.COLUMN_RARITY,
            CardsContract.CardsTable.COLUMN_TITLE,
            CardsContract.CardsTable.COLUMN_TIME
        )
        val cursor: Cursor = db.query(
            CardsContract.CardsTable.TABLE_NAME,
            projection,
            CardsContract.CardsTable.COLUMN_RARITY + " = 0",
            null,
            null,
            null,
            CardsContract.CardsTable.COLUMN_TIME + " DESC"
        )
        try {
            if (cursor.moveToFirst()) {
                val imageColIndex: Int =
                    cursor.getColumnIndex(CardsContract.CardsTable.COLUMN_IMAGE)
                val rarityColIndex: Int =
                    cursor.getColumnIndex(CardsContract.CardsTable.COLUMN_RARITY)
                val titleColIndex: Int =
                    cursor.getColumnIndex(CardsContract.CardsTable.COLUMN_TITLE)
                var mTitle: String
                var image: Int
                var rarity: Int
                do {
                    mTitle = cursor.getString(titleColIndex)
                    image = cursor.getInt(imageColIndex)
                    rarity = cursor.getInt(rarityColIndex)
                    cardsList.add(Card(image, rarity, mTitle))
                } while (cursor.moveToNext())
            } else cursor.close()
        } finally {
            cursor.close()
        }
        val cursor1: Cursor = db.query(
            CardsContract.CardsTable.TABLE_NAME,
            projection,
            CardsContract.CardsTable.COLUMN_RARITY + " = 1",
            null,
            null,
            null,
            CardsContract.CardsTable.COLUMN_TIME + " DESC"
        )
        try {
            if (cursor1.moveToFirst()) {
                val imageColIndex: Int =
                    cursor1.getColumnIndex(CardsContract.CardsTable.COLUMN_IMAGE)
                val rarityColIndex: Int =
                    cursor1.getColumnIndex(CardsContract.CardsTable.COLUMN_RARITY)
                val titleColIndex: Int =
                    cursor1.getColumnIndex(CardsContract.CardsTable.COLUMN_TITLE)
                var mTitle: String
                var image: Int
                var rarity: Int
                do {
                    mTitle = cursor1.getString(titleColIndex)
                    image = cursor1.getInt(imageColIndex)
                    rarity = cursor1.getInt(rarityColIndex)
                    rareCardsList.add(Card(image, rarity, mTitle))
                } while (cursor1.moveToNext())
            } else cursor1.close()
        } finally {
            cursor1.close()
        }
        val cursor2: Cursor = db.query(
            CardsContract.CardsTable.TABLE_NAME,
            projection,
            CardsContract.CardsTable.COLUMN_RARITY + " = 2",
            null,
            null,
            null,
            CardsContract.CardsTable.COLUMN_TIME + " DESC"
        )
        try {
            if (cursor2.moveToFirst()) {
                val imageColIndex: Int =
                    cursor2.getColumnIndex(CardsContract.CardsTable.COLUMN_IMAGE)
                val rarityColIndex: Int =
                    cursor2.getColumnIndex(CardsContract.CardsTable.COLUMN_RARITY)
                val titleColIndex: Int =
                    cursor2.getColumnIndex(CardsContract.CardsTable.COLUMN_TITLE)
                var mTitle: String
                var image: Int
                var rarity: Int
                do {
                    mTitle = cursor2.getString(titleColIndex)
                    image = cursor2.getInt(imageColIndex)
                    rarity = cursor2.getInt(rarityColIndex)
                    superRareCardsList.add(Card(image, rarity, mTitle))
                } while (cursor2.moveToNext())
            } else cursor2.close()
        } finally {
            cursor2.close()
        }
    }
}