package com.example.animationwheel

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageButton
import android.widget.ImageView
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


    lateinit var androidImage: ImageView
    lateinit var rotateButton: ImageButton
    lateinit var scaleButton: ImageButton
    lateinit var fadeButton: ImageButton
    lateinit var moveButton: ImageButton
    lateinit var floatingButton: ImageButton
    lateinit var wobbleButton: ImageButton
    lateinit var spinButton: ImageButton
    lateinit var berserkButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        androidImage = findViewById(R.id.monster)
        rotateButton = findViewById(R.id.rotate)
        scaleButton = findViewById(R.id.scale)
        fadeButton = findViewById(R.id.fade)
        moveButton = findViewById(R.id.move)
        floatingButton = findViewById(R.id.floating)
        wobbleButton = findViewById(R.id.wobble)
        spinButton = findViewById(R.id.spin)
        berserkButton = findViewById(R.id.berserk)

        rotateButton.setOnClickListener {
            rotateImage()
        }


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

    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {

        addListener(object: AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator) {
                view.isEnabled = true
            }
        })

        }

    private fun rotateImage() {
        val animator = ObjectAnimator.ofFloat(androidImage, View.ROTATION, -360f, 0f)
        animator.duration = 1500
        animator.disableViewDuringAnimation(rotateButton)
        animator.start()
    }
    }
