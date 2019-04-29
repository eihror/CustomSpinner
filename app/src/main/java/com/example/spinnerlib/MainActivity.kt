package com.example.spinnerlib

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.spinner
import android.widget.ArrayAdapter

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    configureView()
  }

  private fun configureView() {

    var list = resources.getStringArray(R.array.month).toList()

    spinner.createAdapter(
        R.layout.item_spinner,
        R.layout.item_spinner_dropdown,
        list,
        {
          it.let {
            Toast.makeText(this,"$it selected!", Toast.LENGTH_SHORT)
                .show()
          }
        },
        { })



    var adapter = ArrayAdapter(
        this,
        android.R.layout.simple_dropdown_item_1line, list
    )

    spinner2.setAdapter(adapter)

  }

}