package com.greedy0110.powerruler.usecase

import android.content.Context
import androidx.annotation.StringRes
import com.greedy0110.powerruler.R
import com.greedy0110.powerruler.domain.Kg
import com.greedy0110.powerruler.domain.OneRepMaxFormula
import com.greedy0110.powerruler.repository.EverythingRepository
import com.greedy0110.powerruler.repository.WorkoutKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

//WHAT: EverythingRepository 가 변경 되었는데, useCase 는 단발성으로 값을 얻음.
// 따라서 Repository 의 변경이 최신 반영이 안됌.

class OneRepFormulaUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val everythingRepository: EverythingRepository,
    private val oneRepMaxFormula: OneRepMaxFormula
) {

    fun setWeight(workout: Workout, weight: Kg) = runBlocking {
        everythingRepository.setWeight(workout.uniqueName, weight)
    }

    fun getWeight(workout: Workout) = runBlocking {
        everythingRepository.getWeight(workout.uniqueName)
    }

    fun setRepeat(workout: Workout, repeat: Int) = runBlocking {
        everythingRepository.setRepeat(workout.uniqueName, repeat)
    }

    fun getRepeat(workout: Workout) = runBlocking {
        everythingRepository.getRepeat(workout.uniqueName)
    }

    fun getOneRepMaxBy(workout: Workout): Kg = runBlocking {
        val weight = everythingRepository.getWeight(workout.uniqueName)
            ?: throw IllegalAccessException(context.getString(R.string.system_error))
        val repeat = everythingRepository.getRepeat(workout.uniqueName)
            ?: throw IllegalAccessException(context.getString(R.string.system_error))

        oneRepMaxFormula.oneRepKg(weight, repeat)
    }

    fun getOneRepMax(): Kg = runBlocking {
        Workout.values()
            .map { workout -> getOneRepMaxBy(workout) }
            .sum()
    }

    fun getOneRepMaxPreview(weight: Kg, repeat: Int): Kg {
        return oneRepMaxFormula.oneRepKg(weight, repeat)
    }

    enum class Workout(val uniqueName: WorkoutKey, @StringRes val stringResId: Int) {
        DEAD_LIFT("onerep_dead_lift", R.string.dead_lift),
        SQUAT("onerep_squat", R.string.squat),
        BENCH_PRESS("onerep_bench_press", R.string.bench_press)
    }
}