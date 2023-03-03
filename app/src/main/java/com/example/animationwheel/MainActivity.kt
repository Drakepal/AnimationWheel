package com.example.animationwheel

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {

    private lateinit var imageButtonCenter: ImageButton

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var animation1: LinearLayout
    private lateinit var animation2: LinearLayout
    private lateinit var animation3: LinearLayout
    private lateinit var animation4: LinearLayout
    private lateinit var animation5: LinearLayout
    private lateinit var animation6: LinearLayout
    private lateinit var animation7: LinearLayout
    private lateinit var animation8: LinearLayout

    private var viewList = mutableListOf<LinearLayout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findView()
        setListener()
    }

    private fun setListener() {

        imageButtonCenter.setOnClickListener {
            toggleCircleMenu()
        }

        val listener = createTouchListener()
        constraintLayout.setOnTouchListener(listener)

        for(view in viewList) {
            view.setOnTouchListener(listener)
        }
    }

    private fun findView() {

        imageButtonCenter = findViewById(R.id.imageButtonCenter)

        constraintLayout = findViewById(R.id.constraintLayoutMenu)
        animation1 = findViewById(R.id.animation1)
        animation2 = findViewById(R.id.animation2)
        animation3 = findViewById(R.id.animation3)
        animation4 = findViewById(R.id.animation4)
        animation5 = findViewById(R.id.animation5)
        animation6 = findViewById(R.id.animation6)
        animation7 = findViewById(R.id.animation7)
        animation8 = findViewById(R.id.animation8)

        viewList.add(animation1)
        viewList.add(animation2)
        viewList.add(animation3)
        viewList.add(animation4)
        viewList.add(animation5)
        viewList.add(animation6)
        viewList.add(animation7)
        viewList.add(animation8)



    }

    private fun createTouchListener(): View.OnTouchListener {
        return object: View.OnTouchListener {
            var x: Float = 0f
            var isTouched = false

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        event.let {
                            x = it.x
                        }

                        return if (v is LinearLayout || v is ConstraintLayout) {
                            isTouched = true
                            true
                        } else {
                            isTouched = false
                            false
                        }
                    }

                    MotionEvent.ACTION_MOVE -> {
                        return true
                    }

                    MotionEvent.ACTION_UP -> {

                        val result = event.x -x

                        when {

                            result > 100 -> {
                                Log.d("???", "flip right")

                                for (linearLayout in viewList) {
                                    startRotate(linearLayout, false)
                                }
                            }
                            result < -100 -> {
                                Log.d("???", "flip left")

                                for (linearLayout in viewList) {
                                    startRotate(linearLayout, true)
                                }
                            }
                        }
                        return false
                    } else -> {
                        return true
                    }
                }

                return false
            }
        }
    }

    private fun startRotate(currentView: LinearLayout, isLeft: Boolean) {

        val layoutParams = currentView.layoutParams as ConstraintLayout.LayoutParams
        val currentAngle = layoutParams.circleAngle

        val targetAngle = currentAngle + if (isLeft) {
            -45
        } else {
            45
        }

        val angleAnimator = ValueAnimator.ofFloat(currentAngle, targetAngle)
        angleAnimator.addUpdateListener {
            layoutParams.circleAngle = it.animatedValue as Float
            currentView.layoutParams = layoutParams

        }
        angleAnimator.duration = 400
        angleAnimator.interpolator = AnticipateOvershootInterpolator()

        angleAnimator.start()
    }

    private fun toggleCircleMenu() {

        if (constraintLayout.visibility == View.VISIBLE) {
            constraintLayout.visibility = View.GONE
        } else {
            constraintLayout.visibility = View.VISIBLE
        }
    }
}