package com.greedy0110.powerruler.feature.init

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.greedy0110.powerruler.R
import com.greedy0110.powerruler.domain.Kg
import com.greedy0110.powerruler.domain.toKgOrNull
import com.greedy0110.powerruler.usecase.OneRepFormulaUseCase
import com.greedy0110.powerruler.usecase.UserSettingUseCase
import dagger.hilt.android.qualifiers.ApplicationContext

class InitViewModel @ViewModelInject constructor(
    @ApplicationContext private val appContext: Context,
    private val userSettingUseCase: UserSettingUseCase,
    private val oneRepFormulaUseCase: OneRepFormulaUseCase
) : ViewModel() {

    private val _signal: MutableLiveData<InitSignal> = MutableLiveData()
    val signal: LiveData<InitSignal> = _signal

    val goal = MutableLiveData<String>()
    private val _confirmText: MutableLiveData<String> =
        MutableLiveData(appContext.getString(R.string.next))
    val confirmText: LiveData<String> = _confirmText
    val confirmEnabled = goal.map { it.isNotBlank() }

    private val _currentWorkout: MutableLiveData<OneRepFormulaUseCase.Workout?> =
        MutableLiveData(null)
    val currentWorkout: LiveData<OneRepFormulaUseCase.Workout?> = _currentWorkout

    private val cachedWeight: MutableMap<OneRepFormulaUseCase.Workout, Kg> =
        OneRepFormulaUseCase.Workout.values().map { Pair(it, 0.0) }.toMap().toMutableMap()

    private val cachedRepeat: MutableMap<OneRepFormulaUseCase.Workout, Int> =
        OneRepFormulaUseCase.Workout.values().map { Pair(it, 0) }.toMap().toMutableMap()

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

    fun setWorkout(workout: OneRepFormulaUseCase.Workout) {
        _currentWorkout.postValue(workout)
    }

    // 다음 버튼 누를 때마다, 상태에 따라서 다른 행동을 해야함.
    // 다음 버튼은 상태에 따라서 시작하기 텍스트를 보여 줘야함.
    // 다음 버튼을 누를 때, 캐시에 있는 값을 실제 값으로 세팅해주어야한다.
    fun next() {
        val workout = currentWorkout.value

        save()
        _signal.postValue(
            when (workout) {
                null ->
                    InitSignal.WorkoutSignal(OneRepFormulaUseCase.Workout.DEAD_LIFT)
                OneRepFormulaUseCase.Workout.DEAD_LIFT ->
                    InitSignal.WorkoutSignal(OneRepFormulaUseCase.Workout.SQUAT)
                OneRepFormulaUseCase.Workout.SQUAT ->
                    InitSignal.WorkoutSignal(OneRepFormulaUseCase.Workout.BENCH_PRESS)
                OneRepFormulaUseCase.Workout.BENCH_PRESS ->
                    InitSignal.StartSignal
            }
        )

        _confirmText.postValue(
            appContext.getString(
                if (workout == OneRepFormulaUseCase.Workout.BENCH_PRESS) R.string.start else R.string.next
            )
        )
    }

    private fun save() {
        val goalKg = goal.toKgOrNull() ?: return
        userSettingUseCase.setGoal(goalKg)

        for ((workout, kg) in cachedWeight) {
            oneRepFormulaUseCase.setWeight(workout, kg)
        }

        for ((workout, repeat) in cachedRepeat) {
            oneRepFormulaUseCase.setRepeat(workout, repeat)
        }
    }
}
