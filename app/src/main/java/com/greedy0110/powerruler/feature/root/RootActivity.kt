package com.greedy0110.powerruler.feature.root

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import com.greedy0110.powerruler.databinding.ActivityRootBinding
import com.greedy0110.powerruler.feature.init.InitActivity
import com.greedy0110.powerruler.feature.onerep.OneRepMaxActivity
import com.greedy0110.powerruler.usecase.UserSettingUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RootActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRootBinding.inflate(layoutInflater) }

    @Inject
    lateinit var userSettingUseCase: UserSettingUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Handler().postDelayed(1000L) {
            val goal = userSettingUseCase.getGoal()

            if (goal == null) {
                startActivity(
                    Intent(this, InitActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            } else {
                startActivity(
                    Intent(this, OneRepMaxActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }
}
