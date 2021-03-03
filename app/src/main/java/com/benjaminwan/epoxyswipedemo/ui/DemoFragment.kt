package com.benjaminwan.epoxyswipedemo.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.airbnb.epoxy.EpoxyRecyclerView
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.benjaminwan.epoxyswipedemo.R
import com.benjaminwan.epoxyswipedemo.itemviews.demoItemView
import com.benjaminwan.epoxyswipedemo.utils.setItemDecoration
import com.benjaminwan.epoxyswipedemo.utils.simpleController

class DemoFragment(@LayoutRes contentLayoutId: Int = R.layout.fragment_demo) :
    Fragment(contentLayoutId), MavericksView {

    private val demoRv: EpoxyRecyclerView by lazy { requireView().findViewById(R.id.demoRv) }
    private val demoVM by activityViewModel(DemoViewModel::class)
    private val epoxyController by lazy { epoxyController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        epoxyController.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        epoxyController.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        val demos: MutableList<String> = mutableListOf()
        (0..99).forEach {
            demos.add("Test Swipe Menu Item $it")
        }
        demoVM.setDemos(demos)
    }

    private fun initViews() {
        demoRv.setHasFixedSize(true)
        demoRv.setItemDecoration(4, 2, 4, 2)
        demoRv.setController(epoxyController)
    }

    override fun invalidate() = withState(demoVM) { state ->
        demoRv.requestModelBuild()
    }

    private fun epoxyController() = simpleController(demoVM) { state ->
        state.demos.forEach { demo ->
            demoItemView {
                id("demo${demo.hashCode()}")
                demo(demo)
            }
        }
    }


    companion object {

    }

}