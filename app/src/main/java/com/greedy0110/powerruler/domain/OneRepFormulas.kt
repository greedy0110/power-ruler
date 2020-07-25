package com.greedy0110.powerruler.domain

import javax.inject.Inject
import kotlin.math.pow

// 꺼무위키 참조 https://namu.wiki/w/1RM

class OneRepMaxFormulaEpley @Inject constructor() : OneRepMaxFormula {

    override fun oneRepKg(weight: Kg, repeat: Int): Kg {
        if (weight <= 0 || repeat <= 0) return 0.0

        return weight * (1 + repeat / 30.toFloat())
    }
}

class OneRepMaxFormulaBrzycki @Inject constructor() : OneRepMaxFormula {

    override fun oneRepKg(weight: Kg, repeat: Int): Kg {
        if (weight <= 0 || repeat <= 0) return 0.0

        return weight * (36 / (37 - repeat).toFloat())
    }
}

class OneRepMaxFormulaMcGlothin @Inject constructor() : OneRepMaxFormula {

    override fun oneRepKg(weight: Kg, repeat: Int): Kg {
        if (weight <= 0 || repeat <= 0) return 0.0

        return 100 * weight / (101.3 - 2.67123 * repeat)
    }
}

class OneRepMaxFormulaLombardi @Inject constructor() : OneRepMaxFormula {

    override fun oneRepKg(weight: Kg, repeat: Int): Kg {
        if (weight <= 0 || repeat <= 0) return 0.0

        return 100 * repeat.toDouble().pow(0.1)
    }
}
