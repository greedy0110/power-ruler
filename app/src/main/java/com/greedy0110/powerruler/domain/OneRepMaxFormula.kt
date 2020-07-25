package com.greedy0110.powerruler.domain

typealias Kg = Double

fun Any?.toKg(): Kg = this.toString().toDouble()
fun Any?.toKgOrNull(): Kg? = this.toString().toDoubleOrNull()

interface OneRepMaxFormula {

    fun oneRepKg(weight: Kg, repeat: Int): Kg
}