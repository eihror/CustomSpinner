package com.example.spinnerlib

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.button
import kotlinx.android.synthetic.main.activity_main.spinner

class MainActivity : AppCompatActivity() {

  var error: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    configureView()
  }

  private fun configureView() {
    spinner.setItemSelected {
      Toast.makeText(applicationContext, "Posição $it selecionada", Toast.LENGTH_LONG)
          .show()
    }

    button.setOnClickListener {
      error = !error
      spinner.setError(null)
      spinner.isErrorEnabled(error)
    }

  }

}