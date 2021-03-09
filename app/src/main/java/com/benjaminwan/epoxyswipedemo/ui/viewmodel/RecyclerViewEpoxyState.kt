package com.benjaminwan.epoxyswipedemo.ui.viewmodel

import com.airbnb.mvrx.MavericksState

data class RecyclerViewEpoxyState(
    val demos: List<String> = emptyList(),
) : MavericksState