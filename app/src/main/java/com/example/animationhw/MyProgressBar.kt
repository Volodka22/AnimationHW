package com.example.animationhw

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View

class MyProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs),
    ValueAnimator.AnimatorUpdateListener {

    private var animator: ValueAnimator? = null

    private var progressLinePaint: Paint = Paint()
    private var backgroundLinePaint: Paint = Paint()
    var progress: Int = 0
        set(value) {
            field = value
            startAnim()
        }
    var maxProgress: Int = 100
        set(value) {
            field = value
            startAnim()
        }
    private var currentProgress: Float


    fun setColorProgressLine(x: Int) {
        progressLinePaint.color = x
    }

    fun setColorBackgroundLine(x: Int) {
        backgroundLinePaint.color = x
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putInt("colorProgressLine", this.progressLinePaint.color)
        bundle.putInt("colorBackgroundLine", this.backgroundLinePaint.color)
        bundle.putInt("progress", this.progress)
        bundle.putInt("maxProgress", this.maxProgress)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            maxProgress = state.getInt("maxProgress")
            progress = state.getInt("progress")
            backgroundLinePaint.color = state.getInt("colorBackgroundLine")
            progressLinePaint.color = state.getInt("colorProgressLine")
            currentProgress = progress.toFloat() / maxProgress
            super.onRestoreInstanceState(state.getParcelable("superState"))
        } else {
            super.onRestoreInstanceState(state)
        }
    }


    init {
        val a: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.MyProgressBar, 0, 0
        )
        try {
            progressLinePaint.color =
                a.getColor(R.styleable.MyProgressBar_ColorProgressLine, Color.BLUE)
            backgroundLinePaint.color =
                a.getColor(R.styleable.MyProgressBar_ColorBackgroundLine, Color.GRAY)
            progress = a.getInt(R.styleable.MyProgressBar_progress, 0)
            maxProgress = a.getInt(R.styleable.MyProgressBar_maxProgress, 100)
            progressLinePaint.strokeWidth = height.toFloat()
            backgroundLinePaint.strokeWidth = progressLinePaint.strokeWidth
            currentProgress = progress.toFloat() / maxProgress
        } finally {
            a.recycle()
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        progressLinePaint.strokeWidth = height.toFloat()
        backgroundLinePaint.strokeWidth = progressLinePaint.strokeWidth
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(0F, height / 2F, width.toFloat(), height / 2F, backgroundLinePaint)
        canvas.drawLine(
            0F,
            height / 2F,
            currentProgress * width,
            height / 2F,
            progressLinePaint
        )
    }


    private fun startAnim() {
        animator?.removeAllUpdateListeners()
        val startVal = (animator?.animatedValue ?: 0F) as Float
        animator = ValueAnimator.ofFloat(startVal, progress.toFloat() / maxProgress)
            .apply {
                duration = 2000L
                addUpdateListener(this@MyProgressBar)
                start()
            }
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) {
        currentProgress = animation?.animatedValue as Float
        invalidate()
    }
}