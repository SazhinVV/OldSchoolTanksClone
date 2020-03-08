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
import com.example.oldschooltanksclone.classes.LevelStorage
import com.example.oldschooltanksclone.classes.enums.Direction.*
import com.example.oldschooltanksclone.classes.enums.Material
import com.example.oldschooltanksclone.drawers.BulletDrawer
import com.example.oldschooltanksclone.drawers.ElementsDrawer
import com.example.oldschooltanksclone.drawers.GridDrawer
import com.example.oldschooltanksclone.drawers.TankDrawer
import kotlinx.android.synthetic.main.activity_main.*

const val CELL_SIZE = 50
const val VERTICAL_CELL_AMOUNT = 25
const val HORIZONTAL_CELL_AMOUNT = 33
const val VERTICAL_MAX_SIZE = CELL_SIZE * VERTICAL_CELL_AMOUNT
const val HORIZONTAL_MAX_SIZE = CELL_SIZE * HORIZONTAL_CELL_AMOUNT



class MainActivity : AppCompatActivity() {
    private var editMode = false

    private val gridDrawer by lazy{
        GridDrawer(container)
    }

    private  val elementsDrawer by lazy{
        ElementsDrawer(container)
    }

    private val tankDrawer by lazy {
        TankDrawer(container)
    }

    private val bulletDraw by lazy {
        BulletDrawer(container)
    }

    private val levelStorage by lazy{
        LevelStorage(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        container.layoutParams = FrameLayout.LayoutParams(VERTICAL_MAX_SIZE, HORIZONTAL_MAX_SIZE)
        editor_clear.setOnClickListener{ elementsDrawer.currentMaterial = Material.EMPTY }
        editor_brick.setOnClickListener{ elementsDrawer.currentMaterial = Material.BRICK }
        editor_grass.setOnClickListener{ elementsDrawer.currentMaterial = Material.GRASS }
        editor_concrete.setOnClickListener{ elementsDrawer.currentMaterial = Material.CONCRETE }
        editor_eagle.setOnClickListener { elementsDrawer.currentMaterial = Material.EAGLE }
        editor_enemy_respawn.setOnClickListener { elementsDrawer.currentMaterial = Material.ENEMY_TANK_RESPAWN }
        editor_player_respawn.setOnClickListener { elementsDrawer.currentMaterial = Material.PLAYER_TANK_RESPAWN }
        container.setOnTouchListener { _, event ->
            elementsDrawer.onTouchContainer(event.x, event.y)
            return@setOnTouchListener true
        }
        elementsDrawer.drawElementsList(levelStorage.loadLevel())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menu_settings -> {
                gridDrawer.drawGrid()
                switchEditMode()
                return true
            }
            R.id.menu_save -> {
                levelStorage.saveLevel(elementsDrawer.elementsOnContainer)
                return true
            }else -> super.onOptionsItemSelected(item)
        }
    }

    private fun switchEditMode(){
        if (editMode) {
            gridDrawer.removeGrid()
            materials_container.visibility = GONE
        }else{
            gridDrawer.drawGrid()
            materials_container.visibility = VISIBLE
        }
        editMode = !editMode
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode){
            KEYCODE_DPAD_UP -> tankDrawer.move(my_tank, UP, elementsDrawer.elementsOnContainer)
            KEYCODE_DPAD_DOWN -> tankDrawer.move(my_tank, DOWN, elementsDrawer.elementsOnContainer)
            KEYCODE_DPAD_LEFT -> tankDrawer.move(my_tank, LEFT, elementsDrawer.elementsOnContainer)
            KEYCODE_DPAD_RIGHT -> tankDrawer.move(my_tank, RIGHT, elementsDrawer.elementsOnContainer)
            KEYCODE_SPACE -> bulletDraw.makeBulletMove(my_tank, tankDrawer.currentDirection, elementsDrawer.elementsOnContainer)

        }
        return super.onKeyDown(keyCode, event)
    }


}
