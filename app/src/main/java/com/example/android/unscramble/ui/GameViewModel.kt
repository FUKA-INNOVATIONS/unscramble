package com.example.android.unscramble.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.android.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {

    private lateinit var currentWord: String
    private var usedWords: MutableSet<String> = mutableSetOf()

    private val _uiState = MutableStateFlow(GameUiState())

    /*
    * The default visibility modifier in Kotlin is public,
    * so count is public and accessible from other classes like UI controllers.
    * Since only the get() method is being overridden, this property is immutable and read-only.
    * When an outside class accesses this property, it returns the value of _count and its value can't be modified.
    * This backing property protects the app data inside the ViewModel from unwanted and unsafe changes by external classes,
    * but it lets external callers safely access its value.
    * */

    // A backing property lets you return something from a getter other than the exact object.
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        resetGame()
    }

    // a helper method to pick a random word from the list and shuffle it.
    private fun pickRandomWordAndShuffle(): String {
        currentWord = allWords.random()
        if (usedWords.contains(currentWord)) {
            return pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)
        }
    }

    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        tempWord.shuffle() // Scramble the word
        while (String(tempWord).equals(word)) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
        //Log.d("Fuka scumbled: ", "Used words size: ${usedWords.size} - _uiState value: ${_uiState.value}")
    }

}