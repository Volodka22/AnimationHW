package com.example.animationhw


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.animationhw.databinding.ActivityMainBinding
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.StringBuilder
import java.math.BigInteger


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var animatorSet: AnimatorSet

    private var currentProgress: Int = -1

    private val finalN = 60

    private var ans: StringBuilder = StringBuilder()

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


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("progress", currentProgress)
        outState.putString("ans", ans.toString())
    }


    private fun isPrime(x: BigInteger): Boolean {
        var it = BigInteger("2")
        var flag = true
        while (it * it <= x) {
            if (x.mod(it) == BigInteger.ZERO) {
                flag = false
                break
            }
            it++
        }
        return flag
    }

    private fun calculate() {
        binding.progressbar.visibility = View.VISIBLE
        binding.startButton.visibility = View.GONE
        binding.progressbar.maxProgress = finalN
        lifecycleScope.launch(Dispatchers.Default) {
            var x = BigInteger.ONE.shiftLeft(currentProgress)
            for (i in (currentProgress..finalN)) {
                val curNumber = x - BigInteger.ONE
                x = x.shiftLeft(1)
                if (isPrime(curNumber)) {
                    if (ans.isNotEmpty()) {
                        ans.append(", ")
                    }
                    ans.append(curNumber.toString())
                }
                withContext(Dispatchers.Main) {
                    binding.progressbar.progress++
                    currentProgress++
                }
            }
            withContext(Dispatchers.Main) {
                binding.progressbar.visibility = View.GONE
                binding.header.visibility = View.VISIBLE
                binding.ans.visibility = View.VISIBLE
                binding.ans.text = ans
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentProgress = savedInstanceState?.getInt("progress") ?: -1
        ans = StringBuilder(savedInstanceState?.getString("ans") ?: "")

        if (currentProgress != -1) {
            calculate()
        }

        binding.cross.apply {
            startAnim()
            setOnClickListener { speed++ }
        }

        binding.startButton.setOnClickListener {
            currentProgress = 1
            calculate()
        }

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