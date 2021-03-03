package com.benjaminwan.epoxyswipedemo.ui

import com.airbnb.mvrx.MavericksState

data class DemoState(
    val demos: List<String> = emptyList(),
) : MavericksState