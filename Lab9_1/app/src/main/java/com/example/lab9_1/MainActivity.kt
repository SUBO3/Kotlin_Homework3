package com.example.lab9_1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var progressRabbit = 0
    private var progressTurtle = 0
    private lateinit var btnStart: Button
    private lateinit var sbRabbit: SeekBar
    private lateinit var sbTurtle: SeekBar

    private val handler = Handler(Looper.getMainLooper()) { msg ->
        when (msg.what) {
            1 -> updateProgress(sbRabbit, progressRabbit, "兔子勝利")
            2 -> updateProgress(sbTurtle, progressTurtle, "烏龜勝利")
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainLayout = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { view, insets ->
            val padding = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(padding.left, padding.top, padding.right, padding.bottom)
            insets
        }

        btnStart = findViewById(R.id.btnStart)
        sbRabbit = findViewById(R.id.sbRabbit)
        sbTurtle = findViewById(R.id.sbTurtle)

        btnStart.setOnClickListener {
            startRace()
        }
    }

    private fun startRace() {
        btnStart.isEnabled = false
        progressRabbit = 0
        progressTurtle = 0
        sbRabbit.progress = 0
        sbTurtle.progress = 0
        thread { runParticipant(1, ::rabbitStrategy) }
        thread { runParticipant(2) { 1 } }
    }

    private fun runParticipant(id: Int, strategy: () -> Int) {
        while (progressRabbit < 100 && progressTurtle < 100) {
            Thread.sleep(100)
            if (id == 1) progressRabbit += strategy() else progressTurtle += strategy()
            handler.sendEmptyMessage(id)
        }
    }

    private fun rabbitStrategy(): Int {
        if ((0..2).random() < 2) Thread.sleep(300)
        return 3
    }

    private fun updateProgress(seekBar: SeekBar, progress: Int, winner: String) {
        seekBar.progress = progress
        if (progress >= 100) {
            Toast.makeText(this, winner, Toast.LENGTH_SHORT).show()
            btnStart.isEnabled = true
        }
    }
}
