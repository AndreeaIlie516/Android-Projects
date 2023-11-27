package com.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.geoquiz.databinding.ActivityCheatBinding

const val EXTRA_ANSWER_SHOWN = "com.android.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE = "com.android.geoquiz.answer_is_true"

class CheatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheatBinding

    private var answerIsTrue = false
    private var hasCheated = false
    private var isAnswerShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiLevelTextView: TextView = findViewById(R.id.api_level_text_view)
        val apiLevel = Build.VERSION.SDK_INT
        apiLevelTextView.text = getString(R.string.api_level, apiLevel)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        if (savedInstanceState != null) {
            hasCheated = savedInstanceState.getBoolean("HAS_CHEATED", false)
            isAnswerShown = savedInstanceState.getBoolean("IS_ANSWER_SHOWN", false)
            if (isAnswerShown) {
                showAnswer()
            }
        }

        binding.showAnswerButton.setOnClickListener {
            showAnswer()
        }
    }

    private fun showAnswer() {
        hasCheated = true
        isAnswerShown = true
        val answerText = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        binding.answerTextView.setText(answerText)
        setAnswerShownResult(true)
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("HAS_CHEATED", hasCheated)
        outState.putBoolean("IS_ANSWER_SHOWN", isAnswerShown)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}