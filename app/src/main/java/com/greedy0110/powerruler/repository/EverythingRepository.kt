package com.greedy0110.powerruler.repository

import com.greedy0110.powerruler.domain.Kg

typealias WorkoutKey = String

//WHAT: 개인 앱이니 하나로 ㅎ;
interface EverythingRepository {

    suspend fun getWeight(workout: WorkoutKey): Kg?

    suspend fun setWeight(workout: WorkoutKey, weight: Kg)

    suspend fun getRepeat(workout: WorkoutKey): Int?

    suspend fun setRepeat(workout: WorkoutKey, repeat: Int)

    suspend fun getGoal(): Kg?

    suspend fun setGoal(goal: Kg)
}
