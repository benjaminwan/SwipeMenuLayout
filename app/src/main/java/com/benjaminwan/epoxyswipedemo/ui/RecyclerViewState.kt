package com.benjaminwan.epoxyswipedemo.ui

import com.airbnb.mvrx.MavericksState

data class RecyclerViewState(
    val demos: List<String> = emptyList(),
) : MavericksState