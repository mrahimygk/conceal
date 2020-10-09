package ir.mrahimy.conceal.di

import ir.mrahimy.conceal.ui.home.MainActivityViewModel
import ir.mrahimy.conceal.ui.parse.ParseActivityViewModel
import ir.mrahimy.conceal.ui.sample.SampleViewModel
import ir.mrahimy.conceal.ui.slide.SlideShowViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SampleViewModel(get()) }
    viewModel { MainActivityViewModel(androidApplication(), get(), get()) }
    viewModel { ParseActivityViewModel(androidApplication(), get(), get()) }
    viewModel { SlideShowViewModel(androidApplication()) }
}