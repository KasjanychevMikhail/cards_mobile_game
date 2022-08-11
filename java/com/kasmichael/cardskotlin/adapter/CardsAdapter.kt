package com.kasmichael.cardskotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kasmichael.cardskotlin.Card
import com.kasmichael.cardskotlin.R

class CardsAdapter(mList: ArrayList<Card>, onClickListener: OnCardClickListener) :
    RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {
    private val list: ArrayList<Card>
    private val onClickListener: OnCardClickListener

    interface OnCardClickListener {
        fun onCardClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val context = parent.context
        val layoutIdForCardItem: Int = R.layout.item_card
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutIdForCardItem, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(list[position].image, list[position].title)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CardViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var text: TextView
        fun bind(num: Int, inputText: String?) {
            image.setImageResource(num)
            text.text = inputText
        }

        init {
            image = itemView.findViewById(R.id.cardItemImageView)
            text = itemView.findViewById(R.id.nameCardItemTextView)
            itemView.setOnClickListener { onClickListener.onCardClick(adapterPosition) }
        }
    }

    init {
        list = mList
        this.onClickListener = onClickListener
    }
}