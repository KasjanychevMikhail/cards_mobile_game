package com.kasmichael.cardskotlin

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView


class FlipCardAnimation(ctx: Context, img: ImageView, card: Card) :
    Animation.AnimationListener {
    private val animation1: Animation
    private val animation2: Animation
    private var isBackOfCardShowing = true
    private val img: ImageView
    private val ctx: Context
    private val flipped: FlipEnd
    var card: Card

    interface FlipEnd {
        fun flipEnd(img: ImageView?, card: Card?)
    }

    override fun onAnimationStart(animation: Animation) {}
    override fun onAnimationEnd(animation: Animation) {
        if (animation === animation1) {
            if (isBackOfCardShowing) {
                img.setImageResource(card.image)
                flipped.flipEnd(img, card)
            } else {
                img.setImageResource(R.drawable.card_shirt)
            }
            img.clearAnimation()
            img.setAnimation(animation2)
            img.startAnimation(animation2)
        } else {
            isBackOfCardShowing = !isBackOfCardShowing
        }
    }

    override fun onAnimationRepeat(animation: Animation) {}

    init {
        this.img = img
        this.ctx = ctx
        this.card = card
        flipped = ctx as FlipEnd
        animation1 = AnimationUtils.loadAnimation(ctx, R.anim.card_flip_to_middle)
        animation1.setAnimationListener(this)
        animation2 = AnimationUtils.loadAnimation(ctx, R.anim.card_flip_from_middle)
        animation2.setAnimationListener(this)
        img.clearAnimation()
        img.setAnimation(animation1)
        img.startAnimation(animation1)
    }
}