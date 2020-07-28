package com.greedy0110.powerruler.repository

import android.content.Context
import androidx.core.content.edit
import com.greedy0110.powerruler.domain.Kg
import com.greedy0110.powerruler.domain.toKg
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceEverythingRepository @Inject constructor(
    @ApplicationContext private val appContext: Context
) : EverythingRepository {

    val name = "${appContext.packageName}_PreferenceEverythingRepository"
    private val sharedPreferences by lazy {
        appContext.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    override suspend fun getWeight(workout: WorkoutKey): Kg? {
        val weight = sharedPreferences.getFloat("${workout}${WEIGHT}", -1f)
        if (weight == -1f) return null

        return weight.toKg()
    }

    override suspend fun setWeight(workout: WorkoutKey, weight: Kg) {
        sharedPreferences.edit {
            putFloat("${workout}${WEIGHT}", weight.toFloat())
        }
    }

    override suspend fun getRepeat(workout: WorkoutKey): Int? {
        val repeat = sharedPreferences.getInt("${workout}${REPEAT}", -1)
        if (repeat == -1) return null

        return repeat
    }

    override suspend fun setRepeat(workout: WorkoutKey, repeat: Int) {
        sharedPreferences.edit {
            putInt("${workout}${REPEAT}", repeat)
        }
    }

    override suspend fun getGoal(): Kg? {
        val weight = sharedPreferences.getFloat(GOAL, -1f)
        if (weight == -1f) return null

        return weight.toKg()
    }

    override suspend fun setGoal(goal: Kg) {
        sharedPreferences.edit {
            putFloat(GOAL, goal.toFloat())
        }
    }

    override suspend fun setAdNotice(time: Long) {
        sharedPreferences.edit {
            putLong(AD_NOTICED, time)
        }
    }

    override suspend fun getAdNotice(): Long {
        return sharedPreferences.getLong(AD_NOTICED, 0L)
    }

    private companion object {
        const val WEIGHT = "weight"
        const val REPEAT = "repeat"
        const val GOAL = "goal"
        const val AD_NOTICED = "ad_noticed"
    }
}