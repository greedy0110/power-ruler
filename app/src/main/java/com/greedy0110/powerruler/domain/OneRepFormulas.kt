package com.greedy0110.powerruler.domain

import kotlin.math.pow

// 꺼무위키 참조 https://namu.wiki/w/1RM

class OneRepMaxFormulaEpley : OneRepMaxFormula {

    override fun oneRepKg(weight: Kg, repeat: Int): Kg {
        return weight * (1 + repeat / 30.toFloat())
    }
}

class OneRepMaxFormulaBrzycki : OneRepMaxFormula {

    override fun oneRepKg(weight: Kg, repeat: Int): Kg {
        return weight * (36 / (37 - repeat).toFloat())
    }
}

class OneRepMaxFormulaMcGlothin : OneRepMaxFormula {

    override fun oneRepKg(weight: Kg, repeat: Int): Kg {
        return 100 * weight / (101.3 - 2.67123 * repeat)
    }
}

class OneRepMaxFormulaLombardi : OneRepMaxFormula {

    override fun oneRepKg(weight: Kg, repeat: Int): Kg {
        return 100 * repeat.toDouble().pow(0.1)
    }
}
