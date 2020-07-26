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
import kotlinx.coroutines.runBlocking

class OneRepMaxViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    private val userSettingUseCase: UserSettingUseCase,
    private val formulaUseCase: OneRepFormulaUseCase
) : ViewModel() {

    private val refreshTrigger = MutableLiveData<Boolean>(true)

    val unit: LiveData<String> = refreshTrigger.map { "kg" }

    val goal: LiveData<String> = refreshTrigger.map { "/${userSettingUseCase.getGoal()?.toInt()}" }

    val totalOneRep: LiveData<String> =
        refreshTrigger.map { "${formulaUseCase.getOneRepMax().toInt()}" }

    val items: LiveData<List<ItemHolder2>> =
        refreshTrigger.map {
            Workout.values().map { workout ->
                ItemHolder2(
                    name = when (workout) {
                        Workout.DEAD_LIFT -> context.getString(R.string.dead_lift)
                        Workout.SQUAT -> context.getString(R.string.squat)
                        Workout.BENCH_PRESS -> context.getString(R.string.bench_press)
                    },
                    detail = "${formulaUseCase.getWeight(workout)
                        ?.toInt()}${unit.value} ${formulaUseCase.getRepeat(workout)}rep",
                    unit = unit.value.orEmpty(),
                    onerep = "${formulaUseCase.getOneRepMaxBy(workout).toInt()}",
                    workout = workout
                )
            }
        }

    private val cachedOneRepBy = mutableMapOf<Workout, LiveData<String>>()
    fun getOneRepBy(workout: Workout): LiveData<String> {
        return cachedOneRepBy[workout] ?: kotlin.run {
            val new = refreshTrigger.map { "${formulaUseCase.getOneRepMaxBy(workout).toInt()}" }
            cachedOneRepBy[workout] = new
            new
        }
    }

    private val cachedWorkoutDetail = mutableMapOf<Workout, LiveData<String>>()
    fun getWorkoutDetail(workout: Workout): LiveData<String> {
        return cachedWorkoutDetail[workout] ?: kotlin.run {
            val new = refreshTrigger.map {
                "${formulaUseCase.getWeight(workout)
                    ?.toInt()}${unit.value} ${formulaUseCase.getRepeat(workout)}rep"
            }
            cachedWorkoutDetail[workout] = new
            new
        }
    }

    private val cachedWorkoutName = mutableMapOf<Workout, LiveData<String>>()
    fun getWorkoutName(workout: Workout): LiveData<String> {
        return cachedWorkoutName[workout] ?: kotlin.run {
            val new = refreshTrigger.map {
                when (workout) {
                    Workout.DEAD_LIFT -> context.getString(R.string.dead_lift)
                    Workout.SQUAT -> context.getString(R.string.squat)
                    Workout.BENCH_PRESS -> context.getString(R.string.bench_press)
                }
            }
            cachedWorkoutName[workout] = new
            new
        }
    }

    fun refresh() {
        refreshTrigger.postValue(true)
    }

    data class ItemHolder(val workout: Workout)

    data class ItemHolder2(
        val name: String,
        val detail: String,
        val unit: String,
        val onerep: String,
        val workout: Workout
    )
}