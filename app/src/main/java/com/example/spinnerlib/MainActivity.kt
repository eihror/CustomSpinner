package com.example.spinnerlib

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.spinner
import android.widget.RelativeLayout
import android.widget.RelativeLayout.LayoutParams
import kotlinx.android.synthetic.main.activity_main.hint

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
            Toast.makeText(this, "$it selected!", Toast.LENGTH_SHORT)
                .show()
          }
        },
        { })

    Handler().postDelayed({

      val layoutParams: LayoutParams = hint.layoutParams as LayoutParams
      layoutParams.removeRule(RelativeLayout.CENTER_VERTICAL)
      hint.layoutParams = layoutParams

      val objectAnimator =
        ObjectAnimator.ofFloat(hint, "translationY", 0f, -((hint.height / 2) - 5).toFloat())

      objectAnimator.duration = 1_00
      objectAnimator.start()

    }, 2_000)

  }

}