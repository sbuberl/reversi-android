package com.sbuberl.reversi

class Player {
    private val stone: Stone
    private var points = 2
    private val board: Board
    val moveList : () -> HashSet<EvaluateResult>

    constructor(stone: Stone, board: Board) {
        this.stone = stone
        this.board = board
        if (stone == Stone.BLACK) {
            moveList =  { this.board.blackMoves() }
        } else {
            moveList =  { this.board.whiteMoves() }
        }
    }

    fun play(moveResult: EvaluateResult?) {
        if(moveResult == null) {
            return
        }
        val move = moveResult!!
        board.setStone(move.row, move.column, this.stone)
        this.flipAdjacent(move)
    }

    fun getValidMove(row: Int, column: Int) : EvaluateResult? {
        var moves = this.moveList()
        for (move in moves) {
            if(move.row == row && move.column == column) {
                return move
            }
        }
        return null
    }

    private fun flipAdjacent(move: EvaluateResult) {
        var opposite = this.stone.opposite()
        if (move.flipped.isNotEmpty()) {
            for (flipped in move.flipped) {
                var rowDelta = flipped.rowDelta
                var columnDelta = flipped.columnDelta
                this.flipLine(move.row + rowDelta, move.column + columnDelta, rowDelta, columnDelta, opposite)
            }
        } else {
            //Game.switchTurns();
        }
    }

    fun flipLine(row: Int, col: Int, rowDelta: Int, colDelta: Int, opposite: Stone?){
        val stopCol = if (colDelta > 0) 8 else -1
        val stopRow = if (rowDelta > 0) 8 else -1

        if (row != stopRow && col != stopCol) {
            var current = this.board.getStone(row, col)
            if (opposite !== null && current === opposite) {
                board.setStone(row, col, this.stone)
                this.flipLine(row + rowDelta, col + colDelta, rowDelta, colDelta, opposite)
                return
            }
        }
    }


}