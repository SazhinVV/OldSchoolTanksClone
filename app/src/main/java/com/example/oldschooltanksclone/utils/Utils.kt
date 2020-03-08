package com.example.oldschooltanksclone.utils

import android.view.View
import com.example.oldschooltanksclone.CELL_SIZE
import com.example.oldschooltanksclone.HORIZONTAL_MAX_SIZE
import com.example.oldschooltanksclone.VERTICAL_MAX_SIZE
import com.example.oldschooltanksclone.classes.models.Coordinate
import com.example.oldschooltanksclone.classes.models.Element

fun View.checkViewCanMoveThroughBorder(coordinate: Coordinate):Boolean{
    if (coordinate.top >= 0
        && coordinate.top + this.height <= HORIZONTAL_MAX_SIZE
        && coordinate.left >=0
        && coordinate.left + this.width <= VERTICAL_MAX_SIZE ){
        return true
    }
    return false
}

fun getElementByCoordinate(coordinate: Coordinate, elementsOnContainer: List<Element>): Element? {
    for (element in elementsOnContainer){
        for (height in 0 until element.height){
            for (width in 0 until element.width){
                val searchingCoordinate = Coordinate(
                    top = element.coordinate.top + height * CELL_SIZE,
                    left = element.coordinate.left + width * CELL_SIZE
                )
                if (coordinate == searchingCoordinate) {
                    return  element
                }
            }
        }
    }
    return null
}

