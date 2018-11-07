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
        this.flipAdjacent(move, 0)
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

    private fun flipAdjacent(move: EvaluateResult, lineIndex: Int)
    {
        var flipped = move.flipped;
        if(lineIndex < flipped.size)
        {
            var direction = Board.directions[lineIndex]
            var rowDelta = direction.rowDelta
            var columnDelta = direction.columnDelta
            this.flipLine(move.row + rowDelta, move.column + columnDelta, rowDelta, columnDelta)
            this.flipAdjacent(move, lineIndex + 1)
        } else {
            //Game.switchTurns();
        }
    }

    fun flipLine(row: Int, col: Int, rowDelta: Int, colDelta: Int) {
        val stopCol = if (colDelta > 0) 8 else -1
        val stopRow = if (rowDelta > 0) 8 else -1

        if (row != stopRow && col != stopCol) {
            var current = this.board.getStone(row, col);
            if (current != this.stone && current != Stone.NONE) {
                board.setStone(row, col, this.stone)
                this.flipLine(row + rowDelta, col + colDelta, rowDelta, colDelta)
                return
            }
        }
    }


}