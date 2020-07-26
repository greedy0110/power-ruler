package com.greedy0110.powerruler.feature.init

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.greedy0110.powerruler.R
import com.greedy0110.powerruler.domain.toKgOrNull
import com.greedy0110.powerruler.usecase.OneRepFormulaUseCase
import com.greedy0110.powerruler.usecase.UserSettingUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber

class InitViewModel @ViewModelInject constructor(
    @ApplicationContext private val appContext: Context,
    private val userSettingUseCase: UserSettingUseCase,
    private val oneRepFormulaUseCase: OneRepFormulaUseCase
) : ViewModel() {

    private val _signal: MutableLiveData<InitSignal> = MutableLiveData()
    val signal: LiveData<InitSignal> = _signal

    val goal = MutableLiveData<String>()

    private val _currentWorkout: MutableLiveData<OneRepFormulaUseCase.Workout?> =
        MutableLiveData(null)
    val workoutName = _currentWorkout.map { appContext.getString(it?.stringResId ?: 0) }

    val confirmText: LiveData<String> = _currentWorkout.map {
        appContext.getString(
            if (it == OneRepFormulaUseCase.Workout.BENCH_PRESS) R.string.start else R.string.next
        )
    }
    val confirmEnabled = goal.map { it.isNotBlank() }

    private val edits = mutableMapOf<
            OneRepFormulaUseCase.Workout,
            Pair<MutableLiveData<String>, MutableLiveData<String>>
            >(
        OneRepFormulaUseCase.Workout.DEAD_LIFT to (MutableLiveData("") to MutableLiveData("")),
        OneRepFormulaUseCase.Workout.SQUAT to (MutableLiveData("") to MutableLiveData("")),
        OneRepFormulaUseCase.Workout.BENCH_PRESS to (MutableLiveData("") to MutableLiveData(""))
    )

    fun getWeight(workout: OneRepFormulaUseCase.Workout): MutableLiveData<String> {
        return edits[workout]!!.first
    }

    fun getRepeat(workout: OneRepFormulaUseCase.Workout): MutableLiveData<String> {
        return edits[workout]!!.second
    }

    fun setWorkout(workout: OneRepFormulaUseCase.Workout) {
        _currentWorkout.value = workout
    }

    // 다음 버튼 누를 때마다, 상태에 따라서 다른 행동을 해야함.
    // 다음 버튼은 상태에 따라서 시작하기 텍스트를 보여 줘야함.
    // 다음 버튼을 누를 때, 캐시에 있는 값을 실제 값으로 세팅해주어야한다.
    fun next() {
        val workout = _currentWorkout.value

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
    }

    private fun save() {
        val goalKg = goal.value.toKgOrNull() ?: return
        userSettingUseCase.setGoal(goalKg)

        for ((workout, value) in edits) {
            val weight = value.first.value.toKgOrNull() ?: 0.0
            val repeat = value.second.value?.toIntOrNull() ?: 0

            oneRepFormulaUseCase.setWeight(workout, weight)
            oneRepFormulaUseCase.setRepeat(workout, repeat)

            Timber.d("save $workout $weight kg $repeat reps")
        }
    }
}
