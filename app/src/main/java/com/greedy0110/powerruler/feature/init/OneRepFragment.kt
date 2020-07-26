package com.greedy0110.powerruler.feature.init

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.greedy0110.powerruler.databinding.FragmentOneRepBinding
import com.greedy0110.powerruler.usecase.OneRepFormulaUseCase
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_WORKOUT_ENUM_ORDINAL = "WORKOUT_ENUM"

@AndroidEntryPoint
class OneRepFragment : Fragment() {
    private var workout: OneRepFormulaUseCase.Workout? = null
    private lateinit var binding: FragmentOneRepBinding
    private val viewModel: InitViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            workout = OneRepFormulaUseCase.Workout.values()[bundle.getInt(ARG_WORKOUT_ENUM_ORDINAL)]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOneRepBinding.inflate(layoutInflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(workoutOrdinal: Int) =
            OneRepFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_WORKOUT_ENUM_ORDINAL, workoutOrdinal)
                }
            }
    }
}
