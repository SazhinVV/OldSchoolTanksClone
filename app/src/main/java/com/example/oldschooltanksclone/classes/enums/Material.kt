package com.example.oldschooltanksclone.classes.enums

import com.example.oldschooltanksclone.R

const val CELLS_SIMPLE_ELEMENT = 1
const val CELLS_EAGLE_WIDTH = 4
const val CELLS_EAGLE_HEIGHT = 3
const val TANKS_WIDTH_HEIGHT = 2


enum class Material(
    val tankCanGoThrough: Boolean,
    val bulletCanGoThrough: Boolean,
    val simpleBulletCanDestroy: Boolean,
    val elementsAmountOnScreen: Int,
    val width: Int,
    val height: Int,
    val image: Int) {
    EMPTY(
        true,
        true,
        true,
        0,
        0,
        0,
        0),
    BRICK(
        false,
        false,
        true,
        0,
        CELLS_SIMPLE_ELEMENT,
        CELLS_SIMPLE_ELEMENT,
        R.drawable.brick),
    CONCRETE(
        false,
        false,
        false,
        0,
        CELLS_SIMPLE_ELEMENT,
        CELLS_SIMPLE_ELEMENT,
        R.drawable.concrete),
    GRASS(
        true,
        true,
        false,
        0,
        CELLS_SIMPLE_ELEMENT,
        CELLS_SIMPLE_ELEMENT,
        R.drawable.grass),
    EAGLE(
        false,
        false,
        true,
        1,
        CELLS_EAGLE_WIDTH,
        CELLS_EAGLE_HEIGHT,
        R.drawable.eagle),
    ENEMY_TANK(
        false,
        false,
        true,
        0,
        TANKS_WIDTH_HEIGHT,
        TANKS_WIDTH_HEIGHT,
        R.drawable.enemy_tank),
}