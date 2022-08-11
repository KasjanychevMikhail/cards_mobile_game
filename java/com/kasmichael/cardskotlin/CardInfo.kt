package com.kasmichael.cardskotlin

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment


class CardInfo(private val card: Card) :
    AppCompatDialogFragment() {
    private var rarity: String? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(card.title)
            .setIcon(card.image)
            .setMessage("Редкость: $rarity.")
            .setPositiveButton(
                "ОК"
            ) { dialog, which -> }
        return builder.create()
    }

    init {
        when (card.rarity) {
            0 -> rarity = "Обычная"
            1 -> rarity = "Редкая"
            2 -> rarity = "Очень редкая"
            else -> rarity = "Отсутствует"
        }
    }
}