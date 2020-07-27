package com.greedy0110.powerruler.feature.onerep.update

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.greedy0110.powerruler.R
import com.greedy0110.powerruler.domain.toKgOrNull
import com.greedy0110.powerruler.usecase.OneRepFormulaUseCase
import com.greedy0110.powerruler.util.combineLiveData4
import dagger.hilt.android.qualifiers.ApplicationContext

class UpdateViewModel @ViewModelInject constructor(
    @ApplicationContext private val appContext: Context,
    private val oneRepFormulaUseCase: OneRepFormulaUseCase
) : ViewModel() {

    private val _workout: MutableLiveData<OneRepFormulaUseCase.Workout> = MutableLiveData()
    val workoutName: LiveData<String> = _workout.map { appContext.getString(it.stringResId) }
    private val unit = appContext.getString(R.string.unit_kg)

    val weight: LiveData<String> = _workout
        .map { oneRepFormulaUseCase.getWeight(it) ?: 0.0 }
        .map { it.toInt().toString() }

    val reps: LiveData<String> = _workout
        .map { oneRepFormulaUseCase.getRepeat(it) ?: 0 }
        .map { it.toString() }

    val currentWeight: MutableLiveData<String> = MutableLiveData("")
    val currentRepeat: MutableLiveData<String> = MutableLiveData("")

    val updateEnabled = combineLiveData4(weight, currentWeight, reps, currentRepeat)
        .map { (preW, curW, preR, curR) ->
            !(curW.isBlank() && curR.isBlank()) && (preW != curW || preR != curR)
        }

    private val onerepPreview = combineLiveData4(weight, currentWeight, reps, currentRepeat)
        .map { (weight, currentWeight, reps, currentRepeat) ->
            val localWeight = currentWeight.toKgOrNull() ?: weight.toKgOrNull() ?: 0.0
            val localRepeat =
                currentRepeat.toString().toIntOrNull() ?: reps.toIntOrNull() ?: 0

            oneRepFormulaUseCase.getOneRepMaxPreview(localWeight, localRepeat)
        }

    val oneRepPreviewView = onerepPreview
        .map { preview ->
            appContext.getString(R.string.onerep_max_prev, preview.toInt().toString(), unit)
        }

    val cheerUpView = onerepPreview
        .map { preview ->
            val onerep = oneRepFormulaUseCase.getOneRepMaxBy(_workout.value!!)

            val diff = preview - onerep
            when {
                diff > 0 -> appContext.getString(
                    R.string.onerep_upgrade,
                    diff.toInt().toString(),
                    unit
                )
                diff < 0 -> appContext.getString(
                    R.string.onerep_downgrade,
                    (-1 * diff).toInt().toString(),
                    unit
                )
                else -> ""
            }
        }

    val updateButtonView = onerepPreview
        .map { preview ->
            val onerep = oneRepFormulaUseCase.getOneRepMaxBy(_workout.value!!)

            val diff = preview - onerep
            when {
                diff > 0 -> appContext.getString(R.string.upgrade_onerep_button)
                diff < 0 -> appContext.getString(R.string.downgrade_onerep_button)
                else -> appContext.getString(R.string.upgrade_onerep_button)
            }
        }

    private val _signal: MutableLiveData<UpdateSignal> = MutableLiveData()
    val signal: LiveData<UpdateSignal> = _signal

    fun setWorkout(workout: OneRepFormulaUseCase.Workout) {
        _workout.value = workout
    }

    fun update() {
        val workout = _workout.value
        requireNotNull(workout)

        val weight = currentWeight.value.toKgOrNull() ?: weight.value.toKgOrNull() ?: 0.0
        val repeat =
            currentRepeat.value.toString().toIntOrNull() ?: reps.value.toString().toIntOrNull() ?: 0

        oneRepFormulaUseCase.setWeight(workout, weight)
        oneRepFormulaUseCase.setRepeat(workout, repeat)

        _signal.value = UpdateSignal.CompleteSignal
    }
}
