package com.greedy0110.powerruler.feature.onerep

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.greedy0110.powerruler.databinding.ActivityOneRepMaxBinding
import com.greedy0110.powerruler.databinding.ItemOnerepWorkoutBinding
import com.greedy0110.powerruler.feature.onerep.update.UpdateDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OneRepMaxActivity : AppCompatActivity() {

    private val binding by lazy { ActivityOneRepMaxBinding.inflate(layoutInflater) }
    private val viewModel: OneRepMaxViewModel by viewModels()
    private val adapter by lazy { Adapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initList()
    }

    private fun initList() {
        binding.listWorkout.adapter = adapter
        binding.listWorkout.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        viewModel.items.observe(this) {
            adapter.submitList(it)
        }
    }

    //region RecyclerView.

    private inner class Adapter : ListAdapter<OneRepMaxViewModel.ItemHolder2, ViewHolder>(
        object : DiffUtil.ItemCallback<OneRepMaxViewModel.ItemHolder2>() {
            override fun areItemsTheSame(
                oldItem: OneRepMaxViewModel.ItemHolder2,
                newItem: OneRepMaxViewModel.ItemHolder2
            ): Boolean = oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: OneRepMaxViewModel.ItemHolder2,
                newItem: OneRepMaxViewModel.ItemHolder2
            ): Boolean = oldItem == newItem
        }
    ) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(ItemOnerepWorkoutBinding.inflate(layoutInflater, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.onBind(getItem(position))
        }
    }

    private inner class ViewHolder(
        private val binding: ItemOnerepWorkoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: OneRepMaxViewModel.ItemHolder2) {
            binding.itemHolder = item

            binding.root.setOnClickListener {
                UpdateDialogFragment.Builder(item.workout)
                    .setOnConfirmButton {
                        //TODO: 1RM 갱신
                        viewModel.refresh()
                        Timber.d("${item.workout} 클릭")
                    }
                    .show(supportFragmentManager)
            }
        }
    }

    //endregion
}
