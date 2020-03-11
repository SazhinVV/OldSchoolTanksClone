package com.example.oldschooltanksclone.classes

import android.app.Activity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.example.oldschooltanksclone.R
import com.example.oldschooltanksclone.activities.SCORE_REQUEST_CODE
import com.example.oldschooltanksclone.activities.ScoreActivity

class GameCore (private val activity: Activity){
    @Volatile
    private var isPlay = false
    private var isPlayerOrBaseDestroyed = false
    private var isPlayerWin = false

    fun startOrPauseTheGame(){
        isPlay = !isPlay
    }

    fun isPlaying() = isPlay && !isPlayerOrBaseDestroyed && !isPlayerWin

    fun pauseTheGame (){
        isPlay = false
    }

    fun playerWon(score: Int){
        isPlayerWin = true
        activity.startActivityForResult(ScoreActivity.createIntent(activity, score), SCORE_REQUEST_CODE)
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