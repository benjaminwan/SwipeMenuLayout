package com.benjaminwan.epoxyswipedemo.ui.viewmodel

import android.content.Context
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.benjaminwan.epoxyswipedemo.utils.copyRemove
import com.benjaminwan.epoxyswipedemo.utils.copySwap

class RecyclerViewEpoxyViewModel(
    initialState: RecyclerViewEpoxyState,
    private val context: Context
) : MavericksViewModel<RecyclerViewEpoxyState>(initialState) {

    init {

    }

    fun setDemos(demos: List<String>) {
        setState {
            copy(demos = demos)
        }
    }

    fun initDemos() {
        setState {
            val demos = (0..99).map { "Test Drag & SwipeMenu Item $it" }
            copy(demos = demos)
        }
    }

    fun remove(content: String) {
        setState {
            copy(demos = demos.copyRemove(content))
        }
    }

    fun clear() {
        setState {
            copy(demos = emptyList())
        }
    }

    fun swap(fromPosition: Int, toPosition: Int) {
        setState {
            copy(demos = demos.copySwap(fromPosition, toPosition))
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    companion object : MavericksViewModelFactory<RecyclerViewEpoxyViewModel, RecyclerViewEpoxyState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: RecyclerViewEpoxyState
        ): RecyclerViewEpoxyViewModel {
            val context = viewModelContext.activity.applicationContext
            return RecyclerViewEpoxyViewModel(state, context)
        }
    }

}