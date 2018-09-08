package com.sbuberl.reversi

enum class Stone
{
    NONE, BLACK, WHITE, HINT;
}

data class Cell(val row: Int, val column: Int, var stone :  Stone)
