package com.example.zuizzsaeed_boggle

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import java.util.Locale
import kotlin.math.abs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GameplayFragment : Fragment() {

    private var listener: OnGameplayInteractionListener? = null
    private lateinit var gridLayout: GridLayout
    private lateinit var textEnteredLetters: TextView
    private val selectedLetters = StringBuilder()
    private val selectedButtonIndices = mutableListOf<Int>()
    private val foundWords = mutableSetOf<String>()
    private var lastClickedButtonIndex: Int = -1
    private val vowels = listOf('A', 'E', 'I', 'O', 'U')
    private val specialConsonants = listOf('S', 'Z', 'P', 'X', 'Q')
    private val dictionaryWords = mutableSetOf<String>()

    interface OnGameplayInteractionListener {
        fun updateScore(score: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnGameplayInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnGameplayInteractionListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gameplay, container, false)
        setupUi(view)
        loadDictionaryWords()
        return view
    }

    private fun setupUi(view: View) {
        gridLayout = view.findViewById(R.id.gridLayout)
        textEnteredLetters = view.findViewById(R.id.textEnteredLetters)

        val buttonIds = listOf(
            R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8,
            R.id.button9, R.id.button10, R.id.button11, R.id.button12,
            R.id.button13, R.id.button14, R.id.button15, R.id.button16
        )

        buttonIds.forEachIndexed { index, buttonId ->
            val button = view.findViewById<Button>(buttonId)
            button.setOnClickListener {
                if (lastClickedButtonIndex == -1 || isAdjacent(lastClickedButtonIndex, index)) {
                    handleLetterButtonClick(it as Button, index)
                } else {
                    Toast.makeText(context, "Please click an adjacent button", Toast.LENGTH_SHORT).show()
                }
            }
        }

        view.findViewById<Button>(R.id.buttonClear).setOnClickListener { clearEnteredLetters() }
        view.findViewById<Button>(R.id.buttonSubmit).setOnClickListener { submitWord() }

        generateGrid()
    }

    private fun generateGrid() {
        val letters = (listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z') - vowels).shuffled().take(12) + vowels.shuffled().take(4)
        val gridButtons = gridLayout.children.toList().filterIsInstance<Button>()
        letters.shuffled().forEachIndexed { index, c ->
            gridButtons[index].text = c.toString()
        }
    }


    private fun handleLetterButtonClick(button: Button, index: Int) {
        if (!selectedButtonIndices.contains(index)) {
            selectedLetters.append(button.text)
            textEnteredLetters.text = "Entered Letters: ${selectedLetters.toString()}"
            lastClickedButtonIndex = index
            selectedButtonIndices.add(index)
        } else {
            Toast.makeText(context, "Each letter can only be used once per word.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearEnteredLetters() {
        selectedLetters.clear()
        textEnteredLetters.text = "Entered Letters: "
        lastClickedButtonIndex = -1
        selectedButtonIndices.clear()
    }

    private fun submitWord() {
        val word = selectedLetters.toString().uppercase(Locale.getDefault())

        // First, check if the word meets the length and vowel requirements
        if (word.length >= 4 && word.count { it in vowels } >= 2) {
            // Next, check if the word has already been found
            if (word in foundWords) {
                Toast.makeText(context, "This word has already been found.", Toast.LENGTH_SHORT).show()
            } else {
                // Then, check if the word is in the dictionary
                if (dictionaryWords.contains(word)) {
                    // Calculate the score for a valid word
                    val score = word.map { char ->
                        if (char in vowels) 5 else 1
                    }.sum() * if (word.any { it in specialConsonants }) 2 else 1

                    listener?.updateScore(score) // Notify the listener to update the score
                    Toast.makeText(context, "Points awarded: $score", Toast.LENGTH_SHORT).show()
                    foundWords.add(word) // Add the word to the set of found words
                } else {
                    // If the word is not in the dictionary, subtract points and notify the user
                    Toast.makeText(context, "$word is not in the dictionary. 10 points subtracted!", Toast.LENGTH_LONG).show()
                    listener?.updateScore(-10)
                }
            }
        } else {
            // If the word does not meet the length and vowel requirements, notify the user
            Toast.makeText(context, "Word must be at least 4 letters long and contain at least 2 vowels", Toast.LENGTH_SHORT).show()
        }

        clearEnteredLetters() // Clear the current selection of letters
    }




    private fun isAdjacent(pos1: Int, pos2: Int): Boolean {
        val row1 = pos1 / 4
        val col1 = pos1 % 4
        val row2 = pos2 / 4
        val col2 = pos2 % 4
        return abs(row1 - row2) <= 1 && abs(col1 - col2) <= 1
    }

    private fun loadDictionaryWords() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val inputStream = requireContext().assets.open("dictionary.txt")
                inputStream.bufferedReader().forEachLine { line ->
                    dictionaryWords.add(line.uppercase())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } }
    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}