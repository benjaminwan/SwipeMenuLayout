package com.benjaminwan.epoxyswipedemo.ui

import android.content.Context
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext

class DemoViewModel(
    initialState: DemoState,
    private val context: Context
) : MavericksViewModel<DemoState>(initialState) {

    init {

    }

    fun setDemos(demos: List<String>) {
        setState {
            copy(demos = demos)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    companion object : MavericksViewModelFactory<DemoViewModel, DemoState> {
        override fun create(viewModelContext: ViewModelContext, state: DemoState): DemoViewModel {
            val context = viewModelContext.activity.applicationContext
            return DemoViewModel(state, context)
        }
    }

}