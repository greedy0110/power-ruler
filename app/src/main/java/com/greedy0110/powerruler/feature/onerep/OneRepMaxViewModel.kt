package com.greedy0110.powerruler.feature.onerep

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.greedy0110.powerruler.R
import com.greedy0110.powerruler.usecase.OneRepFormulaUseCase
import com.greedy0110.powerruler.usecase.OneRepFormulaUseCase.Workout
import com.greedy0110.powerruler.usecase.UserSettingUseCase
import dagger.hilt.android.qualifiers.ApplicationContext

class OneRepMaxViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    private val userSettingUseCase: UserSettingUseCase,
    private val formulaUseCase: OneRepFormulaUseCase
) : ViewModel() {

    private val refreshTrigger = MutableLiveData<Boolean>(true)

    val unit: LiveData<String> = refreshTrigger.map { context.getString(R.string.unit_kg) }

    val goal: LiveData<String> = refreshTrigger.map { "/${userSettingUseCase.getGoal()?.toInt()}" }

    val totalOneRep: LiveData<String> =
        refreshTrigger.map { "${formulaUseCase.getOneRepMax().toInt()}" }

    val items: LiveData<List<ItemHolder>> =
        refreshTrigger.map {
            Workout.values().map { workout ->
                ItemHolder(
                    name = when (workout) {
                        Workout.DEAD_LIFT -> context.getString(R.string.dead_lift)
                        Workout.SQUAT -> context.getString(R.string.squat)
                        Workout.BENCH_PRESS -> context.getString(R.string.bench_press)
                    },
                    detail = "${formulaUseCase.getWeight(workout)?.toInt()}" +
                            "${unit.value} " +
                            "${formulaUseCase.getRepeat(workout)}" +
                            context.getString(R.string.unit_repeat),
                    unit = unit.value.orEmpty(),
                    onerep = "${formulaUseCase.getOneRepMaxBy(workout).toInt()}",
                    workout = workout
                )
            }
        }

    fun refresh() {
        refreshTrigger.postValue(true)
    }

    data class ItemHolder(
        val name: String,
        val detail: String,
        val unit: String,
        val onerep: String,
        val workout: Workout
    )
}