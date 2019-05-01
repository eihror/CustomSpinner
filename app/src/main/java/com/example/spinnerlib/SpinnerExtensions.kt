package com.example.spinnerlib

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView

fun <V> Spinner.createAdapter(
  layout: Int,
  dropDownLayout: Int,
  list: List<V>?,
  selectedListener: ((position: Int) -> Unit)? = null,
  nothingSelectedListener: (() -> Unit)? = null
) {

  var isSpinnerTouched = false

  setOnTouchListener { _, _ ->
    isSpinnerTouched = true
    false
  }

  val aa = object : ArrayAdapter<V>(this.context, layout, list) {
    override fun getView(
      position: Int,
      convertView: View?,
      parent: ViewGroup?
    ): View {
      val view = super.getView(position, convertView, parent!!)
      val textview = view as TextView
      if (position == 0) {
        textview.text = ""
      }
      textview.setTextColor(Color.BLACK)
      return view
    }

    override fun getDropDownView(
      position: Int,
      convertView: View?,
      parent: ViewGroup
    ): View {
      val view = super.getDropDownView(position, convertView, parent)
      val textview = view as TextView
      if (position == 0) {
        textview.setTextColor(Color.GRAY)
      } else {
        textview.setTextColor(Color.BLACK)
      }
      return view
    }
  }
  dropDownLayout.let { aa.setDropDownViewResource(it) }

  with(this) {
    adapter = aa
  }

  this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
      nothingSelectedListener?.let { it() }
    }

    override fun onItemSelected(
      parent: AdapterView<*>?,
      view: View?,
      position: Int,
      id: Long
    ) {
      if (!isSpinnerTouched) return
      selectedListener?.let { it(position) }
    }
  }

}
