package com.example.oldschooltanksclone.utils

import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.oldschooltanksclone.CELL_SIZE
import com.example.oldschooltanksclone.HORIZONTAL_MAX_SIZE
import com.example.oldschooltanksclone.VERTICAL_MAX_SIZE
import com.example.oldschooltanksclone.classes.models.Coordinate
import com.example.oldschooltanksclone.classes.models.Element
import com.example.oldschooltanksclone.classes.models.Tank
import kotlin.random.Random

const val TOTAL_PERCENT = 100

fun View.checkViewCanMoveThroughBorder(coordinate: Coordinate): Boolean {
    if (coordinate.top >= 0
        && coordinate.top + this.height <= HORIZONTAL_MAX_SIZE
        && coordinate.left >= 0
        && coordinate.left + this.width <= VERTICAL_MAX_SIZE
    ) {
        return true
    }
    return false
}

fun getElementByCoordinates(coordinate: Coordinate, elementsOnContainer: List<Element>): Element? {
    for (element in elementsOnContainer.toList()) {
        for (height in 0 until element.height) {
            for (width in 0 until element.width) {
                val searchingCoordinate = Coordinate(
                    top = element.coordinate.top + height * CELL_SIZE,
                    left = element.coordinate.left + width * CELL_SIZE
                )
                if (coordinate == searchingCoordinate) {
                    return element
                }
            }
        }
    }
    return null
}

fun getTankByCoordinates(coordinate: Coordinate, tanksList: List<Tank>): Element? {
    return getElementByCoordinates(coordinate, tanksList.map { it.element })
}

fun Element.drawElement(container: FrameLayout) {
    val view = ImageView(container.context)
    val layoutParams = FrameLayout.LayoutParams(
        this.material.width * CELL_SIZE,
        this.material.height * CELL_SIZE
    )
    this.material.image.let { view.setImageResource(it) }
    layoutParams.topMargin = this.coordinate.top
    layoutParams.leftMargin = this.coordinate.left

    view.id = this.viewId
    view.layoutParams = layoutParams
    view.scaleType = ImageView.ScaleType.FIT_XY
    container.runOnUiThread {
        container.addView(view)
    }
}

fun FrameLayout.runOnUiThread(block: () -> Unit) {
    (this.context as Activity).runOnUiThread {
        block()
    }
}

fun checkIfChanceBiggerThanRandom(percentChance: Int): Boolean {
    return Random.nextInt(TOTAL_PERCENT) <= percentChance
}

fun View.getViewCoordinate(): Coordinate {
    return Coordinate(
        (this.layoutParams as FrameLayout.LayoutParams).topMargin,
        (this.layoutParams as FrameLayout.LayoutParams).leftMargin
    )
}