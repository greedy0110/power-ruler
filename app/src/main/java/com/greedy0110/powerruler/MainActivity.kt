package com.greedy0110.powerruler

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.greedy0110.powerruler.databinding.ActivityMainBinding
import com.greedy0110.powerruler.databinding.ItemHeaderBinding
import com.greedy0110.powerruler.databinding.ItemWorkoutBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val adapter by lazy { Adapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initList()
    }

    private fun initList() {
        binding.listWorkout.adapter = adapter
        binding.listWorkout.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        //TODO: 문자열 리소스로 만들기, ViewModel 로 빼기
        adapter.submitList(
            emptySequence<ItemHolder>()
                .plus(ItemHolder.Header)
                .plus(ItemHolder.Workout("데드리프트"))
                .plus(ItemHolder.Workout("스쿼트"))
                .plus(ItemHolder.Workout("밴치프레스"))
                .toList()
        )
    }

    //region RecyclerView.

    private inner class Adapter : ListAdapter<ItemHolder, RecyclerView.ViewHolder>(
        object : DiffUtil.ItemCallback<ItemHolder>() {
            override fun areItemsTheSame(oldItem: ItemHolder, newItem: ItemHolder): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: ItemHolder, newItem: ItemHolder): Boolean =
                oldItem == newItem
        }
    ) {

        override fun getItemViewType(position: Int): Int {
            return when (getItem(position)) {
                is ItemHolder.Header -> 0
                is ItemHolder.Workout -> 1
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                0 -> HeaderViewHolder(ItemHeaderBinding.inflate(layoutInflater, parent, false))
                1 -> WorkoutViewHolder(ItemWorkoutBinding.inflate(layoutInflater, parent, false))
                else -> throw IllegalArgumentException("뭔가 이상함.")
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is HeaderViewHolder -> Unit
                is WorkoutViewHolder -> holder.onBind(getItem(position) as ItemHolder.Workout)
            }
        }
    }

    private class HeaderViewHolder(val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    private class WorkoutViewHolder(val binding: ItemWorkoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val context = itemView.context

        fun onBind(item: ItemHolder.Workout) {
            binding.name.text = item.name

            //TODO: 스피너 세팅
            val spinnerItems = List(15) { it + 1 }
            val spinnerAdapter = ArrayAdapter<Int>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerItems
            )

            binding.spinnerRepeat.adapter = spinnerAdapter
            binding.spinnerRepeat.setSelection(12)

            //TODO: 이거로 변경된 값을 넘기기
        }
    }

    sealed class ItemHolder {
        object Header : ItemHolder()
        data class Workout(val name: String) : ItemHolder()
    }

    //endregion
}