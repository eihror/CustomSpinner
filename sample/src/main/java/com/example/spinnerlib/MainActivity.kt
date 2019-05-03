package com.example.spinnerlib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.button
import kotlinx.android.synthetic.main.activity_main.spinner
import kotlinx.android.synthetic.main.activity_main.spinner2
import kotlinx.android.synthetic.main.activity_main.spinner3

class MainActivity : AppCompatActivity() {

  var error: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    configureView()
  }

  private fun configureView() {

    spinner.setList(resources.getStringArray(R.array.month).toList())
    spinner2.setList(resources.getStringArray(R.array.month).toList())
    spinner3.setList(resources.getStringArray(R.array.month).toList())

    spinner.setItemSelected {
    }
    spinner2.setItemSelected {
    }
    spinner3.setItemSelected {
    }

    button.setOnClickListener {
      error = !error
      spinner.setError(null)
      spinner2.setError("Mensagem de erro")
      spinner3.setError(
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum malesuada, nisl ut venenatis"
      )
      spinner.isErrorEnabled(error)
      spinner2.isErrorEnabled(error)
      spinner3.isErrorEnabled(error)

    }

  }

}