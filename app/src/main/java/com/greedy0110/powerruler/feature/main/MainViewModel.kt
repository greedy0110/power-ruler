package com.greedy0110.powerruler.feature.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class MainViewModel : ViewModel() {

    private val _workout: MutableLiveData<List<ItemHolder.Workout>> = MutableLiveData(
        listOf(
            ItemHolder.Workout("데드리프트"),
            ItemHolder.Workout("스쿼트"),
            ItemHolder.Workout("밴치프레스")
        )
    )

    val items: LiveData<List<ItemHolder>> = _workout.map {
        emptySequence<ItemHolder>()
            .plus(ItemHolder.Header)
            .plus(it.asSequence())
            .toList()
    }

    val enableConfirm = _workout.map { list ->
        list.isNotEmpty() && list.all { it.repeat != null && it.weight != null }
    }

    sealed class ItemHolder {
        object Header : ItemHolder()
        data class Workout(
            val name: String,
            val weight: Int? = null,
            val repeat: Int? = null
        ) : ItemHolder()
    }
}