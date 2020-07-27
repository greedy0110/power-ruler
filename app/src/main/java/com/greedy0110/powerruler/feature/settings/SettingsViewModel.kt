package com.greedy0110.powerruler.feature.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.greedy0110.powerruler.domain.toKgOrNull
import com.greedy0110.powerruler.usecase.UserSettingUseCase

class SettingsViewModel @ViewModelInject constructor(
    private val useCase: UserSettingUseCase
) : ViewModel() {

    val goal: LiveData<String> = MutableLiveData(useCase.getGoal()?.toInt().toString())
    val currentGoal: MutableLiveData<String> = MutableLiveData("")

    private val _signal: MutableLiveData<SettingsSignal> = MutableLiveData()
    val signal: LiveData<SettingsSignal> = _signal

    fun update() {
        val goal = currentGoal.value?.toKgOrNull()

        if (goal != null) {
            useCase.setGoal(goal)
            _signal.value = SettingsSignal.UpdateComplete
        } else {
            _signal.value = SettingsSignal.WrongUpdate
        }
    }
}