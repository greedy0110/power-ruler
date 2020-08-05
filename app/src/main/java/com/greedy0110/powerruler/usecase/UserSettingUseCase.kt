package com.greedy0110.powerruler.usecase

import com.greedy0110.powerruler.domain.Kg
import com.greedy0110.powerruler.repository.EverythingRepository
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UserSettingUseCase @Inject constructor(
    private val everythingRepository: EverythingRepository
) {

    fun setGoal(goal: Kg) = runBlocking {
        everythingRepository.setGoal(goal)
    }

    fun getGoal(): Kg? = runBlocking {
        everythingRepository.getGoal()
    }

    fun needShowAd(): Boolean = runBlocking {
        val lastNoticed = everythingRepository.getAdNotice()
        val diff = System.currentTimeMillis() - lastNoticed
        val coolTime = TimeUnit.SECONDS.toMillis(30)

        Timber.d("$diff, $coolTime, $lastNoticed")

        diff > coolTime
    }

    fun showAd() = runBlocking {
        everythingRepository.setAdNotice(System.currentTimeMillis())
    }
}
