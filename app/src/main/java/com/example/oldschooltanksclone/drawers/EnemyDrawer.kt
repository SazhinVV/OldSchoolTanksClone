package com.example.oldschooltanksclone.drawers

import android.widget.FrameLayout
import com.example.oldschooltanksclone.CELL_SIZE
import com.example.oldschooltanksclone.classes.enums.Material
import com.example.oldschooltanksclone.classes.models.Coordinate
import com.example.oldschooltanksclone.classes.models.Element
import com.example.oldschooltanksclone.utils.drawElement

private const val MAX_ENEMY_AMOUNT = 20

class EnemyDrawer (private val container: FrameLayout){
    private val respawnList: List<Coordinate>
    private var enemyAmount = 0
    private var currentCoordinate: Coordinate

    init {
        respawnList = getRespawnList()
        currentCoordinate = respawnList[0]
    }

    private fun getRespawnList(): List<Coordinate> {
        val respawnList = mutableListOf<Coordinate>()
        respawnList.add(Coordinate(0,0))
        respawnList.add(Coordinate(0,container.width / 2 - CELL_SIZE))
        respawnList.add(Coordinate(0,container.width - 2 * CELL_SIZE))
        return respawnList
    }

    fun startEnemyDrawing(elements: MutableList<Element>) {
        Thread(Runnable {
            while (enemyAmount <= MAX_ENEMY_AMOUNT) {
                drawEnemy(elements)
                enemyAmount++
                Thread.sleep(3000)
            } }).start()

    }

    private fun drawEnemy(elements: MutableList<Element>) {
        var index = respawnList.indexOf(currentCoordinate) + 1
        if (index == respawnList.size){
            index = 0
        }
        currentCoordinate = respawnList[index]
        val enemyTankElement = Element(
            material = Material.ENEMY_TANK,
            coordinate = currentCoordinate,
            width = Material.ENEMY_TANK.width,
            height = Material.ENEMY_TANK.height)
        enemyTankElement.drawElement(container)
        elements.add(enemyTankElement)
    }
}