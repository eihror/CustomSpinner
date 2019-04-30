package com.example.spinnerlib

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.hint
import kotlinx.android.synthetic.main.activity_main.spinner
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    configureView()
  }

  private fun configureView() {

    val list = resources.getStringArray(R.array.month)
        .toList()

    spinner.createAdapter(
        R.layout.item_spinner,
        R.layout.item_spinner_dropdown,
        list,
        {
          it.let {
            if (it == 0) {
              animateHint(false)
            } else {
              animateHint(true)
              Toast.makeText(this, "$it selected!", Toast.LENGTH_SHORT)
                  .show()
            }
          }
        },
        { })

  }

  private fun animateHint(animate: Boolean) {

    var start: Float
    var end: Float

    if (animate) {
      start = 0F
      end = -(spinner.height / 2).toFloat()
    } else {
      start = -(spinner.height / 2).toFloat()
      end = 0F
    }

    val animation = ObjectAnimator.ofFloat(hint, "translationY", start, end)
        .apply {
          duration = 1_00

          addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
              hint.background = if (animate) {
                ContextCompat.getDrawable(applicationContext, R.drawable.bg_hint)
              } else {
                null
              }

            }
          })

        }

    animation.start()
  }

}