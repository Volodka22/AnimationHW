package com.example.animationhw

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.min


class Cross(context: Context, attrs: AttributeSet) : View(context, attrs), ValueAnimator.AnimatorUpdateListener {

    private var valueColor: Int
    private var thickness: Int
    private var lineLength = min(width, height) / 2F
    private var crossPaint: Paint
    private var xCenter  = width / 2F
    private var yCenter = height / 2F
    private var rotateAngle : Long = System.currentTimeMillis() % 360
    private var speed : Int


    init {
        val a: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.Cross, 0, 0
        )
        try {
            valueColor = a.getColor(R.styleable.Cross_colorLine, Color.RED)
            thickness = a.getDimensionPixelSize(
                R.styleable.Cross_thickness, 0
            )
            speed = a.getInt(R.styleable.Cross_speed, 0)
            crossPaint = Paint(ANTI_ALIAS_FLAG).apply {
                color = valueColor
                strokeWidth = thickness.toFloat()
            }
        } finally {
            a.recycle()
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        lineLength = min(w, h) / 2F
        xCenter = width / 2F
        yCenter = height / 2F
    }

    private fun drawRotateLine(angleDeg: Long, canvas: Canvas) {
        val angle = angleDeg *  PI / 180
        val newX = cos(angle) * lineLength + xCenter
        val newY = sin(angle) * lineLength + yCenter
        canvas.drawLine(xCenter, yCenter, newX.toFloat(), newY.toFloat(), crossPaint)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0..4) {
            drawRotateLine((rotateAngle + 90 * i) % 360, canvas)
        }
    }

    private var animator: ValueAnimator? = null

    fun startAnim() {
        animator = ValueAnimator.ofInt(0, 180)
        animator!!.repeatCount = ObjectAnimator.INFINITE
        animator!!.addUpdateListener(this)
        animator!!.start()
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) {
        rotateAngle += speed
        invalidate()
    }
}
