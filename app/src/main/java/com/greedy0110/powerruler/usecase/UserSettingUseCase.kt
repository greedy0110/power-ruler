package com.greedy0110.powerruler.usecase

import com.greedy0110.powerruler.domain.Kg
import com.greedy0110.powerruler.repository.EverythingRepository
import kotlinx.coroutines.runBlocking
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
}
