package com.greedy0110.powerruler.feature.settings

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.greedy0110.powerruler.R
import com.greedy0110.powerruler.databinding.ActivitySettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(Activity.RESULT_CANCELED)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.signal.observe(this) {
            when (it) {
                is SettingsSignal.UpdateComplete -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                is SettingsSignal.WrongUpdate -> {
                    Toast.makeText(this, R.string.system_needed, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
