package com.example.oldschooltanksclone.drawers

import android.widget.FrameLayout
import com.example.oldschooltanksclone.CELL_SIZE
import com.example.oldschooltanksclone.HALF_WIDTH_OF_CONTAINER
import com.example.oldschooltanksclone.VERTICAL_MAX_SIZE
import com.example.oldschooltanksclone.classes.GameCore.isPlaying
import com.example.oldschooltanksclone.classes.SoundManager
import com.example.oldschooltanksclone.classes.enums.Direction
import com.example.oldschooltanksclone.classes.enums.Material
import com.example.oldschooltanksclone.classes.models.Coordinate
import com.example.oldschooltanksclone.classes.models.Element
import com.example.oldschooltanksclone.classes.models.Tank
import com.example.oldschooltanksclone.utils.checkIfChanceBiggerThanRandom
import com.example.oldschooltanksclone.utils.drawElement

private const val MAX_ENEMY_AMOUNT = 20

class EnemyDrawer(
    private val container: FrameLayout,
    private val elements: MutableList<Element>
) {
    private val respawnList: List<Coordinate>
    private var enemyAmount = 0
    private var currentCoordinate: Coordinate
    val tanks = mutableListOf<Tank>()
    lateinit var bulletDrawer: BulletDrawer
    private var gameStarted = false

    init {
        respawnList = getRespawnList()
        currentCoordinate = respawnList[0]
    }

    private fun getRespawnList(): List<Coordinate> {
        val respawnList = mutableListOf<Coordinate>()
        respawnList.add(Coordinate(0, 0))
        respawnList.add(Coordinate(0, HALF_WIDTH_OF_CONTAINER - CELL_SIZE))
        respawnList.add(Coordinate(0, VERTICAL_MAX_SIZE - 2 * CELL_SIZE))
        return respawnList
    }

    fun startEnemyCreation() {
        if (gameStarted) {
            return
        }
        gameStarted = true
        Thread(Runnable {
            while (enemyAmount < MAX_ENEMY_AMOUNT) {
                if (!isPlaying()) {
                    continue
                }
                drawEnemy()
                enemyAmount++
                Thread.sleep(3000)
            }
        }).start()
        moveEnemyTanks()
    }

    private fun drawEnemy() {
        var index = respawnList.indexOf(currentCoordinate) + 1
        if (index == respawnList.size) {
            index = 0
        }
        currentCoordinate = respawnList[index]
        val enemyTank = Tank(
            Element(
                material = Material.ENEMY_TANK,
                coordinate = currentCoordinate
            ), Direction.DOWN, this
        )
        enemyTank.element.drawElement(container)
        tanks.add(enemyTank)
    }

    private fun moveEnemyTanks() {
        Thread(Runnable {
            while (true) {
                if (!isPlaying()) {
                    continue
                }
                goThroughAllTanks()
                Thread.sleep(400)
            }
        }).start()
    }

    private fun goThroughAllTanks() {
        if (tanks.isNotEmpty()) {
            SoundManager.tankMove()
        } else {
            SoundManager.tankStop()
        }
        tanks.toList().forEach {
            it.move(it.direction, container, elements)
            if (checkIfChanceBiggerThanRandom(10)) {
                bulletDrawer.addNewBulletForTank(it)
            }
        }
    }

    fun removeTank(tankIndex: Int) {
        tanks.removeAt(tankIndex)
    }
}