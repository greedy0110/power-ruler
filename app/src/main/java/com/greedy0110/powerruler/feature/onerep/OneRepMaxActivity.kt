package com.greedy0110.powerruler.feature.onerep

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.greedy0110.powerruler.databinding.ActivityOneRepMaxBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OneRepMaxActivity : AppCompatActivity() {

    private val binding by lazy { ActivityOneRepMaxBinding.inflate(layoutInflater) }
    private val viewModel: OneRepMaxViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun initList() {

    }
}