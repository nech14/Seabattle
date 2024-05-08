package com.example.seabattle

import android.graphics.Color
import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView

class GameActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gridLayout = findViewById(R.id.grid)

        createGridWithShips()
    }

    private fun createGridWithShips() {
        val gridCells = Array(10) { IntArray(10) }

        generateShips(gridCells)

        // Populate grid layout
        for (i in 0 until 10) {
            for (j in 0 until 10) {
                val textView = TextView(this)
                textView.layoutParams = GridLayout.LayoutParams().apply {
                    width = GridLayout.LayoutParams.WRAP_CONTENT
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                }
                textView.setTextColor(Color.WHITE)
                textView.textSize = 24f
                textView.text = gridCells[i][j].toString()
                textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                gridLayout.addView(textView)
            }
        }
    }

    private fun generateShips(gridCells: Array<IntArray>) {
        val fleet = mutableListOf(
            Ship("Линкор", 4),
            Ship("Эсминец", 3),
            Ship("Эсминец", 3),
            Ship("Крейсер", 2),
            Ship("Крейсер", 2),
            Ship("Крейсер", 2),
            Ship("Подводная лодка", 1),
            Ship("Подводная лодка", 1),
            Ship("Подводная лодка", 1),
            Ship("Подводная лодка", 1)
        )

        while (fleet.isNotEmpty()) {
            val ship = fleet.removeAt(0)
            val orientation = if (Math.random() < 0.5) Orientation.HORIZONTAL else Orientation.VERTICAL
            val startRow = (0..9).random()
            val startColumn = (0..9).random()

            if (isValidPosition(gridCells, startRow, startColumn, orientation, ship.size)) {
                placeShipOnGrid(gridCells, startRow, startColumn, orientation, ship)
            } else {
                fleet.add(ship) // Try again with this ship
            }
        }
    }

    private fun isValidPosition(
        gridCells: Array<IntArray>,
        startRow: Int,
        startColumn: Int,
        orientation: Orientation,
        size: Int
    ): Boolean {
        if (orientation == Orientation.HORIZONTAL) {
            if (startColumn + size > 10) return false
            for (j in startColumn until startColumn + size) {
                if (gridCells[startRow][j] != 0 || hasAdjacentShip(gridCells, startRow, j)) return false
            }
        } else {
            if (startRow + size > 10) return false
            for (i in startRow until startRow + size) {
                if (gridCells[i][startColumn] != 0 || hasAdjacentShip(gridCells, i, startColumn)) return false
            }
        }
        return true
    }

    private fun hasAdjacentShip(gridCells: Array<IntArray>, row: Int, column: Int): Boolean {
        for (i in (row - 1)..(row + 1)) {
            for (j in (column - 1)..(column + 1)) {
                if (i in 0 until 10 && j in 0 until 10 && gridCells[i][j] != 0) return true
            }
        }
        return false
    }

    private fun placeShipOnGrid(
        gridCells: Array<IntArray>,
        startRow: Int,
        startColumn: Int,
        orientation: Orientation,
        ship: Ship
    ) {
        if (orientation == Orientation.HORIZONTAL) {
            for (j in startColumn until startColumn + ship.size) {
                gridCells[startRow][j] = 1
            }
        } else {
            for (i in startRow until startRow + ship.size) {
                gridCells[i][startColumn] = 1
            }
        }
    }

    data class Ship(val type: String, val size: Int)

    enum class Orientation {
        HORIZONTAL, VERTICAL
    }

}