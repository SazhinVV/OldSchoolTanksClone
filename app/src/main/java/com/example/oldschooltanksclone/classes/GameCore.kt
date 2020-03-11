package com.example.oldschooltanksclone.classes

import android.app.Activity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.example.oldschooltanksclone.R
import kotlinx.android.synthetic.main.activity_main.view.*

class GameCore (private val activity: Activity){
    @Volatile
    private var isPlay = false
    private var isPlayerOrBaseDestroyed = false

    fun startOrPauseTheGame(){
        isPlay = !isPlay
    }

    fun isPlaying() = isPlay && !isPlayerOrBaseDestroyed

    fun pauseTheGame (){
        isPlay = false
    }

    fun destroyBaseOrPlayer(){
        isPlayerOrBaseDestroyed = true
        pauseTheGame()
        animateEndGame()
    }

    private fun animateEndGame() {
        activity.runOnUiThread {
            val endGameTextView = activity.findViewById<TextView>(R.id.game_over_tv)
            endGameTextView.visibility = View.VISIBLE
            val slideUp = AnimationUtils.loadAnimation(activity, R.anim.slide_up)
            endGameTextView.startAnimation(slideUp)
        }
    }

}