package com.example.animationwheel

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
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

        scaleButton.setOnClickListener {
            scaleImage()
        }
        fadeButton.setOnClickListener {
            fadeImage()
        }
        moveButton.setOnClickListener {
            moveImage()
        }
        floatingButton.setOnClickListener {
            floatingImage()
        }
        wobbleButton.setOnClickListener {
            wobbleImage()
        }
        spinButton.setOnClickListener {
            spinImage()
        }
        berserkButton.setOnClickListener {
            berserkImage()
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

    private fun scaleImage() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 5f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 5f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(androidImage, scaleX, scaleY)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(scaleButton)
        animator.start()
    }

    private fun fadeImage() {
        val animator = ObjectAnimator.ofFloat(androidImage, View.ALPHA, 0f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(fadeButton)
        animator.start()
    }

    private fun moveImage() {
        val animator = ObjectAnimator.ofFloat(androidImage, View.TRANSLATION_X, 0f, 1200f, 0f, -1200f, 0f)
        animator.duration = 2400
        animator.disableViewDuringAnimation(moveButton)
        animator.start()
    }

    private fun floatingImage() {
        val animator = ObjectAnimator.ofFloat(androidImage, View.TRANSLATION_Y, 0f, -2400f, 0f)
        animator.duration = 10000
        animator.disableViewDuringAnimation(floatingButton)
        animator.start()
    }
    private fun wobbleImage() {
        val animator = ObjectAnimator.ofFloat(androidImage, View.TRANSLATION_X, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f)
        animator.duration = 1500
        animator.disableViewDuringAnimation(moveButton)
        animator.start()
        val animatorTwo = ObjectAnimator.ofFloat(androidImage, View.ROTATION, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f)
        animatorTwo.duration = 1500
        animatorTwo.disableViewDuringAnimation(moveButton)
        animatorTwo.start()

    }

    private fun berserkImage() {
        val animator = ObjectAnimator.ofFloat(androidImage, View.ROTATION_X, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f, 0f, 3f, 0f, -3f)
        animator.duration = 1500
        animator.disableViewDuringAnimation(berserkButton)
        animator.start()
        val animatorThree = ObjectAnimator.ofFloat(androidImage, View.TRANSLATION_Y, 0f, 700f, 400f, -700f, 0f, 0f, 700f, 400f, -700f, 0f, 0f, 700f, 400f, -700f, 0f, 0f, 700f, 400f, -700f, 0f, 0f, 700f, 400f, -700f, 0f )
        animatorThree.duration = 1500
        animatorThree.disableViewDuringAnimation(berserkButton)
        animatorThree.start()
        val animatorTwo = ObjectAnimator.ofFloat(androidImage, View.TRANSLATION_X, 0f, -700f, 0f, 700f, 0f, 0f, -700f, 0f, 700f, 0f, 0f, -700f, 0f, 700f, 0f, 0f, -700f, 0f, 700f, 0f, 0f, -700f, 0f, 700f, 0f)
        animatorTwo.duration = 1500
        animatorTwo.disableViewDuringAnimation(berserkButton)
        animatorTwo.start()

    }

    private fun spinImage() {
        spinButton.setOnClickListener {
            androidImage.animate().apply {
                duration = 1000
                rotationYBy(360f)
            }.withEndAction {
                androidImage.animate().apply {
                    duration = 1000
                    rotationYBy(360f)
                }.start()
            }
        }
    }


}
