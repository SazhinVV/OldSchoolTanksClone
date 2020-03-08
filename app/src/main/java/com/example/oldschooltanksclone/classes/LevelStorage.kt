package com.example.oldschooltanksclone.classes

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.oldschooltanksclone.classes.models.Element
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val KEY_LEVEL = "KEY_LEVEL"

class LevelStorage(val context: Context){
    private val prefs = (context as Activity).getPreferences(MODE_PRIVATE)

    fun saveLevel(elementsOnContainer: List<Element>){
        prefs.edit()
            .putString(KEY_LEVEL, Gson().toJson(elementsOnContainer))
            .apply()
    }

    fun loadLevel(): List<Element>? {
        val levelFromPrefs = prefs.getString(KEY_LEVEL, null)
        var listOfElements: List<Element>? = null
        levelFromPrefs?.let {
            val type = object : TypeToken<List<Element>>() {}.type
            listOfElements = Gson().fromJson(it, type)

        }
        return listOfElements
    }

}