package com.greedy0110.powerruler.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

data class Tuple4<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

fun <A, B> combineLiveData2(
    a: LiveData<A>,
    b: LiveData<B>
): LiveData<Pair<A, B>> {
    return MediatorLiveData<Pair<A, B>>().apply {
        var lastA: A? = null
        var lastB: B? = null

        fun update() {
            val localLastA = lastA
            val localLastB = lastB

            if (localLastA == null ||
                localLastB == null
            ) return

            this.value = Pair(localLastA, localLastB)
        }

        addSource(a) {
            lastA = it
            update()
        }
        addSource(b) {
            lastB = it
            update()
        }
    }
}

fun <A, B, C, D> combineLiveData4(
    a: LiveData<A>,
    b: LiveData<B>,
    c: LiveData<C>,
    d: LiveData<D>
): LiveData<Tuple4<A, B, C, D>> {
    return MediatorLiveData<Tuple4<A, B, C, D>>().apply {
        var lastA: A? = null
        var lastB: B? = null
        var lastC: C? = null
        var lastD: D? = null

        fun update() {
            val localLastA = lastA
            val localLastB = lastB
            val localLastC = lastC
            val localLastD = lastD

            if (localLastA == null ||
                localLastB == null ||
                localLastC == null ||
                localLastD == null
            ) return

            this.value = Tuple4(localLastA, localLastB, localLastC, localLastD)
        }

        addSource(a) {
            lastA = it
            update()
        }
        addSource(b) {
            lastB = it
            update()
        }
        addSource(c) {
            lastC = it
            update()
        }
        addSource(d) {
            lastD = it
            update()
        }
    }
}
