package com.example.zuizzsaeed_boggle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.content.Context
import android.widget.Button


class ScoreFragment : Fragment() {

    private lateinit var textScore: TextView
    private var score: Int = 0
    interface OnNewGameListener {
        fun onNewGame()
    }

    private var newGameListener: OnNewGameListener? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        newGameListener = context as? OnNewGameListener
        if (newGameListener == null) {
            throw RuntimeException("$context must implement OnNewGameListener")
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_score, container, false)
        textScore = view.findViewById(R.id.textScore)
        view.findViewById<Button>(R.id.buttonNewGame).setOnClickListener {
            newGameListener?.onNewGame()
        }
        updateDisplay()
        return view
    }

    fun updateScore(points: Int) {
        score += points
        if (score < 0) { // score should never be below 0
            score = 0
        }
        updateDisplay()
    }


    private fun updateDisplay() {
        activity?.runOnUiThread {
            textScore.text = "Score: $score"
        }
    }

    fun resetScore() {
        score = 0
        updateDisplay()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ScoreFragment()
    }
}