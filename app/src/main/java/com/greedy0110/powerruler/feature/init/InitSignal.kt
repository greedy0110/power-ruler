package com.greedy0110.powerruler.feature.init

import com.greedy0110.powerruler.usecase.OneRepFormulaUseCase

sealed class InitSignal {
    data class WorkoutSignal(val workout: OneRepFormulaUseCase.Workout) : InitSignal()
    object StartSignal : InitSignal()
}
