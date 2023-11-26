package com.android.geoquiz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var currentIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trueButton.setOnClickListener { view: View ->
            questionBank[currentIndex].isAnswered = true
            binding.trueButton.isEnabled = false
            binding.falseButton.isEnabled = false
            checkAnswer(true)
        }

        binding.falseButton.setOnClickListener { view: View ->
            questionBank[currentIndex].isAnswered = true
            binding.trueButton.isEnabled = false
            binding.falseButton.isEnabled = false
            checkAnswer(false)
        }

        binding.questionTextView.setOnClickListener { view: View ->
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }


        binding.nextButton.setOnClickListener {
            checkScore()
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        binding.prevButton.setOnClickListener {
            currentIndex = if (currentIndex > 0) {
                (currentIndex - 1) % questionBank.size
            } else {
                questionBank.size - 1
            }
            updateQuestion()
        }

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        binding.questionTextView.setText(questionTextResId)
        updateButtons()
    }

    private fun updateButtons() {
        val questionIsAnswered = questionBank[currentIndex].isAnswered
        binding.trueButton.isEnabled = !questionIsAnswered
        binding.falseButton.isEnabled = !questionIsAnswered
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer

        val messageResId: Int

        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            score += 1
        } else {
            messageResId = R.string.incorrect_toast
        }

        Toast.makeText(
            this, messageResId, Toast.LENGTH_SHORT
        ).show()

        questionBank[currentIndex].isAnswered = true
        updateButtons()
    }


    private fun checkScore() {
        if (currentIndex == questionBank.size - 1) {
            Toast.makeText(
                this, "Score: $score", Toast.LENGTH_SHORT
            ).show()
        }
    }
}