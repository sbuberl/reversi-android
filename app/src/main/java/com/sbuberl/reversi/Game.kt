package com.sbuberl.reversi

class Game {
    private val board = Board()
    private var isUserTurn: Boolean = false
    private val view: GameView

    constructor(view: GameView) {
        this.view = view
    }

    fun start() {
        val isUserBlack = Math.random() >= 0.5
        val user: MoveEvaluator = { row: Int, column: Int -> this.userEvaluator(row, column)}
        val ai: MoveEvaluator = { row: Int, column: Int -> this.aiEvaluator(row, column)}

        if(isUserBlack) {
            board.buildMoveList(user, ai)
        } else {
            board.buildMoveList(ai, user)
        }

        isUserTurn = isUserBlack
    }

    private fun userEvaluator(row: Int, column: Int): Int {
        return 1
    }

    private fun aiEvaluator(row: Int, column: Int): Int {
        return 1
    }


}