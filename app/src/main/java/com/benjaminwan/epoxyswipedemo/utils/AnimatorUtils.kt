package com.benjaminwan.epoxyswipedemo.utils

import android.animation.*
import android.view.View

/**
 * 晃动动画生成工具
 * @param  view
 * @param  shakeFactor 幅度
 * @return 返回 ObjectAnimator
 */
private fun tada(view: View, shakeFactor: Float): ObjectAnimator {
    val pvhScaleX = PropertyValuesHolder.ofKeyframe(
        View.SCALE_X,
        Keyframe.ofFloat(0f, 1f),
        Keyframe.ofFloat(.1f, .9f),
        Keyframe.ofFloat(.2f, .9f),
        Keyframe.ofFloat(.3f, 1.1f),
        Keyframe.ofFloat(.4f, 1.1f),
        Keyframe.ofFloat(.5f, 1.1f),
        Keyframe.ofFloat(.6f, 1.1f),
        Keyframe.ofFloat(.7f, 1.1f),
        Keyframe.ofFloat(.8f, 1.1f),
        Keyframe.ofFloat(.9f, 1.1f),
        Keyframe.ofFloat(1f, 1f)
    )

    val pvhScaleY = PropertyValuesHolder.ofKeyframe(
        View.SCALE_Y,
        Keyframe.ofFloat(0f, 1f),
        Keyframe.ofFloat(.1f, .9f),
        Keyframe.ofFloat(.2f, .9f),
        Keyframe.ofFloat(.3f, 1.1f),
        Keyframe.ofFloat(.4f, 1.1f),
        Keyframe.ofFloat(.5f, 1.1f),
        Keyframe.ofFloat(.6f, 1.1f),
        Keyframe.ofFloat(.7f, 1.1f),
        Keyframe.ofFloat(.8f, 1.1f),
        Keyframe.ofFloat(.9f, 1.1f),
        Keyframe.ofFloat(1f, 1f)
    )

    val pvhRotate = PropertyValuesHolder.ofKeyframe(
        View.ROTATION,
        Keyframe.ofFloat(0f, 0f),
        Keyframe.ofFloat(.1f, -3f * shakeFactor),
        Keyframe.ofFloat(.2f, -3f * shakeFactor),
        Keyframe.ofFloat(.3f, 3f * shakeFactor),
        Keyframe.ofFloat(.4f, -3f * shakeFactor),
        Keyframe.ofFloat(.5f, 3f * shakeFactor),
        Keyframe.ofFloat(.6f, -3f * shakeFactor),
        Keyframe.ofFloat(.7f, 3f * shakeFactor),
        Keyframe.ofFloat(.8f, -3f * shakeFactor),
        Keyframe.ofFloat(.9f, 3f * shakeFactor),
        Keyframe.ofFloat(1f, 0f)
    )

    return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate)
        .setDuration(1000)
}

/**
 * 晃动动画 晃动一次
 */
fun View.shake() {
    val animator = tada(this, 1f)
    animator.start()
}

/**
 * 晃动动画 晃动二次
 */
fun View.shakeRestart() {
    val animator = tada(this, 1f)
    animator.repeatCount = ValueAnimator.RESTART
    animator.start()
}

/**
 * 晃动动画 晃动二次
 */
fun View.shakeReverse() {
    val animator = tada(this, 1f)
    animator.repeatCount = ValueAnimator.REVERSE
    animator.start()
}

/**
 * 晃动动画 连续晃动
 */
fun View.shakeInfinite(): ObjectAnimator {
    val scaleX = this.scaleX
    val scaleY = this.scaleY
    val rotation = this.rotation
    val animator = tada(this, 1f)
    animator.repeatCount = ValueAnimator.INFINITE
    animator.start()
    animator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            this@shakeInfinite.scaleX = scaleX
            this@shakeInfinite.scaleY = scaleY
            this@shakeInfinite.rotation = rotation
        }
    })
    return animator
}