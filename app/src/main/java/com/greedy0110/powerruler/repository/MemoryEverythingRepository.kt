package com.greedy0110.powerruler.repository

import com.greedy0110.powerruler.domain.Kg
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoryEverythingRepository @Inject constructor() : EverythingRepository {

    private val weightCache = mutableMapOf<WorkoutKey, Kg>()
    private val repeatCache = mutableMapOf<WorkoutKey, Int>()
    private var goal: Kg? = null

    override suspend fun getWeight(workout: WorkoutKey): Kg? {
        return weightCache[workout]
    }

    override suspend fun setWeight(workout: WorkoutKey, weight: Kg) {
        weightCache[workout] = weight
    }

    override suspend fun getRepeat(workout: WorkoutKey): Int? {
        return repeatCache[workout]
    }

    override suspend fun setRepeat(workout: WorkoutKey, repeat: Int) {
        repeatCache[workout] = repeat
    }

    override suspend fun getGoal(): Kg? {
        return goal
    }

    override suspend fun setGoal(goal: Kg) {
        this.goal = goal
    }
}
