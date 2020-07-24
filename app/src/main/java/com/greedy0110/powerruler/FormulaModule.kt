package com.greedy0110.powerruler

import com.greedy0110.powerruler.domain.OneRepMaxFormula
import com.greedy0110.powerruler.domain.OneRepMaxFormulaEpley
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class FormulaModule {

    @Binds
    abstract fun bindsOneRepFormulas(oneRepMaxFormula: OneRepMaxFormulaEpley): OneRepMaxFormula
}