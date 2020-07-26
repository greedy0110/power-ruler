package com.greedy0110.powerruler.feature.init

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.observe
import com.greedy0110.powerruler.R
import com.greedy0110.powerruler.databinding.ActivityInitBinding
import com.greedy0110.powerruler.feature.onerep.OneRepMaxActivity
import com.greedy0110.powerruler.usecase.OneRepFormulaUseCase
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class InitActivity : AppCompatActivity() {

    private val binding by lazy { ActivityInitBinding.inflate(layoutInflater) }
    private val viewModel: InitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        supportFragmentManager.commit {
            replace(R.id.container_fragment, GoalFragment())
        }

        viewModel.signal.observe(this) {
            when (it) {
                is InitSignal.WorkoutSignal -> {
                    supportFragmentManager.commit {
                        addToBackStack(null)
                        replace(
                            R.id.container_fragment,
                            OneRepFragment.newInstance(it.workout.ordinal)
                        )
                    }
                }
                is InitSignal.StartSignal -> {
                    startActivity(
                        Intent(this, OneRepMaxActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
            }
        }
    }
}
