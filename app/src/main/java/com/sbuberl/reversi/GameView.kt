package com.sbuberl.reversi

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.math.floor


class GameView(context: Context, attrs: AttributeSet ? = null): SurfaceView(context, attrs), SurfaceHolder.Callback {
    private var canvasWidth = 0
    private var canvasHeight = 0
    private var cellSize = 0f
    private var scoreBoardHeight = 120
    private var game: Game? = null
    private var thread: GameThread

    init {
        holder.addCallback(this)
        // instantiate the game thread
        thread = GameThread(holder, this)
    }

    fun setGame(game: Game) {
        this.game = game
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        var retry = true
        while (retry) {
            try {
                thread.setRunning(false)
                thread.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            retry = false
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touch = event!!
        val touched_x = touch.x.toInt()
        val touched_y = touch.y.toInt()

        val action = touch.action
        var touched = false
        when (action) {
            MotionEvent.ACTION_DOWN -> touched = true
            MotionEvent.ACTION_MOVE -> touched = true
            MotionEvent.ACTION_UP -> touched = false
            MotionEvent.ACTION_CANCEL -> touched = false
            MotionEvent.ACTION_OUTSIDE -> touched = false
        }

        if (touched) {
            val row = floor((touched_y - scoreBoardHeight) / (cellSize + 1)).toInt()
            if (row in 1..8) {
                val column = floor(touched_x / (cellSize + 1)).toInt()
                val current: Stone = game!!.getStone(row, column)
                if (current == Stone.HINT) {
                    var user = game!!.userPlayer
                    var move = user.getValidMove(row, column)
                    if(move !== null) {
                        user.play(move)
                    }
                }
            }
        }
        return true
    }

    fun drawBoard(canvas: Canvas) {
        this.canvasWidth = canvas.width
        this.canvasHeight = canvas.height
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
        var x: Float
        var y: Float = scoreBoardHeight.toFloat() + 1

        paint.style = Paint.Style.FILL
        val color = Color.parseColor("#0BA31C")
        paint.color = color

        for (row in 0 until 8) {
            x = 1f
            for (column in 0 until 8) {
                canvas.drawRect(x, y, x + cellSize, y + cellSize, paint)
                val stone = game!!.getStone(row, column)
                if (stone == Stone.BLACK || stone == Stone.WHITE) {
                    drawStone(canvas, row, column, stone)
                } else if (stone == Stone.HINT) {
                    drawHint(canvas, row, column)
                }
                x += cellSize + 1
            }
            y += cellSize + 1
        }
    }

    private fun drawStone(canvas: Canvas, row: Int, column: Int, stone: Stone) {
        val paint = Paint()
        paint.style = Paint.Style.FILL
        val centerX = column * (cellSize + 1) + cellSize / 2f
        val centerY = row * (cellSize + 1) + cellSize / 2f + scoreBoardHeight
        val radius = 0.4f * cellSize
        if (stone == Stone.BLACK) {
            paint.color = Color.BLACK
            canvas.drawCircle(centerX, centerY, radius, paint)
        } else if (stone == Stone.WHITE) {
            paint.color = Color.WHITE
            canvas.drawCircle(centerX, centerY, radius, paint)
        }
    }

    private fun drawHint(canvas: Canvas, row: Int, column: Int) {
        val paint = Paint()
        paint.style = Paint.Style.FILL
        val centerX = column * (cellSize + 1) + cellSize / 2f
        val centerY = row * (cellSize + 1) + cellSize / 2f + scoreBoardHeight
        val radius = 0.1f * cellSize
        paint.color = Color.BLUE
        canvas.drawCircle(centerX, centerY, radius, paint)
    }

}

