package com.benjaminwan.epoxyswipedemo.utils

import androidx.fragment.app.Fragment
import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.withState

class MavericksEpoxyController(
    val buildModelsCallback: EpoxyController.() -> Unit = {}
) : AsyncEpoxyController() {
    override fun buildModels() {
        buildModelsCallback()
    }
}

/**
 * Create a [MavericksEpoxyController] that builds models with the given callback.
 */
fun Fragment.simpleController(
    buildModels: EpoxyController.() -> Unit
) = MavericksEpoxyController {
    // Models are built asynchronously, so it is possible that this is called after the fragment
    // is detached under certain race conditions.
    if (view == null || isRemoving) return@MavericksEpoxyController
    buildModels()
}

/**
 * Create a [MavericksEpoxyController] that builds models with the given callback.
 * When models are built the current state of the viewmodel will be provided.
 */
fun <S : MavericksState, A : MavericksViewModel<S>> Fragment.simpleController(
    viewModel: A,
    buildModels: EpoxyController.(state: S) -> Unit
) = MavericksEpoxyController {
    if (view == null || isRemoving) return@MavericksEpoxyController
    com.airbnb.mvrx.withState(viewModel) { state ->
        buildModels(state)
    }
}

fun <A : MavericksViewModel<B>, B : MavericksState, C : MavericksViewModel<D>, D : MavericksState> Fragment.simpleController(
    viewModel1: A,
    viewModel2: C,
    buildModels: EpoxyController.(state1: B, state2: D) -> Unit
) = MavericksEpoxyController {
    if (view == null || isRemoving) return@MavericksEpoxyController
    withState(viewModel1, viewModel2) { state1, state2 ->
        buildModels(state1, state2)
    }
}
