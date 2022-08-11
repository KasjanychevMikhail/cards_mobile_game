package com.kasmichael.cardskotlin

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kasmichael.cardskotlin.FlipCardAnimation.FlipEnd
import com.kasmichael.cardskotlin.data.CardsContract
import com.kasmichael.cardskotlin.data.CardsDBHelper
import java.util.*
import kotlin.collections.ArrayList


class OpenChestActivity : AppCompatActivity(), View.OnClickListener, FlipEnd {
    private val cardsList: ArrayList<Card> = ArrayList()
    private val allCardsList: ArrayList<Card> = ArrayList()
    private val allRareCardsList: ArrayList<Card> = ArrayList()
    private val allSuperRareCardsList: ArrayList<Card> = ArrayList()
    private var cardsDBHelper: CardsDBHelper? = null
    private var firstImageView: ImageView? = null
    private var secondImageView: ImageView? = null
    private var thirdImageView: ImageView? = null
    private var fourthImageView: ImageView? = null
    private var fifthImageView: ImageView? = null
    private var firstTextView: TextView? = null
    private var secondTextView: TextView? = null
    private var thirdTextView: TextView? = null
    private var fourthTextView: TextView? = null
    private var fifthTextView: TextView? = null
    private var countOpenCards = 0
    private var sizeOfCards = 0
    private var sizeOfRareCards = 0
    private var sizeOfSuperRareCards = 0
    private var getAllCardsBtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_chest)
        getAllCardsBtn = findViewById<View>(R.id.getAllCardsButton) as Button
        getAllCardsBtn!!.setOnClickListener(this)
        firstImageView = findViewById(R.id.firstCardImageView)
        secondImageView = findViewById(R.id.secondCardImageView)
        thirdImageView = findViewById(R.id.thirdCardImageView)
        fourthImageView = findViewById(R.id.fourthCardImageView)
        fifthImageView = findViewById(R.id.fifthCardImageView)
        firstImageView!!.setOnClickListener(this)
        secondImageView!!.setOnClickListener(this)
        thirdImageView!!.setOnClickListener(this)
        fourthImageView!!.setOnClickListener(this)
        fifthImageView!!.setOnClickListener(this)
        firstTextView = findViewById(R.id.firstCardTextView)
        secondTextView = findViewById(R.id.secondCardTextView)
        thirdTextView = findViewById(R.id.thirdCardTextView)
        fourthTextView = findViewById(R.id.fourthCardTextView)
        fifthTextView = findViewById(R.id.fifthCardTextView)
        addCardsToLists()
        sizeOfCards = allCardsList.size
        sizeOfRareCards = allRareCardsList.size
        sizeOfSuperRareCards = allSuperRareCardsList.size
        createCardsList()
        cardsDBHelper = CardsDBHelper(this)
    }

    override fun onClick(view: View) {
        when (view.getId()) {
            R.id.getAllCardsButton -> {
                addCardsIntoDB()
                finish()
            }
            R.id.firstCardImageView -> FlipCardAnimation(
                this@OpenChestActivity, view as ImageView,
                cardsList[0]
            )
            R.id.secondCardImageView -> FlipCardAnimation(
                this@OpenChestActivity, view as ImageView,
                cardsList[1]
            )
            R.id.thirdCardImageView -> FlipCardAnimation(
                this@OpenChestActivity, view as ImageView,
                cardsList[2]
            )
            R.id.fourthCardImageView -> FlipCardAnimation(
                this@OpenChestActivity, view as ImageView,
                cardsList[3]
            )
            R.id.fifthCardImageView -> FlipCardAnimation(
                this@OpenChestActivity, view as ImageView,
                cardsList[4]
            )
            else -> {}
        }
    }

    override fun flipEnd(img: ImageView?, card: Card?) {
        when (img?.id) {
            R.id.firstCardImageView -> firstTextView!!.text = card?.title
            R.id.secondCardImageView -> secondTextView!!.text = card?.title
            R.id.thirdCardImageView -> thirdTextView!!.text = card?.title
            R.id.fourthCardImageView -> fourthTextView!!.text = card?.title
            R.id.fifthCardImageView -> fifthTextView!!.text = card?.title
            else -> {}
        }
        img?.setClickable(false)
        countOpenCards++
        if (countOpenCards == 5) {
            getAllCardsBtn?.setVisibility(View.VISIBLE)
        }
    }

    private fun addCardsIntoDB() {
        val db = cardsDBHelper!!.writableDatabase
        for (i in 0 until cardsList.size) {
            val values = ContentValues()
            val currentDate = Date()
            val time: Long = currentDate.getTime()
            val card = cardsList[i]
            values.put(CardsContract.CardsTable.COLUMN_IMAGE, card.image)
            values.put(CardsContract.CardsTable.COLUMN_RARITY, card.rarity)
            values.put(CardsContract.CardsTable.COLUMN_TITLE, card.title)
            values.put(CardsContract.CardsTable.COLUMN_TIME, time)
            val newRowId = db.insert(CardsContract.CardsTable.TABLE_NAME, null, values)
            if (newRowId == -1L) {
                Toast.makeText(this, "Ошибка при добавлении в базу данных.", Toast.LENGTH_SHORT)
                    .show()
                return
            }
        }
        Toast.makeText(
            this,
            "Добавлено " + cardsList.size.toString() + "карт.",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun createCardsList() {
        for (i in 0..4) {
            val card = generateCard()
            cardsList.add(card)
        }
    }

    private fun generateCard(): Card {
        val res: Card
        val randomValue = (Math.random() * 100).toInt()
        val typeOfCard: Int
        val randomIdx: Int
        if (randomValue < PROBABILITY_OF_ONE_CARD) {
            typeOfCard = 0
            randomIdx = (Math.random() * sizeOfCards).toInt()
        } else if (randomValue < PROBABILITY_OF_ONE_CARD + PROBABILITY_OF_ONE_RARE_CARD) {
            typeOfCard = 1
            randomIdx = (Math.random() * sizeOfRareCards).toInt()
        } else {
            typeOfCard = 2
            randomIdx = (Math.random() * sizeOfSuperRareCards).toInt()
        }
        res = getCardFromSet(typeOfCard, randomIdx)
        return res
    }

    private fun getCardFromSet(type: Int, idx: Int): Card {
        return when (type) {
            0 -> allCardsList[idx]
            1 -> allRareCardsList[idx]
            2 -> allSuperRareCardsList[idx]
            else -> Card(R.drawable.ic_empty, -1, "Ошибка")
        }
    }

    private fun addCardsToLists() {
        allCardsList.add(Card(R.drawable.card_bolla, 0, "Bolla"))
        allCardsList.add(Card(R.drawable.card_bronkhorst, 0, "Bronkhorst"))
        allCardsList.add(Card(R.drawable.card_gaya, 0, "Gaya"))
        allCardsList.add(Card(R.drawable.card_dellova, 0, "Dellova"))
        allCardsList.add(Card(R.drawable.card_ion, 0, "Ion"))
        allCardsList.add(Card(R.drawable.card_hartjes, 0, "Hartjes"))
        allCardsList.add(Card(R.drawable.card_mannsverk, 0, "Mannsverk"))
        allCardsList.add(Card(R.drawable.card_moreno, 0, "Moreno"))
        allCardsList.add(Card(R.drawable.card_stevanovic, 0, "Stevanovic"))
        allCardsList.add(Card(R.drawable.card_viti, 0, "Viti"))
        allRareCardsList.add(Card(R.drawable.rare_card_brobbey, 1, "Brobbey"))
        allRareCardsList.add(Card(R.drawable.rare_card_dennis, 1, "Dennis"))
        allRareCardsList.add(Card(R.drawable.rare_card_richter, 1, "Richter"))
        allRareCardsList.add(Card(R.drawable.rare_card_tankulic, 1, "Tankulic"))
        allRareCardsList.add(Card(R.drawable.rare_card_thomas, 1, "Thomas"))
        allSuperRareCardsList.add(Card(R.drawable.super_rare_card_lewandowski, 2, "Lewandowski"))
        allSuperRareCardsList.add(Card(R.drawable.super_rare_card_mbappe, 2, "Mbappe"))
        allSuperRareCardsList.add(Card(R.drawable.super_rare_card_messi, 2, "Messi"))
    }

    companion object {
        private const val PROBABILITY_OF_ONE_CARD = 70
        private const val PROBABILITY_OF_ONE_RARE_CARD = 20
        private const val PROBABILITY_OF_ONE_SUPER_RARE_CARD = 10
    }
}