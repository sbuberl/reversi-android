package com.sbuberl.reversi

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context, attrs: AttributeSet ? = null): SurfaceView(context, attrs), SurfaceHolder.Callback  {
    private var canvasWidth = 0
    private var canvasHeight = 0
    private var cellSize = 0f
    private var scoreBoardHeight = 120

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        var canvas : Canvas? = null
        try {
            canvas = this.holder.lockCanvas(null)
            canvas?.let {
                synchronized(this) {
                    this.canvasWidth = it.width
                    this.canvasHeight = it.height
                    drawBoard(it)
                }
            }
        } finally {
            canvas?.let {
                this.holder.unlockCanvasAndPost(it)
            }
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    private fun drawBoard(canvas: Canvas) {
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK
        canvas.drawPaint(paint)

        var boardSize: Float
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            boardSize = this.canvasWidth.toFloat()
        } else {
            boardSize = this.canvasHeight.toFloat()
        }

        cellSize = boardSize / 8.0f - 1
        var x : Float
        var y : Float = scoreBoardHeight.toFloat() + 1

        paint.style = Paint.Style.FILL
        val color = Color.parseColor("#0BA31C")
        paint.color = color

        for (row in 0 until 8) {
            x = 1f
            for (column in 0 until 8) {
                canvas.drawRect(x, y, x + cellSize, y + cellSize, paint)
                x += cellSize + 1
            }
            y += cellSize + 1
        }

        drawStone(canvas, 3, 3, Stone.BLACK)
        drawStone(canvas, 3, 4, Stone.WHITE)
        drawStone(canvas, 4, 3, Stone.WHITE)
        drawStone(canvas, 4, 4, Stone.BLACK)
    }

    private fun drawStone(canvas: Canvas, row: Int, column: Int, stone: Stone) {
        val paint = Paint()
        paint.style = Paint.Style.FILL

        val centerX = column * (cellSize + 1) + cellSize / 2f
        val centerY = row * (cellSize + 1) + cellSize / 2f + scoreBoardHeight
        val radius = 0.4f * cellSize
        if(stone == Stone.BLACK) {
            paint.color = Color.BLACK
            canvas.drawCircle(centerX, centerY, radius, paint)
        } else if(stone == Stone.WHITE) {
            paint.color = Color.WHITE
            canvas.drawCircle(centerX, centerY, radius, paint)
        }
    }
}

