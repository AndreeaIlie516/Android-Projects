package com.android.geoquiz

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

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

        binding.cheatButton.setOnClickListener {
            //Start CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
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

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        if (messageResId == R.string.correct_toast) {
            quizViewModel.increaseScore()
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