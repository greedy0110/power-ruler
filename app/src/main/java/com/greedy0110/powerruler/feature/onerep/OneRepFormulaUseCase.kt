package com.greedy0110.powerruler.feature.onerep

import android.content.Context
import com.greedy0110.powerruler.R
import com.greedy0110.powerruler.domain.Kg
import com.greedy0110.powerruler.domain.OneRepMaxFormula
import com.greedy0110.powerruler.repository.EverythingRepository
import com.greedy0110.powerruler.repository.WorkoutKey
import kotlinx.coroutines.runBlocking

class OneRepFormulaUseCase(
    private val context: Context,
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

    enum class Workout(val uniqueName: WorkoutKey) {
        DEAD_LIFT("onerep_dead_lift"),
        SQUAT("onerep_squat"),
        BENCH_PRESS("onerep_bench_press")
    }
}