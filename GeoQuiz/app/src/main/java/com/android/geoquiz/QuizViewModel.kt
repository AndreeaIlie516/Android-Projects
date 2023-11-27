package com.android.geoquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val SCORE_KEY = "SCORE_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"

class QuizViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    private var score: Int
        get() = savedStateHandle.get(SCORE_KEY) ?: 0
        set(value) = savedStateHandle.set(SCORE_KEY, value)

    private var cheatsUsed: Int
        get() = savedStateHandle.get("CHEATS_USED") ?: 0
        set(value) = savedStateHandle.set("CHEATS_USED", value)

    private val maxCheatsAllowed = 3

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    var currentQuestionIsAnswered: Boolean
        get() = questionBank[currentIndex].isAnswered
        set(value) {
            questionBank[currentIndex].isAnswered = value
        }

    var isCheater: Boolean
        get() = questionBank[currentIndex].isCheated
        set(value) {
            questionBank[currentIndex].isCheated = value
        }

    val canCheat: Boolean
        get() = cheatsUsed < maxCheatsAllowed

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = if (currentIndex > 0) {
            (currentIndex - 1) % questionBank.size
        } else {
            questionBank.size - 1
        }
    }

    fun increaseScore() {
        score += 1
    }

    fun checkScore(): Int {
        if (currentIndex == questionBank.size - 1) {
            return score
        }
        return -1
    }

    fun markCurrentQuestionAsCheated() {
        questionBank[currentIndex].isCheated = true
    }

    fun useCheat() {
        if (canCheat) {
            cheatsUsed++
            markCurrentQuestionAsCheated()
        }
    }

    fun getRemainingCheats(): Int = maxCheatsAllowed - cheatsUsed
}