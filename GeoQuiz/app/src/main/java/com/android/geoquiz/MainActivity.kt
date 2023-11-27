package com.android.geoquiz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding.trueButton.setOnClickListener { view: View ->
            quizViewModel.currentQuestionIsAnswered = true
            binding.trueButton.isEnabled = false
            binding.falseButton.isEnabled = false
            checkAnswer(true)
        }

        binding.falseButton.setOnClickListener { view: View ->
            quizViewModel.currentQuestionIsAnswered = true
            binding.trueButton.isEnabled = false
            binding.falseButton.isEnabled = false
            checkAnswer(false)
        }

        binding.questionTextView.setOnClickListener { view: View ->
            quizViewModel.moveToNext()
            updateQuestion()
        }


        binding.nextButton.setOnClickListener {
            checkScore()
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        updateQuestion()
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
        updateButtons()
    }

    private fun updateButtons() {
        val questionIsAnswered = quizViewModel.currentQuestionIsAnswered
        binding.trueButton.isEnabled = !questionIsAnswered
        binding.falseButton.isEnabled = !questionIsAnswered
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId: Int

        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            quizViewModel.increaseScore()
        } else {
            messageResId = R.string.incorrect_toast
        }

        Toast.makeText(
            this, messageResId, Toast.LENGTH_SHORT
        ).show()

        quizViewModel.currentQuestionIsAnswered = true
        updateButtons()
    }


    private fun checkScore() {
        val score = quizViewModel.checkScore()
        if (score >= 0) {
            Toast.makeText(
                this, "Score: $score", Toast.LENGTH_SHORT
            ).show()
        }
    }
}