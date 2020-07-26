package com.greedy0110.powerruler.feature.init

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.greedy0110.powerruler.domain.Kg
import com.greedy0110.powerruler.domain.toKgOrNull
import com.greedy0110.powerruler.usecase.OneRepFormulaUseCase
import dagger.hilt.android.qualifiers.ApplicationContext

class InitViewModel @ViewModelInject constructor(
    @ApplicationContext val appContext: Context
) : ViewModel() {

    val goal = MutableLiveData<String>()
    val confirmEnabled = goal.map { it.isNotBlank() }

    private val cachedWeight: MutableMap<OneRepFormulaUseCase.Workout, Kg?> =
        OneRepFormulaUseCase.Workout.values().map { Pair(it, null) }.toMap().toMutableMap()

    private val cachedRepeat: MutableMap<OneRepFormulaUseCase.Workout, Int?> =
        OneRepFormulaUseCase.Workout.values().map { Pair(it, null) }.toMap().toMutableMap()

    fun getWorkoutName(workout: OneRepFormulaUseCase.Workout): String {
        return appContext.getString(workout.stringResId)
    }

    fun setWorkoutWeight(workout: OneRepFormulaUseCase.Workout, weightCandidate: String) {
        val weight = weightCandidate.toKgOrNull() ?: return
        cachedWeight[workout] = weight
    }

    fun setWorkoutRepeat(workout: OneRepFormulaUseCase.Workout, repeatCandidate: String) {
        val repeat = repeatCandidate.toIntOrNull() ?: return
        cachedRepeat[workout] = repeat
    }
}