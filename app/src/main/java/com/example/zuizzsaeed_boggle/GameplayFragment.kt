package com.example.zuizzsaeed_boggle
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.widget.GridLayout
import java.util.*
import kotlin.math.abs

class GameplayFragment : Fragment() {

    interface OnGameplayInteractionListener {
        fun onLettersEntered(letters: String)
        fun updateScore(score: Int)
    }

    private var interactionListener: OnGameplayInteractionListener? = null
    private lateinit var enteredLettersTextView: TextView
    private var lastPressedButton: Button? = null
    private val selectedIndices = HashSet<Int>()
    private var currentScore = 0

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

        // Set click listener for the Clear button
        view.findViewById<Button>(R.id.buttonClear).setOnClickListener {
            clearEnteredLetters()
        }

        // Set click listener for the Submit button
        view.findViewById<Button>(R.id.buttonSubmit).setOnClickListener {
            val enteredLetters = enteredLettersTextView.text.toString()
            val score = calculateScore(enteredLetters)
            interactionListener?.updateScore(score)
        }

        // Iterate over buttons and assign random letters
        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as? Button
            val letter = getRandomLetter()
            button?.text = letter // Assign a random letter to each button
            // Set click listener for the button
            button?.setOnClickListener {
                handleButtonClick(button, letter, i)
            }
        }

        return view
    }

    private fun calculateScore(word: String): Int {
        val consonantsWorthOne = "BCDFGHJKLMNPQRSTVWXYZ"
        val consonantsWorthDouble = "SZPXQ"
        val vowels = "AEIOU"
        var containsConsonantsWorthDouble: Boolean = false

        var score = 0
        for (letter in word) {
            if (letter in consonantsWorthDouble) {
                containsConsonantsWorthDouble = true
            }
            if (letter in consonantsWorthOne) {
                score += 1
            } else if (letter in vowels) {
                score += 5 // Vowels are worth 5 points
            }
        }
        if (containsConsonantsWorthDouble) {
            score *= 2
        }
        return score
    }


    private fun getRandomLetter(): String {
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val random = Random()
        return alphabet[random.nextInt(alphabet.length)].toString()
    }

    private fun handleButtonClick(button: Button, letter: String, index: Int) {
        val enteredText = enteredLettersTextView.text.toString()
        if (lastPressedButton == null || isAdjacent(lastPressedButton!!, button)) {
            if (!selectedIndices.contains(index)) {
                enteredLettersTextView.text = "$enteredText$letter"
                lastPressedButton = button
                selectedIndices.add(index)
            } else {
                Toast.makeText(requireContext(), "You may only use each letter once!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Letters must be adjacent!", Toast.LENGTH_SHORT).show()
        }
    }



    private fun countVowels(word: String): Int {
        val vowels = "AEIOUaeiou"
        return word.count { vowels.contains(it) }
    }

    private fun clearEnteredLetters() {
        enteredLettersTextView.text = "Entered Letters: "
        lastPressedButton = null
        selectedIndices.clear()
    }


    private fun isAdjacent(button1: Button, button2: Button): Boolean {
        val index1 = getIndex(button1)
        val index2 = getIndex(button2)
        val col1 = index1 % 4
        val row1 = index1 / 4
        val col2 = index2 % 4
        val row2 = index2 / 4
        return abs(col1 - col2) <= 1 && abs(row1 - row2) <= 1
    }

    private fun getIndex(button: Button): Int {
        val id = button.id
        return when (id) {
            R.id.button1 -> 0
            R.id.button2 -> 1
            R.id.button3 -> 2
            R.id.button4 -> 3
            R.id.button5 -> 4
            R.id.button6 -> 5
            R.id.button7 -> 6
            R.id.button8 -> 7
            R.id.button9 -> 8
            R.id.button10 -> 9
            R.id.button11 -> 10
            R.id.button12 -> 11
            R.id.button13 -> 12
            R.id.button14 -> 13
            R.id.button15 -> 14
            R.id.button16 -> 15
            else -> throw IllegalArgumentException("Unknown button ID")
        }
    }

}
