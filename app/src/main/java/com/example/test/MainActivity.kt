package com.example.test

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private var isPlayerX = true
    private var gameBoard = Array(3) { IntArray(3) }
    private var gameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resetButton: Button = findViewById(R.id.resetBtn)
        resetButton.setOnClickListener { resetGame() }

        // Thiết lập sự kiện cho từng ô trên bàn cờ
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                val button = getButton(i, j)
                button.setOnClickListener { onCellClicked(i, j, button) }
            }
        }
    }

    private fun onCellClicked(row: Int, col: Int, button: Button) {
        if (!gameOver && gameBoard[row][col] == 0) {
            if (isPlayerX) {
                button.text = "X"
                button.setTextColor(resources.getColor(R.color.red))
                gameBoard[row][col] = 1
            } else {
                button.text = "O"
                button.setTextColor(resources.getColor(R.color.green))
                gameBoard[row][col] = 2
            }

            if (checkForWinner()) {
                gameOver = true
                showGameOverDialog("Player ${if (isPlayerX) "X" else "O"} Wins! Do you want to play again?")
            } else if (isBoardFull()) {
                gameOver = true
                showGameOverDialog("It's a draw! Do you want to play again?")
            } else {
                isPlayerX = !isPlayerX
            }
        }
    }

    private var winnerDeclared = false

    private fun checkForWinner(): Boolean {
        for (i in 0 until 3) {
            if (gameBoard[i][0] != 0 && gameBoard[i][0] == gameBoard[i][1] && gameBoard[i][0] == gameBoard[i][2]) {
                if (!winnerDeclared) {
                    winnerDeclared = true
                    highlightWinnerCells(i, 0, i, 1, i, 2)
                }
                return true
            }

            if (gameBoard[0][i] != 0 && gameBoard[0][i] == gameBoard[1][i] && gameBoard[0][i] == gameBoard[2][i]) {
                if (!winnerDeclared) {
                    winnerDeclared = true
                    highlightWinnerCells(0, i, 1, i, 2, i)
                }
                return true
            }
        }

        if (gameBoard[0][0] != 0 && gameBoard[0][0] == gameBoard[1][1] && gameBoard[0][0] == gameBoard[2][2]) {
            if (!winnerDeclared) {
                winnerDeclared = true
                highlightWinnerCells(0, 0, 1, 1, 2, 2)
            }
            return true
        }

        if (gameBoard[0][2] != 0 && gameBoard[0][2] == gameBoard[1][1] && gameBoard[0][2] == gameBoard[2][0]) {
            if (!winnerDeclared) {
                winnerDeclared = true
                highlightWinnerCells(0, 2, 1, 1, 2, 0)
            }
            return true
        }

        return false
    }


    private fun isBoardFull(): Boolean {
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (gameBoard[i][j] == 0) {
                    return false
                }
            }
        }
        return true
    }

    private fun showGameOverDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("OK") { _, _ -> resetGame() }
            .setNegativeButton("Exit") { _, _ -> finish() }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun resetGame() {
        isPlayerX = true
        gameOver = false

        for (i in 0 until 3) {
            for (j in 0 until 3) {
                val button = getButton(i, j)
                button.text = ""
                button.setBackgroundColor(resources.getColor(android.R.color.background_light))
            }
        }

        gameBoard = Array(3) { IntArray(3) }




    }

    private fun highlightWinnerCells(row1: Int, col1: Int, row2: Int, col2: Int, row3: Int, col3: Int) {
        val button1 = getButton(row1, col1)
        val button2 = getButton(row2, col2)
        val button3 = getButton(row3, col3)

        val yellowColor = ContextCompat.getColor(this, R.color.yellow)

        // Thiết lập màu nền của các ô đã thắng là màu vàng
        button1.setBackgroundResource(R.color.yellow)
        button2.setBackgroundResource(R.color.yellow)
        button3.setBackgroundResource(R.color.yellow)
    }



    private fun getButton(row: Int, col: Int): Button {
        val buttonId = resources.getIdentifier("btn${row * 3 + col + 1}", "id", packageName)
        return findViewById(buttonId)
    }
}
