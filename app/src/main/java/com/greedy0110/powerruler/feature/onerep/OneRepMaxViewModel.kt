package com.greedy0110.powerruler.feature.onerep

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
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

    init {

        runBlocking {
            userSettingUseCase.setGoal(500.0)

            formulaUseCase.setWeight(Workout.DEAD_LIFT, 140.0)
            formulaUseCase.setRepeat(Workout.DEAD_LIFT, 2)

            formulaUseCase.setWeight(Workout.SQUAT, 100.0)
            formulaUseCase.setRepeat(Workout.SQUAT, 10)

            formulaUseCase.setWeight(Workout.BENCH_PRESS, 80.0)
            formulaUseCase.setRepeat(Workout.BENCH_PRESS, 10)
        }
    }

    val unit: LiveData<String> = MutableLiveData("kg")

    val goal: LiveData<String> = liveData {
        emit("/${userSettingUseCase.getGoal()?.toInt()}")
    }

    val totalOneRep: LiveData<String> = liveData {
        emit("${formulaUseCase.getOneRepMax().toInt()}")
    }

    val items: LiveData<List<ItemHolder>> = MutableLiveData(
        Workout.values().map { ItemHolder(it) }
    )

    private val cachedOneRepBy = mutableMapOf<Workout, LiveData<String>>()
    fun getOneRepBy(workout: Workout): LiveData<String> {
        return cachedOneRepBy[workout] ?: kotlin.run {
            val new = MutableLiveData(
                "${formulaUseCase.getOneRepMaxBy(workout).toInt()}"
            )
            cachedOneRepBy[workout] = new
            new
        }
    }

    private val cachedWorkoutDetail = mutableMapOf<Workout, LiveData<String>>()
    fun getWorkoutDetail(workout: Workout): LiveData<String> {
        return cachedWorkoutDetail[workout] ?: kotlin.run {
            val new = MutableLiveData(
                "${formulaUseCase.getWeight(workout)
                    ?.toInt()}${unit.value} ${formulaUseCase.getRepeat(workout)}rep"
            )
            cachedWorkoutDetail[workout] = new
            new
        }
    }

    private val cachedWorkoutName = mutableMapOf<Workout, LiveData<String>>()
    fun getWorkoutName(workout: Workout): LiveData<String> {
        return cachedWorkoutName[workout] ?: kotlin.run {
            val new = MutableLiveData(
                when (workout) {
                    Workout.DEAD_LIFT -> context.getString(R.string.dead_lift)
                    Workout.SQUAT -> context.getString(R.string.squat)
                    Workout.BENCH_PRESS -> context.getString(R.string.bench_press)
                }
            )
            cachedWorkoutName[workout] = new
            new
        }
    }

    data class ItemHolder(val workout: Workout)
}