package com.greedy0110.powerruler.feature.onerep.update

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.greedy0110.powerruler.usecase.OneRepFormulaUseCase
import dagger.hilt.android.qualifiers.ApplicationContext

class UpdateViewModel @ViewModelInject constructor(
    @ApplicationContext private val appContext: Context,
    private val oneRepFormulaUseCase: OneRepFormulaUseCase
) : ViewModel() {

    private val _workout: MutableLiveData<OneRepFormulaUseCase.Workout> = MutableLiveData()
    val workoutName: LiveData<String> = _workout.map { appContext.getString(it.stringResId) }

    val weight: LiveData<String> = _workout
        .map { oneRepFormulaUseCase.getWeight(it) ?: 0.0 }
        .map { it.toInt().toString() }

    val reps: LiveData<String> = _workout
        .map { oneRepFormulaUseCase.getRepeat(it) ?: 0 }
        .map { it.toString() }

    val currentWeight: MutableLiveData<String> = MutableLiveData("")
    val currentRepeat: MutableLiveData<String> = MutableLiveData("")

    fun setWorkout(workout: OneRepFormulaUseCase.Workout) {
        _workout.value = workout
    }
}