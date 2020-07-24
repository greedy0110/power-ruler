package com.greedy0110.powerruler.repository

import com.greedy0110.powerruler.domain.Kg

class MemoryEverythingRepository : EverythingRepository {

    private val weightCache = mutableMapOf<WorkoutKey, Kg>()
    private val repeatCache = mutableMapOf<WorkoutKey, Int>()

    override suspend fun getName(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun setName(name: String) {
        TODO("Not yet implemented")
    }

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
}