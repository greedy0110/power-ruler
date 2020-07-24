package com.greedy0110.powerruler.feature.onerep

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.runBlocking

class OneRepMaxViewModel @ViewModelInject constructor(
    private val useCase: OneRepFormulaUseCase
) : ViewModel() {

    init {

        runBlocking {
            useCase.setGoal(500.0)

            useCase.setWeight(OneRepFormulaUseCase.Workout.DEAD_LIFT, 140.0)
            useCase.setRepeat(OneRepFormulaUseCase.Workout.DEAD_LIFT, 2)

            useCase.setWeight(OneRepFormulaUseCase.Workout.SQUAT, 100.0)
            useCase.setRepeat(OneRepFormulaUseCase.Workout.SQUAT, 10)

            useCase.setWeight(OneRepFormulaUseCase.Workout.BENCH_PRESS, 80.0)
            useCase.setRepeat(OneRepFormulaUseCase.Workout.BENCH_PRESS, 10)
        }
    }

    val unit: LiveData<String> = MutableLiveData("kg")

    val goal: LiveData<String> = liveData {
        emit("/${useCase.getGoal()?.toInt()}")
    }

    val totalOneRep: LiveData<String> = liveData {
        emit("${useCase.getOneRepMax().toInt()}")
    }
}