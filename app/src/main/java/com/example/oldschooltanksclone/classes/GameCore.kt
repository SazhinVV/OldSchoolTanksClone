package com.example.oldschooltanksclone.classes

object GameCore {
    @Volatile
    private var isPlay = false

    fun startOrPauseTheGame(){
        isPlay = !isPlay
    }

    fun isPlaying() = isPlay

    fun pauseTheGame (){
        isPlay = false
    }

}