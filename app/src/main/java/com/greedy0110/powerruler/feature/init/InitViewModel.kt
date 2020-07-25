package com.greedy0110.powerruler.feature.init

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class InitViewModel : ViewModel() {

    val goal = MutableLiveData<String>()
    val confirmEnabled = goal.map { it.isNotBlank() }
}