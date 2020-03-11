package com.example.oldschooltanksclone.classes

import android.app.Activity
import android.view.View
import android.view.animation.Animation
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

    fun resumeTheGame(){
        isPlay = true
    }

    fun playerWon(score: Int){
        isPlayerWin = true
        activity.startActivityForResult(ScoreActivity.createIntent(activity, score), SCORE_REQUEST_CODE)
    }

    fun destroyBaseOrPlayer(score: Int){
        isPlayerOrBaseDestroyed = true
        pauseTheGame()
        animateEndGame(score)
    }

    private fun animateEndGame(score: Int) {
        activity.runOnUiThread {
            val endGameTextView = activity.findViewById<TextView>(R.id.game_over_tv)
            endGameTextView.visibility = View.VISIBLE
            val slideUp = AnimationUtils.loadAnimation(activity, R.anim.slide_up)
            endGameTextView.startAnimation(slideUp)
            slideUp.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    activity.startActivityForResult(ScoreActivity.createIntent(activity, score), SCORE_REQUEST_CODE)
                }

                override fun onAnimationStart(animation: Animation?) {
                }

            })
        }
    }

}