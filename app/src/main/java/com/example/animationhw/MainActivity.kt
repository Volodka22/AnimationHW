package com.example.animationhw


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.animationhw.databinding.ActivityMainBinding
import android.view.View
import androidx.core.animation.doOnEnd


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var animatorSet: AnimatorSet

    private fun getScaleCoordinate(coordinate: Char, view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "scale$coordinate", 1.5f).apply {
            duration = 1000L
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = 1
        }
    }

    private fun getScaleCoordinatesAnimation(view: View): AnimatorSet {
        val animX = getScaleCoordinate('X', view)
        val animY = getScaleCoordinate('Y', view)
        return AnimatorSet().apply {
            playTogether(animX, animY)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cross.startAnim()

        val seq = listOf(
            getScaleCoordinatesAnimation(binding.circle1),
            getScaleCoordinatesAnimation(binding.circle3),
            getScaleCoordinatesAnimation(binding.circle4),
            getScaleCoordinatesAnimation(binding.circle2)
        )

        animatorSet = AnimatorSet().apply {
            playSequentially(seq)
            doOnEnd { start() }
            start()
        }
    }
}