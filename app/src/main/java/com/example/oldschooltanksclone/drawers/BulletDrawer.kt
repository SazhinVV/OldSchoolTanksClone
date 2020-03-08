package com.example.oldschooltanksclone.drawers

import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.oldschooltanksclone.CELL_SIZE
import com.example.oldschooltanksclone.R
import com.example.oldschooltanksclone.classes.enums.Direction
import com.example.oldschooltanksclone.classes.models.Coordinate
import com.example.oldschooltanksclone.classes.models.Element
import com.example.oldschooltanksclone.utils.checkViewCanMoveThroughBorder
import com.example.oldschooltanksclone.utils.getElementByCoordinate

private const val BULLET_WIDTH = 15
private const val BULLET_HEIGHT = 25

class BulletDrawer (val container: FrameLayout){

    private var canBulletGoGurther = true
    private var bulletThread: Thread? = null

    private fun checkBulletThreadAlive() = bulletThread != null && bulletThread!!.isAlive

    fun makeBulletMove(my_tank: View, currentDirection: Direction, elementsOnContainer: MutableList<Element>){
        canBulletGoGurther = true
        if (!checkBulletThreadAlive()) {
            bulletThread = Thread(Runnable {
                val bullet = createBullet(my_tank, currentDirection)
                while (bullet.checkViewCanMoveThroughBorder(Coordinate(bullet.top, bullet.left)) && canBulletGoGurther) {
                    when (currentDirection) {
                        Direction.UP -> {
                            (bullet.layoutParams as FrameLayout.LayoutParams).topMargin -= BULLET_HEIGHT
                        }
                        Direction.DOWN -> {
                            (bullet.layoutParams as FrameLayout.LayoutParams).topMargin += BULLET_HEIGHT
                        }
                        Direction.LEFT -> {
                            (bullet.layoutParams as FrameLayout.LayoutParams).leftMargin -= BULLET_HEIGHT
                        }
                        Direction.RIGHT -> {
                            (bullet.layoutParams as FrameLayout.LayoutParams).leftMargin += BULLET_HEIGHT
                        }
                    }
                    Thread.sleep(50)
                    chooseBehaviorInTermsOfDirections(
                        elementsOnContainer,
                        currentDirection,
                        Coordinate(
                            (bullet.layoutParams as FrameLayout.LayoutParams).topMargin,
                            (bullet.layoutParams as FrameLayout.LayoutParams).leftMargin))
                    (container.context as Activity).runOnUiThread {
                        container.removeView(bullet)
                        container.addView(bullet)
                    }
                }
                (container.context as Activity).runOnUiThread {
                    container.removeView(bullet)
                }
            })
            bulletThread!!.start()
        }
    }

    private fun createBullet(my_tank: View, currentDirection: Direction): ImageView{
        return ImageView(container.context)
            .apply {
                this.setImageResource(R.drawable.bullet)
                this.layoutParams = FrameLayout.LayoutParams(BULLET_WIDTH, BULLET_HEIGHT)
                val bulletCoordinate = getBulletCoordinates(this, my_tank, currentDirection)
                (this.layoutParams as FrameLayout.LayoutParams).topMargin = bulletCoordinate.top
                (this.layoutParams as FrameLayout.LayoutParams).leftMargin = bulletCoordinate.left
                this.rotation = currentDirection.rotation
            }
    }

    private fun getBulletCoordinates(
    bullet: ImageView,
    my_tank: View,
    currentDirection: Direction
    ):Coordinate{
        val tankLeftTopCoordinate = Coordinate(my_tank.top, my_tank.left)
        return when(currentDirection){
            Direction.UP ->
                Coordinate(
                    top = tankLeftTopCoordinate.top - bullet.layoutParams.height,
                    left = getDistanceToMiddleOfTank(tankLeftTopCoordinate.left, bullet.layoutParams.width))
            Direction.DOWN ->
                Coordinate(
                    top = tankLeftTopCoordinate.top + my_tank.layoutParams.height,
                    left = getDistanceToMiddleOfTank(tankLeftTopCoordinate.left, bullet.layoutParams.width))
            Direction.LEFT ->
                Coordinate(
                    top = getDistanceToMiddleOfTank(tankLeftTopCoordinate.top, bullet.layoutParams.height),
                    left = tankLeftTopCoordinate.left - bullet.layoutParams.width)
            Direction.RIGHT ->
                Coordinate(
                    top = getDistanceToMiddleOfTank(tankLeftTopCoordinate.top, bullet.layoutParams.height),
                    left = tankLeftTopCoordinate.left + my_tank.layoutParams.width)
            }
        }

    private fun getDistanceToMiddleOfTank(startCoordinate: Int, bulletSize: Int): Int{
        return startCoordinate + (CELL_SIZE - bulletSize / 2)
    }

    private fun getCoordinatesForTopOrBottomDirection(bulletCoordinate: Coordinate): List<Coordinate>{
        val leftCell = bulletCoordinate.left - bulletCoordinate.left % CELL_SIZE
        val rightCell = leftCell + CELL_SIZE
        val topCoordinate = bulletCoordinate.top - bulletCoordinate.top % CELL_SIZE
        return listOf(
            Coordinate (topCoordinate, leftCell),
            Coordinate (topCoordinate, rightCell)
        )
    }

    private fun getCoordinatesForLeftOrRightDirection(bulletCoordinate: Coordinate): List<Coordinate>{
        val topCell = bulletCoordinate.top - bulletCoordinate.top % CELL_SIZE
        val bottomCell = topCell + CELL_SIZE
        val leftCoordinate = bulletCoordinate.left - bulletCoordinate.left % CELL_SIZE
        return listOf(
            Coordinate (topCell, leftCoordinate),
            Coordinate (bottomCell, leftCoordinate)
        )
    }

    private fun removeElementAndStopBullet(element: Element?, elementsOnContainer: MutableList<Element>){
        if (element != null){
            if (element.material.bulletCanGoThrough){
                return
            }
            if (element.material.simpleBulletCanDestroy){
                stopBullet()
                removeView(element)
                elementsOnContainer.remove(element)
            }else{
                stopBullet()
            }
        }
    }

    private fun stopBullet(){
        canBulletGoGurther = false
    }

    private fun removeView(element: Element) {
        val activity = container.context as Activity
        activity.runOnUiThread {
            container.removeView(activity.findViewById(element.viewId))
        }

    }

    private fun compareCollections(
        elementsOnContainer: MutableList<Element>,
        detectedCoordinateList: List<Coordinate>){
            detectedCoordinateList.forEach {
                val element = getElementByCoordinate(it, elementsOnContainer)
                removeElementAndStopBullet(element, elementsOnContainer)
        }
    }

    private fun chooseBehaviorInTermsOfDirections(
        elementsOnContainer: MutableList<Element>,
        currentDirection: Direction,
        bulletCoordinate: Coordinate){
        when (currentDirection){
            Direction.DOWN, Direction.UP -> {
                compareCollections(elementsOnContainer, getCoordinatesForTopOrBottomDirection(bulletCoordinate))
            }
            Direction.LEFT, Direction.RIGHT -> {
                compareCollections(elementsOnContainer, getCoordinatesForLeftOrRightDirection(bulletCoordinate))
            }
        }
    }






}

