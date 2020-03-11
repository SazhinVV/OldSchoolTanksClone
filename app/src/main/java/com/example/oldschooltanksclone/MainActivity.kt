package com.example.oldschooltanksclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.oldschooltanksclone.classes.GameCore
import com.example.oldschooltanksclone.classes.GameCore.isPlaying
import com.example.oldschooltanksclone.classes.GameCore.startOrPauseTheGame
import com.example.oldschooltanksclone.classes.LevelStorage
import com.example.oldschooltanksclone.classes.SoundManager
import com.example.oldschooltanksclone.classes.enums.Direction
import com.example.oldschooltanksclone.classes.enums.Direction.*
import com.example.oldschooltanksclone.classes.enums.Material
import com.example.oldschooltanksclone.classes.models.Coordinate
import com.example.oldschooltanksclone.classes.models.Element
import com.example.oldschooltanksclone.classes.models.Tank
import com.example.oldschooltanksclone.drawers.*
import kotlinx.android.synthetic.main.activity_main.*

const val CELL_SIZE = 50
const val VERTICAL_CELL_AMOUNT = 26
const val HORIZONTAL_CELL_AMOUNT = 34
const val VERTICAL_MAX_SIZE = CELL_SIZE * VERTICAL_CELL_AMOUNT
const val HORIZONTAL_MAX_SIZE = CELL_SIZE * HORIZONTAL_CELL_AMOUNT
const val HALF_WIDTH_OF_CONTAINER = VERTICAL_MAX_SIZE / 2

class MainActivity : AppCompatActivity() {
    private var editMode = false
    private lateinit var item: MenuItem
    private val playerTank by lazy {
        Tank(
            Element(
                material = Material.PLAYER_TANK,
                coordinate = getPlayerTankCoordinate()
            ), UP, enemyDrawer
        )
    }

    private val bulletDrawer by lazy {
        BulletDrawer(
            container,
            elementsDrawer.elementsOnContainer,
            enemyDrawer
        )
    }

    private fun getPlayerTankCoordinate() = Coordinate(
        top = HORIZONTAL_MAX_SIZE - Material.PLAYER_TANK.height * CELL_SIZE,
        left = HALF_WIDTH_OF_CONTAINER - 8 * CELL_SIZE
    )

    private val eagle = Element(
        material = Material.EAGLE,
        coordinate = getEagleCoordinate()
    )


    private fun getEagleCoordinate() = Coordinate(
        top = HORIZONTAL_MAX_SIZE - Material.EAGLE.height * CELL_SIZE,
        left = HALF_WIDTH_OF_CONTAINER - Material.EAGLE.width * CELL_SIZE / 2
    )

    private val gridDrawer by lazy {
        GridDrawer(container)
    }

    private val elementsDrawer by lazy {
        ElementsDrawer(container)
    }

    private val levelStorage by lazy {
        LevelStorage(this)
    }

    private val enemyDrawer by lazy {
        EnemyDrawer(container, elementsDrawer.elementsOnContainer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SoundManager.context = this
        enemyDrawer.bulletDrawer = bulletDrawer
        container.layoutParams = FrameLayout.LayoutParams(VERTICAL_MAX_SIZE, HORIZONTAL_MAX_SIZE)
        editor_clear.setOnClickListener { elementsDrawer.currentMaterial = Material.EMPTY }
        editor_brick.setOnClickListener { elementsDrawer.currentMaterial = Material.BRICK }
        editor_concrete.setOnClickListener { elementsDrawer.currentMaterial = Material.CONCRETE }
        editor_grass.setOnClickListener { elementsDrawer.currentMaterial = Material.GRASS }
        container.setOnTouchListener { _, event ->
            if (!editMode) {
                return@setOnTouchListener true
            }
            elementsDrawer.onTouchContainer(event.x, event.y)
            return@setOnTouchListener true
        }
        elementsDrawer.drawElementsList(levelStorage.loadLevel())
        elementsDrawer.drawElementsList(listOf(playerTank.element, eagle))
        hideSettings()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        item = menu.findItem(R.id.menu_play_game)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                switchEditMode()
                true
            }
            R.id.menu_save -> {
                levelStorage.saveLevel(elementsDrawer.elementsOnContainer)
                true
            }
            R.id.menu_play_game -> {
                if (editMode) {
                    return true
                }
                startOrPauseTheGame()
                if (isPlaying()) {
                    startTheGame()
                } else {
                    pauseTheGame()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startTheGame() {
        item.icon = ContextCompat.getDrawable(this, R.drawable.ic_pause)
        enemyDrawer.startEnemyCreation()
        SoundManager.playIntroMusic()
    }

    private fun pauseTheGame() {
        item.icon = ContextCompat.getDrawable(this, R.drawable.ic_play)
        GameCore.pauseTheGame()
        SoundManager.pauseSounds()
    }

    override fun onPause() {
        super.onPause()
        pauseTheGame()
    }

    private fun switchEditMode() {
        editMode = !editMode
        if (editMode) {
            showSettings()
        } else {
            hideSettings()
        }
    }

    private fun showSettings() {
        gridDrawer.drawGrid()
        materials_container.visibility = VISIBLE
    }

    private fun hideSettings() {
        gridDrawer.removeGrid()
        materials_container.visibility = GONE
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (!isPlaying()) {
            return super.onKeyDown(keyCode, event)
        }
        when (keyCode) {
            KEYCODE_DPAD_UP -> onButtonPressed(UP)
            KEYCODE_DPAD_LEFT -> onButtonPressed(LEFT)
            KEYCODE_DPAD_DOWN -> onButtonPressed(DOWN)
            KEYCODE_DPAD_RIGHT -> onButtonPressed(RIGHT)
            KEYCODE_SPACE -> bulletDrawer.addNewBulletForTank(playerTank)
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun onButtonPressed(direction: Direction) {
        SoundManager.tankMove()
        playerTank.move(direction, container, elementsDrawer.elementsOnContainer)
    }


    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KEYCODE_DPAD_UP, KEYCODE_DPAD_LEFT, KEYCODE_DPAD_DOWN, KEYCODE_DPAD_RIGHT -> onButtonReleased()
        }
        return super.onKeyUp(keyCode, event)
    }

    private fun onButtonReleased() {
        if (enemyDrawer.tanks.isEmpty()) {
            SoundManager.tankStop()
        }
    }
}