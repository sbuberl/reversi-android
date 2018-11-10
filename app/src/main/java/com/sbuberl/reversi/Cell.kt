package com.sbuberl.reversi

enum class Stone
{
    NONE, BLACK, WHITE, HINT;
    fun opposite() : Stone? {
        if(this === Stone.BLACK) {
            return Stone.WHITE
        } else if(this === Stone.WHITE) {
            return Stone.BLACK
        }
        return null
    }
}

data class Cell(val row: Int, val column: Int, var stone :  Stone)
