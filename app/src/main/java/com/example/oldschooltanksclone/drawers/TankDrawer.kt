package com.example.oldschooltanksclone.drawers

import android.view.View
import android.widget.FrameLayout
import com.example.oldschooltanksclone.CELL_SIZE
import com.example.oldschooltanksclone.classes.enums.Direction
import com.example.oldschooltanksclone.classes.models.Coordinate
import com.example.oldschooltanksclone.classes.models.Element
import com.example.oldschooltanksclone.utils.checkViewCanMoveThroughBorder
import com.example.oldschooltanksclone.utils.getElementByCoordinate

class TankDrawer (val container: FrameLayout) {
    var currentDirection = Direction.UP

    fun move(my_tank: View, direction: Direction, elementsOnContainer: List<Element>) {
        val layoutParams = my_tank.layoutParams as FrameLayout.LayoutParams
        val currentCoordinate = Coordinate(layoutParams.topMargin, layoutParams.leftMargin)
        currentDirection = direction
        my_tank.rotation = direction.rotation
        when (direction) {
            Direction.UP -> {
                (my_tank.layoutParams as FrameLayout.LayoutParams).topMargin -= CELL_SIZE
            }
            Direction.DOWN -> {
                (my_tank.layoutParams as FrameLayout.LayoutParams).topMargin += CELL_SIZE
            }
            Direction.LEFT -> {
                (my_tank.layoutParams as FrameLayout.LayoutParams).leftMargin -= CELL_SIZE
            }
            Direction.RIGHT -> {
                (my_tank.layoutParams as FrameLayout.LayoutParams).leftMargin += CELL_SIZE
            }
        }
        val nextCoordinate = Coordinate(layoutParams.topMargin, layoutParams.leftMargin)
        if (my_tank.checkViewCanMoveThroughBorder(nextCoordinate)
            && checkTankCanMoveThroughMaterial(nextCoordinate, elementsOnContainer)
        ) {
            container.removeView(my_tank)
            container.addView(my_tank, 0)
        } else {
            (my_tank.layoutParams as FrameLayout.LayoutParams).topMargin = currentCoordinate.top
            (my_tank.layoutParams as FrameLayout.LayoutParams).leftMargin = currentCoordinate.left
        }

    }

    private fun getTankCoordinates(topLeftCoordinate: Coordinate): List<Coordinate> {
        val coordinateList = mutableListOf<Coordinate>()
        coordinateList.add(topLeftCoordinate)
        coordinateList.add(Coordinate(topLeftCoordinate.top + CELL_SIZE, topLeftCoordinate.left))
        coordinateList.add(Coordinate(topLeftCoordinate.top, topLeftCoordinate.left + CELL_SIZE))
        coordinateList.add(
            Coordinate(
                topLeftCoordinate.top + CELL_SIZE,
                topLeftCoordinate.left + CELL_SIZE
            )
        )
        return coordinateList
    }

    private fun checkTankCanMoveThroughMaterial(
        coordinate: Coordinate,
        elementsOnContainer: List<Element>
    ): Boolean {
        getTankCoordinates(coordinate).forEach {
            val element = getElementByCoordinate(it, elementsOnContainer)
            if (element != null
                && !element.material.tankCanGoThrough
            ) {
                return false
            }
        }
        return true
    }
}