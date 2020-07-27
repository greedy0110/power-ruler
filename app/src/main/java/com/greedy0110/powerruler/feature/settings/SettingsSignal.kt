package com.greedy0110.powerruler.feature.settings

sealed class SettingsSignal {
    object UpdateComplete: SettingsSignal()
    object WrongUpdate: SettingsSignal()
}