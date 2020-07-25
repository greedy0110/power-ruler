package com.greedy0110.powerruler.feature.init

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.greedy0110.powerruler.databinding.FragmentGoalBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoalFragment : Fragment() {

    private val binding by lazy { FragmentGoalBinding.inflate(layoutInflater) }
    private val viewModel: InitViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
}
