package com.greedy0110.powerruler.feature.init

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.greedy0110.powerruler.R
import com.greedy0110.powerruler.databinding.ActivityInitBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InitActivity : AppCompatActivity() {

    private val binding by lazy { ActivityInitBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.commit {
            replace(R.id.container_fragment, GoalFragment())
        }
    }
}
