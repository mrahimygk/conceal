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


inline fun <T1, T2, T3, T4, R> combine(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    source4: LiveData<T4>,
    crossinline combine: (data1: T1?, data2: T2?, data3: T3?, data4: T4?) -> R
): LiveData<R> = MediatorLiveData<R>().apply {

    var data1: T1? = null
    var data2: T2? = null
    var data3: T3? = null
    var data4: T4? = null

    addSource(source1) {
        data1 = it
        value = combine(data1, data2, data3, data4)
    }

    addSource(source2) {
        data2 = it
        value = combine(data1, data2, data3, data4)
    }

    addSource(source3) {
        data3 = it
        value = combine(data1, data2, data3, data4)
    }

    addSource(source4) {
        data4 = it
        value = combine(data1, data2, data3, data4)
    }

}


inline fun <T1, T2, T3, T4, T5, R> combine(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    source4: LiveData<T4>,
    source5: LiveData<T5>,
    crossinline combine: (data1: T1?, data2: T2?, data3: T3?, data4: T4?, data5: T5?) -> R
): LiveData<R> = MediatorLiveData<R>().apply {

    var data1: T1? = null
    var data2: T2? = null
    var data3: T3? = null
    var data4: T4? = null
    var data5: T5? = null

    addSource(source1) {
        data1 = it
        value = combine(data1, data2, data3, data4, data5)
    }

    addSource(source2) {
        data2 = it
        value = combine(data1, data2, data3, data4, data5)
    }

    addSource(source3) {
        data3 = it
        value = combine(data1, data2, data3, data4, data5)
    }

    addSource(source4) {
        data4 = it
        value = combine(data1, data2, data3, data4, data5)
    }
    addSource(source5) {
        data5 = it
        value = combine(data1, data2, data3, data4, data5)
    }

}

