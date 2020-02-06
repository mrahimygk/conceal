package ir.mrahimy.conceal.di

import ir.mrahimy.conceal.ui.sample.SampleModel
import org.koin.dsl.module

val modelModule = module {
    factory { SampleModel() }
}