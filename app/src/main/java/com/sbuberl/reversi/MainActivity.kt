package com.sbuberl.reversi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view: GameView = findViewById(R.id.gameView) as GameView
        val game = Game(view)
        game.start()
    }
}
