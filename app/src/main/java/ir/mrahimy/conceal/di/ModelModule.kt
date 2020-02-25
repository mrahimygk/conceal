package ir.mrahimy.conceal.di

import ir.mrahimy.conceal.ui.home.MainActivityModel
import ir.mrahimy.conceal.ui.parse.ParseActivityModel
import ir.mrahimy.conceal.ui.sample.SampleModel
import ir.mrahimy.conceal.ui.slide.SlideShowModel
import org.koin.dsl.module

val modelModule = module {
    factory { SampleModel() }
    factory { MainActivityModel(get(), get()) }
    factory { ParseActivityModel() }
    factory { SlideShowModel() }

}