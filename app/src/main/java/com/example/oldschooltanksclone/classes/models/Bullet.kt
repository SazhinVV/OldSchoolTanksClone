package com.example.oldschooltanksclone.classes.models

import android.view.View
import com.example.oldschooltanksclone.classes.enums.Direction

data class Bullet (
    val view: View,
    val direction: Direction,
    val tank: Tank,
    var canBulletMoveFurther: Boolean = true
)