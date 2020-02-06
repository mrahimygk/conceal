package ir.mrahimy.conceal.di

import ir.mrahimy.conceal.ui.home.MainActivityViewModel
import ir.mrahimy.conceal.ui.sample.SampleViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SampleViewModel(get()) }
    viewModel { MainActivityViewModel(androidApplication(), get()) }
}