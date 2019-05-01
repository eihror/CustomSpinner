package com.example.spinnerlib

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Resources.NotFoundException
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop

class AwesomeSpinner : LinearLayout {

  private var _relativeLayout: RelativeLayout = RelativeLayout(context)
  private var _spinner: Spinner = Spinner(context)
  private var _hint: TextView = TextView(context)
  private var _error: TextView = TextView(context)

  constructor(context: Context) : super(context)

  constructor(
    context: Context,
    attrs: AttributeSet
  ) : super(context, attrs) {

    _dimenStart = context.resources.getDimension(R.dimen.base_start_end)
    _dimenEnd = context.resources.getDimension(R.dimen.base_start_end)
    _dimenTop = context.resources.getDimension(R.dimen.base_top_bot)
    _dimenBottom = context.resources.getDimension(R.dimen.base_top_bot)
    _dimenPaddingHint = context.resources.getDimension(R.dimen.base_padding_hint)
    _borderRadius = context.resources.getDimension(R.dimen.base_border_radius)

    initAttrs(attrs)

    orientation = VERTICAL

    configureBaseBlock()
    configureBaseSpinner()
    configureBaseHint()
    configureBaseError()

    _relativeLayout.addView(_hint, childCount)
    _relativeLayout.addView(_spinner, childCount)

    addView(_relativeLayout, childCount)
    addView(_error, childCount)
  }

  private fun initAttrs(attrs: AttributeSet) {
    val typedArray = context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.AwesomeSpinner,
        0, 0
    )

    try {
      _dimenStart = typedArray.getDimension(
          R.styleable.AwesomeSpinner_as_paddingStart, resources.getDimension(R.dimen.base_start_end)
      )
      _dimenEnd = typedArray.getDimension(
          R.styleable.AwesomeSpinner_as_paddingEnd, resources.getDimension(R.dimen.base_start_end)
      )
      _dimenTop = typedArray.getDimension(
          R.styleable.AwesomeSpinner_as_paddingTop, resources.getDimension(R.dimen.base_top_bot)
      )
      _dimenBottom = typedArray.getDimension(
          R.styleable.AwesomeSpinner_as_paddingBottom, resources.getDimension(R.dimen.base_top_bot)
      )
      _borderRadius = typedArray.getDimension(
          R.styleable.AwesomeSpinner_as_borderRadius,
          resources.getDimension(R.dimen.base_border_radius)
      )
      _borderStroke = typedArray.getDimension(
          R.styleable.AwesomeSpinner_as_borderStroke, _borderStroke
      )
      _hintText =
        typedArray.getString(R.styleable.AwesomeSpinner_as_hintText)?.let { it } ?: run { "" }
      _defaultColor = typedArray.getColor(R.styleable.AwesomeSpinner_as_defaultColor, _defaultColor)
      _highlightColor =
        typedArray.getColor(R.styleable.AwesomeSpinner_as_highlightColor, _highlightColor)
      _errorColor = typedArray.getColor(R.styleable.AwesomeSpinner_as_errorColor, _errorColor)
      _errorEnabled = typedArray.getBoolean(R.styleable.AwesomeSpinner_as_errorEnabled, false)
      _justify = typedArray.getBoolean(R.styleable.AwesomeSpinner_as_justify, true)

    } finally {
      typedArray.recycle()
    }
  }

  private fun configureBaseBlock() {
    _relativeLayout.layoutParams =
      LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
  }

  private fun configureBaseSpinner() {

    val list = resources.getStringArray(R.array.month)
        .toList()

    _backgroundSpinner.shape = GradientDrawable.RECTANGLE
    _backgroundSpinner.setColor(ContextCompat.getColor(context, android.R.color.white))
    _backgroundSpinner.cornerRadius = _borderRadius

    try {
      _backgroundSpinner.setStroke(
          _borderStroke.toInt(), ContextCompat.getColor(context, _defaultColor)
      )
    } catch (e: NotFoundException) {
      _backgroundSpinner.setStroke(
          _borderStroke.toInt(), _defaultColor
      )
    }

    val stateLisDrawable = StateListDrawable()
    stateLisDrawable.addState(StateSet.WILD_CARD, _backgroundSpinner)

    val layout = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    layout.setMargins(
        _spinner.marginLeft, 10F.toInt(), _spinner.marginRight, _spinner.marginBottom
    )

    _spinner.layoutParams = layout
    _spinner.id = View.generateViewId()
    _spinner.background = stateLisDrawable
    _spinner.setPadding(
        (_dimenStart + _dimenPaddingHint).toInt(),
        _dimenTop.toInt(),
        (_dimenEnd + _dimenPaddingHint).toInt(),
        _dimenBottom.toInt()
    )

    post {
      _spinner.dropDownVerticalOffset = _spinner.measuredHeight
    }

    _spinner.createAdapter(
        R.layout.item_spinner,
        R.layout.item_spinner_dropdown,
        list,
        {
          it.let { pos: Int ->
            if (pos == 0) {
              animateHint(false)
            } else {
              animateHint(true)
            }
          }
        },
        { })

  }

  private fun configureBaseHint() {
    val layout = RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    layout.addRule(RelativeLayout.ALIGN_TOP, _spinner.id)
    layout.addRule(RelativeLayout.ALIGN_BOTTOM, _spinner.id)
    layout.setMargins(
        _dimenStart.toInt(),
        _hint.marginTop,
        _dimenEnd.toInt(),
        _hint.marginBottom
    )

    _hint.layoutParams = layout
    _hint.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
    _hint.setPadding(
        _dimenPaddingHint.toInt(),
        _hint.paddingTop,
        _dimenPaddingHint.toInt(),
        _hint.paddingBottom
    )
    _hint.gravity = Gravity.CENTER
    _hint.text = _hintText

    try {
      _hint.setTextColor(ContextCompat.getColor(context, _defaultColor))
    } catch (e: NotFoundException) {
      _hint.setTextColor(_defaultColor)
    }

  }

  private fun configureBaseError() {
    _error.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    try {
      _error.setTextColor(ContextCompat.getColor(context, _errorColor))
    } catch (e: NotFoundException) {
      _error.setTextColor(_errorColor)
    }
    _error.visibility = View.GONE

    if (_justify) {
      _error.setPadding(
          (_dimenStart + _dimenPaddingHint).toInt(),
          _error.paddingTop,
          (_dimenEnd + _dimenPaddingHint).toInt(),
          _error.paddingBottom
      )
    } else {
      _error.setPadding(0, 0, 0, 0)
    }
  }

  private fun animateHint(animate: Boolean) {

    val start: Float
    val end: Float

    if (animate) {
      start = 0F
      end = -(_spinner.height / 2).toFloat()
    } else {
      start = -(_spinner.height / 2).toFloat()
      end = 0F
    }

    ObjectAnimator.ofFloat(_hint, "translationY", start, end)
        .apply {
          duration = 1_00

          addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
              val backgroundHint = GradientDrawable()
              backgroundHint.shape = GradientDrawable.LINE
              backgroundHint.setColor(ContextCompat.getColor(context, android.R.color.white))
              backgroundHint.setStroke(
                  (_borderStroke * 2).toInt(),
                  ContextCompat.getColor(context, android.R.color.white)
              )
              _hint.background = if (animate) {
                backgroundHint
              } else {
                null
              }
            }
          })

          start()
        }
  }

  /* Functions */

  fun setError(error: String?) {
    _error.text = error
  }

  fun isErrorEnabled(show: Boolean) {
    _errorEnabled = show

    if (_errorEnabled) {
      _backgroundSpinner.setStroke(
          _borderStroke.toInt(), ContextCompat.getColor(context, _errorColor)
      )
      _hint.setTextColor(ContextCompat.getColor(context, _errorColor))
    } else {
      _backgroundSpinner.setStroke(
          _borderStroke.toInt(), ContextCompat.getColor(context, _defaultColor)
      )
      _hint.setTextColor(ContextCompat.getColor(context, _defaultColor))
    }

    if (_error.text.isNotEmpty() || _error.text.isNotBlank()) {
      if (show) {
        if (!_wasAdded) {
          _wasAdded = true
        }
        _error.visibility = View.VISIBLE

        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator() //add this
        fadeIn.duration = 1_00

        _error.animation = fadeIn
      } else {
        if (!_wasAdded) {
          return
        }
        if (_error.visibility == View.GONE) {
          return
        }

        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.interpolator = AccelerateInterpolator() //and this
        fadeOut.startOffset = 1000
        fadeOut.duration = 1_00
        _error.animation = fadeOut

        _error.visibility = View.GONE
      }
    }
  }

  inline fun setItemSelected(crossinline selected: (Int) -> Unit) {
    selected(1)
    return
  }

  companion object {
    val _backgroundSpinner = GradientDrawable()

    var _dimenStart: Float = 0.0f
    var _dimenEnd: Float = 0.0f
    var _dimenTop: Float = 0.0f
    var _dimenBottom: Float = 0.0f
    var _dimenPaddingHint: Float = 0.0f
    var _borderRadius: Float = 0.0f
    var _borderStroke: Float = 0.0f
    var _hintText: String = ""
    var _defaultColor: Int = R.color.default_color
    var _highlightColor: Int = R.color.highlight_color
    var _errorColor: Int = R.color.error_color
    var _errorEnabled: Boolean = false
    var _justify: Boolean = true
    var _wasAdded: Boolean = false
  }

}