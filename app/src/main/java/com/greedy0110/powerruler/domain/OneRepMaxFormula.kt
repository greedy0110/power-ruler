package com.greedy0110.powerruler.domain

typealias Kg = Double

interface OneRepMaxFormula {

    fun oneRepKg(weight: Kg, repeat: Int): Kg
}