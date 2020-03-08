package com.example.oldschooltanksclone.classes.models

import com.example.oldschooltanksclone.classes.enums.Material

data class Element(
    val viewId: Int,
    val material: Material,
    val coordinate: Coordinate,
    val width: Int,
    val height: Int
)