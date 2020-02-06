package ir.mrahimy.conceal.di

import ir.mrahimy.conceal.ui.sample.SampleAdapter
import org.koin.dsl.module

val adapterModule = module {
    factory { SampleAdapter() }
}