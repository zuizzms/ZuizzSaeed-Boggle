package com.example.zuizzsaeed_boggle

import android.widget.GridLayout
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.*

class GameplayFragment : Fragment() {
    // Interface for communication with activity
    interface OnGameplayInteractionListener {
        fun onLettersEntered(letters: String)
        fun updateScore(score: Int)
    }


    private var interactionListener: OnGameplayInteractionListener? = null
    private lateinit var enteredLettersTextView: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnGameplayInteractionListener) {
            interactionListener = context
        } else {
            throw RuntimeException("$context must implement OnGameplayInteractionListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_gameplay, container, false)

        // Get the grid layout
        val gridLayout = view.findViewById<GridLayout>(R.id.gridLayout)

        // Find the TextView for entered letters
        enteredLettersTextView = view.findViewById(R.id.textEnteredLetters)

        // Iterate over buttons and assign random letters
        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as? Button
            val letter = getRandomLetter()
            button?.text = letter // Assign a random letter to each button
            // Set click listener for the button
            button?.setOnClickListener {
                appendEnteredLetter(letter)
            }
        }

        // Set click listener for the Clear button
        view.findViewById<Button>(R.id.buttonClear).setOnClickListener {
            clearEnteredLetters()
        }

        // Set click listener for the Submit button
        view.findViewById<Button>(R.id.buttonSubmit).setOnClickListener {
            val enteredLetters = enteredLettersTextView.text.toString()
            interactionListener?.onLettersEntered(enteredLetters)
            interactionListener?.updateScore(10) // Adding 10 points to the score
        }

        return view
    }

    // Function to get a random letter from the alphabet
    private fun getRandomLetter(): String {
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val random = Random()
        return alphabet[random.nextInt(alphabet.length)].toString()
    }

    // Function to append a letter to the entered letters text
    private fun appendEnteredLetter(letter: String) {
        val enteredText = enteredLettersTextView.text.toString()
        enteredLettersTextView.text = "$enteredText$letter"
    }

    // Function to clear the entered letters
    private fun clearEnteredLetters() {
        enteredLettersTextView.text = ""
    }
}
