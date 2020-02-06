package ir.mrahimy.conceal.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

inline fun <T1, T2, R> combine(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    crossinline combine: (data1: T1?, data2: T2?) -> R
): LiveData<R> = MediatorLiveData<R>().apply {

    var data1: T1? = null
    var data2: T2? = null

    addSource(source1) {
        data1 = it
        value = combine(data1, data2)
    }

    addSource(source2) {
        data2 = it
        value = combine(data1, data2)
    }

}

inline fun <T1, T2, T3, R> combine(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    crossinline combine: (data1: T1?, data2: T2?, data3: T3?) -> R
): LiveData<R> = MediatorLiveData<R>().apply {

    var data1: T1? = null
    var data2: T2? = null
    var data3: T3? = null

    addSource(source1) {
        data1 = it
        value = combine(data1, data2, data3)
    }

    addSource(source2) {
        data2 = it
        value = combine(data1, data2, data3)
    }

    addSource(source3) {
        data3 = it
        value = combine(data1, data2, data3)
    }

}

