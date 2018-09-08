package com.sbuberl.reversi

typealias MoveEvaluator = (row: Int, column: Int) -> Int

data class Direction(val rowDelta: Int, val columnDelta: Int)

data class EvaluateResult(val row: Int, val column: Int, val board: Board, val value: Int, val flipped: List<Direction>)

class Board : Cloneable {
    private val cells: Array<Array<Cell>>
    private val directions = listOf(
            Direction(-1, -1), Direction(-1, 0), Direction(-1, 1),
            Direction(-1, 0), Direction(0, 1),
            Direction(1, -1), Direction(1, 0), Direction(1, 1))
    private val emptyCells: HashSet<Cell>
    private val blackMoves: HashSet<EvaluateResult>
    private val whiteMoves: HashSet<EvaluateResult>

    constructor() {
        this.cells = Array(8) { row -> Array(8) { column -> Cell(row, column, Stone.NONE) } }
        this.emptyCells = HashSet<Cell>()
        this.blackMoves = HashSet<EvaluateResult>()
        this.whiteMoves = HashSet<EvaluateResult>()
    }

    constructor(other: Board) {
        val otherCells = other.cells
        this.cells = Array(8) { row -> Array(8) { column -> Cell(row, column, otherCells[row][column].stone) } }
        this.emptyCells = HashSet<Cell>(other.emptyCells)
        this.blackMoves = HashSet<EvaluateResult>(other.blackMoves)
        this.whiteMoves = HashSet<EvaluateResult>(other.whiteMoves)
    }

    override fun clone(): Board {
        return Board(this)
    }

    fun reset() {
        emptyCells.clear()
        blackMoves.clear()
        whiteMoves.clear()

        for (row in 0 until 8) {
            for (column in 0 until 8) {
                val cell = cells[row][column]
                cell.stone = Stone.NONE
                emptyCells.add(cell)
            }
        }

        setStone(3, 3, Stone.BLACK)
        setStone(3, 4, Stone.WHITE)
        setStone(4, 3, Stone.WHITE)
        setStone(4, 4, Stone.BLACK)
    }

    fun getStone(row: Int, column: Int): Stone {
        return cells[row][column].stone
    }

    fun setStone(row: Int, column: Int, stone: Stone) {
        val cell = cells[row][column]
        cell.stone = stone
        emptyCells.remove(cell)
    }

    fun buildMoveList(blackEval: MoveEvaluator, whiteEval: MoveEvaluator) {
        blackMoves.clear()
        whiteMoves.clear()

        for (empty in emptyCells) {
            checkMove(empty, Stone.BLACK, blackMoves, blackEval)
            checkMove(empty, Stone.WHITE, whiteMoves, whiteEval)
        }
    }

    private fun checkMove(empty : Cell, stone: Stone, moveList: HashSet<EvaluateResult>, evaluator: MoveEvaluator) {
        val move = evaluate(empty.row, empty.column, stone, evaluator)
        move?.let {
            moveList.add(it)
        }
    }

    private fun evaluate(row: Int, column: Int, stone: Stone, evaluator: MoveEvaluator): EvaluateResult? {
        var tempBoard = clone()
        var value = 0
        val flippedLines = ArrayList<Direction>()
        for (direction in directions) {
            var result = tempBoard.evaluateLine(row, column, stone, evaluator, direction.rowDelta, direction.columnDelta)
            result?.let {
                value += it
                flippedLines.add(direction)
            }
        }

        if(flippedLines.size > 0) {
            tempBoard.setStone(row, column, stone)
            return EvaluateResult(row, column, tempBoard, value, flippedLines)
        } else {
            return null
        }
    }

    private fun evaluateLine(cellRow: Int, cellColumn: Int, stone: Stone, evaluator: MoveEvaluator, rowDelta: Int, columnDelta: Int) : Int? {
        var current: Stone
        var stopCol = if (columnDelta > 0) 8 else -1
        var stopRow = if (rowDelta > 0) 8 else -1
        var oppositeFound = false
        var value = 0

        var row = cellRow + rowDelta
        var column = cellColumn + columnDelta
        while (row != stopRow && column != stopCol) {
            current = this.cells[row][column].stone
            if (current == Stone.NONE) {
                break
            } else if (current != stone) {
                oppositeFound = true
                value += evaluator(row, column)
                this.cells[row][column].stone = stone
            } else if(oppositeFound) {
                return value
            }
            row += rowDelta
            column += columnDelta
        }

        return null
    }
}