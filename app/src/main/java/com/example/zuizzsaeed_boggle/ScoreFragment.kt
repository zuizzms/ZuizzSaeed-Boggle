package com.example.zuizzsaeed_boggle
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ScoreFragment : Fragment() {

    private lateinit var scoreTextView: TextView
    private var currentScore: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_score, container, false)

        // Initialize score text view
        scoreTextView = view.findViewById(R.id.textScore)
        updateScore(currentScore)

        // Return the inflated layout
        return view
    }

    // Update the score displayed in the score text view
    fun updateScore(score: Int) {
        currentScore = score
        scoreTextView.text = "Score: $score"
    }
}
