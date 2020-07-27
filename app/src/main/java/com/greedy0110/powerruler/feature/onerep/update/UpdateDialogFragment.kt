package com.greedy0110.powerruler.feature.onerep.update

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.greedy0110.powerruler.databinding.DialogUpdateOnerepBinding
import com.greedy0110.powerruler.usecase.OneRepFormulaUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UpdateDialogFragment : DialogFragment() {

    private val viewModel: UpdateViewModel by viewModels()

    @Inject
    lateinit var oneRepFormulaUseCase: OneRepFormulaUseCase

    lateinit var onComplete: () -> Unit
    lateinit var workout: OneRepFormulaUseCase.Workout

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            val binding = DialogUpdateOnerepBinding.inflate(layoutInflater, null, false)

            binding.lifecycleOwner = this
            binding.viewModel = viewModel

            viewModel.setWorkout(workout)

            viewModel.signal.observe(this) {
                when (it) {
                    UpdateSignal.CompleteSignal -> {
                        onComplete()
                        dismiss()
                    }
                }
            }

            return AlertDialog.Builder(activity)
                .setView(binding.root)
                .create()
        } ?: throw IllegalStateException("${UpdateDialogFragment::class.simpleName} creation error")
    }

    class Builder(private val workout: OneRepFormulaUseCase.Workout) {
        private var onConfirmButton: () -> Unit = {}

        fun setOnConfirmButton(onConfirm: () -> Unit) = apply {
            onConfirmButton = onConfirm
        }

        fun show(
            fm: FragmentManager,
            tag: String = UpdateDialogFragment::class.simpleName ?: "power"
        ) {
            val newFragment = UpdateDialogFragment()

            newFragment.onComplete = onConfirmButton
            newFragment.workout = workout

            newFragment.show(fm, tag)
        }
    }
}