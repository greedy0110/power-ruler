package com.greedy0110.powerruler.feature.onerep.update

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.greedy0110.powerruler.R
import com.greedy0110.powerruler.databinding.DialogUpdateOnerepBinding
import com.greedy0110.powerruler.domain.toKgOrNull
import com.greedy0110.powerruler.usecase.OneRepFormulaUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UpdateDialogFragment : DialogFragment() {

    private val viewModel: UpdateViewModel by viewModels()

    @Inject
    lateinit var oneRepFormulaUseCase: OneRepFormulaUseCase

    lateinit var onConfirm: () -> Unit
    lateinit var workout: OneRepFormulaUseCase.Workout

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val binding = DialogUpdateOnerepBinding.inflate(layoutInflater, null, false)

            binding.buttonConfirm.setOnClickListener {
                val weight = binding.editWeight.text.toKgOrNull()
                val repeat = binding.editRepeat.text.toString().toIntOrNull()

                if (weight == null || repeat == null) {
                    Toast.makeText(context, R.string.system_needed, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                oneRepFormulaUseCase.setWeight(workout, weight)
                oneRepFormulaUseCase.setRepeat(workout, repeat)

                onConfirm()
                dismiss()
            }

            binding.lifecycleOwner = this
            binding.viewModel = viewModel

            viewModel.setWorkout(workout)

            return AlertDialog.Builder(it)
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

            newFragment.onConfirm = onConfirmButton
            newFragment.workout = workout

            newFragment.show(fm, tag)
        }
    }
}