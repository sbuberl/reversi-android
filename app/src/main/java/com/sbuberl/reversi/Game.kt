package com.sbuberl.reversi

class Game {
    private val board = Board()
    private var isUserTurn: Boolean = false
    private val view: GameView
    var blackPlayer: Player
        private set
    var whitePlayer: Player
        private set
    var aiPlayer: Player
        private set
    var userPlayer: Player
        private set

    constructor(view: GameView) {
        val isUserBlack = true //Math.random() >= 0.5
        this.blackPlayer = Player(Stone.BLACK, board)
        this.whitePlayer = Player(Stone.WHITE, board)
        if(isUserBlack) {
            this.userPlayer = this.blackPlayer
            this.aiPlayer = this.whitePlayer
        } else {
            this.userPlayer = this.whitePlayer
            this.aiPlayer = this.blackPlayer

        }

        board.buildMoveList()
        isUserTurn = isUserBlack
        this.view = view
        view.setGame(this)
    }

    fun start() {
        addHints()
    }

    fun getStone(row: Int, column: Int) : Stone {
        return board.getStone(row, column)
    }

    fun addHints() {
        var moves = this.userPlayer.moveList()
        for (move in moves) {
            board.setStone(move.row, move.column, Stone.HINT)
        }
    }

    fun removeHints() {
        var moves = this.userPlayer.moveList()
        for (move in moves) {
            board.setStone(move.row, move.column, Stone.NONE)
        }
    }

}