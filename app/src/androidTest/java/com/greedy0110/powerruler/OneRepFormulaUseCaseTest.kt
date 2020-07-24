package com.greedy0110.powerruler

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import com.greedy0110.powerruler.domain.OneRepMaxFormula
import com.greedy0110.powerruler.domain.OneRepMaxFormulaEpley
import com.greedy0110.powerruler.feature.onerep.OneRepFormulaUseCase
import com.greedy0110.powerruler.repository.EverythingRepository
import com.greedy0110.powerruler.repository.MemoryEverythingRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class OneRepFormulaUseCaseTest {
    private lateinit var appContext: Context
    private lateinit var oneRepFormulaUseCase: OneRepFormulaUseCase
    private lateinit var everythingRepository: EverythingRepository
    private lateinit var oneRepFormula: OneRepMaxFormula

    @Before
    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        everythingRepository = MemoryEverythingRepository()
        oneRepFormula = OneRepMaxFormulaEpley()

        oneRepFormulaUseCase = OneRepFormulaUseCase(
            appContext,
            everythingRepository,
            oneRepFormula
        )

        runBlocking {
            oneRepFormulaUseCase.setWeight(OneRepFormulaUseCase.Workout.DEAD_LIFT, 140.0)
            oneRepFormulaUseCase.setRepeat(OneRepFormulaUseCase.Workout.DEAD_LIFT, 2)

            oneRepFormulaUseCase.setWeight(OneRepFormulaUseCase.Workout.SQUAT, 100.0)
            oneRepFormulaUseCase.setRepeat(OneRepFormulaUseCase.Workout.SQUAT, 10)

            oneRepFormulaUseCase.setWeight(OneRepFormulaUseCase.Workout.BENCH_PRESS, 80.0)
            oneRepFormulaUseCase.setRepeat(OneRepFormulaUseCase.Workout.BENCH_PRESS, 10)
        }
    }

    @Test
    fun oneRepMaxFormula() {

        Truth.assertThat(oneRepFormulaUseCase.getOneRepMax()).isNaN()
    }
}