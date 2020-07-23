package com.greedy0110.powerruler.feature.onerep

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.greedy0110.powerruler.databinding.ActivityOneRepMaxBinding

class OneRepMaxActivity : AppCompatActivity() {

    private val binding by lazy { ActivityOneRepMaxBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}