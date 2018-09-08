package com.sbuberl.reversi

class Player {
    private val stone: Stone
    private val evaluator: MoveEvaluator
    private var points = 2

    constructor(stone: Stone, evaluator: MoveEvaluator) {
        this.stone = stone
        this.evaluator = evaluator
    }
}