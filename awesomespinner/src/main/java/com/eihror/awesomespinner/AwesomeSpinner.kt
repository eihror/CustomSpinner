package com.eihror.awesomespinner

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import android.graphics.Typeface
import android.util.TypedValue
import androidx.core.view.marginStart

class AwesomeSpinner : LinearLayout {

    private var _relativeLayout: RelativeLayout = RelativeLayout(context)
    var _spinner: Spinner = Spinner(context)
    private var _hint: TextView = TextView(context)
    private var _error: TextView = TextView(context)
    private var _arrow: ImageView = ImageView(context)

    var _list: List<String> = listOf("Example")

    var _typeface: Typeface? = null
    var _dimenStart: Int = 0
    var _dimenEnd: Int = 0
    var _dimenTop: Int = 0
    var _dimenBottom: Int = 0
    var _dimenPaddingHint: Int = 0
    var _borderRadius: Float = 0.0f
    var _borderStroke: Float = 0.0f
    var _hintText: String = ""
    var _hintView: Int = -1
    var _textSize: Int = 0
    var _dropDownView: Int = -1
    var _arrowIcon: Int = R.drawable.ic_dropdown
    var _arrowColor: Int = R.color.default_color
    var _defaultColor: Int = R.color.default_color
    var _highlightColor: Int = R.color.highlight_color
    var _errorColor: Int = R.color.error_color
    var _errorEnabled: Boolean = false
    var _justify: Boolean = true
    var _wasAdded: Boolean = false

    constructor(context: Context) : super(context)

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs) {

        _dimenStart = context.resources.getDimension(R.dimen.base_start_end)
            .toInt()
        _dimenEnd = context.resources.getDimension(R.dimen.base_start_end)
            .toInt()
        _dimenTop = context.resources.getDimension(R.dimen.base_top_bot)
            .toInt()
        _dimenBottom = context.resources.getDimension(R.dimen.base_top_bot)
            .toInt()
        _dimenPaddingHint = context.resources.getDimension(R.dimen.base_padding_hint)
            .toInt()
        _borderRadius = context.resources.getDimension(R.dimen.base_border_radius)

        initAttrs(attrs)

        orientation = VERTICAL

        configureBaseBlock()
        configureBaseSpinner()
        configureBaseHint()
        configureBaseArrow()
        configureBaseError()

        _relativeLayout.addView(_arrow, childCount)
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
            _dimenStart = typedArray.getDimensionPixelSize(
                R.styleable.AwesomeSpinner_as_paddingStart,
                resources.getDimension(R.dimen.base_start_end).toInt()
            )
            _dimenEnd = typedArray.getDimensionPixelSize(
                R.styleable.AwesomeSpinner_as_paddingEnd,
                resources.getDimension(R.dimen.base_start_end).toInt()
            )
            _dimenTop = typedArray.getDimensionPixelSize(
                R.styleable.AwesomeSpinner_as_paddingTop,
                resources.getDimension(R.dimen.base_top_bot).toInt()
            )
            _dimenBottom = typedArray.getDimensionPixelSize(
                R.styleable.AwesomeSpinner_as_paddingBottom,
                resources.getDimension(R.dimen.base_top_bot).toInt()
            )
            _borderRadius = typedArray.getDimension(
                R.styleable.AwesomeSpinner_as_borderRadius,
                resources.getDimension(R.dimen.base_border_radius).dp2px()
            )
            _borderStroke = typedArray.getDimension(
                R.styleable.AwesomeSpinner_as_borderStroke, _borderStroke.dp2px()
            )
            _hintView =
                typedArray.getResourceId(R.styleable.AwesomeSpinner_as_hintView, R.layout.item_spinner)
            _dropDownView =
                typedArray.getResourceId(
                    R.styleable.AwesomeSpinner_as_dropDownView, R.layout.item_spinner_dropdown
                )
            _hintText =
                typedArray.getString(R.styleable.AwesomeSpinner_as_hintText)?.let { it } ?: run { "" }
            _textSize = typedArray.getDimensionPixelSize(
                R.styleable.AwesomeSpinner_as_textSize,
                resources.getDimension(R.dimen.base_font_size).toInt()
            )
            _arrowIcon = typedArray.getResourceId(R.styleable.AwesomeSpinner_as_arrowIcon, _arrowIcon)
            _arrowColor = typedArray.getColor(R.styleable.AwesomeSpinner_as_arrowColor, _arrowColor)
            _defaultColor = typedArray.getColor(R.styleable.AwesomeSpinner_as_defaultColor, _defaultColor)
            _highlightColor =
                typedArray.getColor(R.styleable.AwesomeSpinner_as_highlightColor, _highlightColor)
            _errorColor = typedArray.getColor(R.styleable.AwesomeSpinner_as_errorColor, _errorColor)
            _errorEnabled = typedArray.getBoolean(R.styleable.AwesomeSpinner_as_errorEnabled, false)
            _justify = typedArray.getBoolean(R.styleable.AwesomeSpinner_as_justify, true)

            val typefacePath = typedArray.getString(R.styleable.AwesomeSpinner_as_fontFamily)
            if (typefacePath != null) {
                _typeface = Typeface.createFromAsset(context.assets, typefacePath)
            }

            _textSize = (_textSize.toFloat().px2sp()).toInt()

        } finally {
            typedArray.recycle()
        }

    }

    private fun configureBaseBlock() {
        _relativeLayout.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val height = (_dimenTop + _dimenBottom + _borderStroke + _textSize).toInt()

        _relativeLayout.minimumHeight = height
    }

    private fun configureBaseSpinner() {

        val _backgroundSpinner = GradientDrawable()
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

        _spinner.createAdapter(
            _hintView,
            _dropDownView,
            _textSize.toFloat(),
            _typeface,
            _list
        )

        val layoutSpinner = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        layoutSpinner.setMargins(
            _spinner.marginLeft, (_textSize + _borderStroke.toInt()), _spinner.marginRight,
            _spinner.marginBottom
        )

        _spinner.id = View.generateViewId()
        _spinner.background = _backgroundSpinner
        _spinner.setPadding(
            (_dimenStart + _dimenPaddingHint),
            _dimenTop,
            (_dimenEnd + _dimenPaddingHint),
            _dimenBottom
        )
        _spinner.layoutParams = layoutSpinner

    }

    private fun configureBaseHint() {
        val layout = RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layout.addRule(RelativeLayout.ALIGN_TOP, _spinner.id)
        layout.addRule(RelativeLayout.ALIGN_BOTTOM, _spinner.id)
        layout.setMargins(
            _dimenStart,
            _hint.marginTop,
            _dimenEnd,
            _hint.marginBottom
        )

        _hint.layoutParams = layout
        _hint.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        _hint.setPadding(
            _dimenPaddingHint,
            _hint.paddingTop,
            _dimenPaddingHint,
            _hint.paddingBottom
        )
        _hint.gravity = Gravity.CENTER
        _hint.text = _hintText
        if (_typeface != null) {
            _hint.typeface = _typeface
        }

        try {
            _hint.setTextColor(ContextCompat.getColor(context, _defaultColor))
        } catch (e: NotFoundException) {
            _hint.setTextColor(_defaultColor)
        }

        _hint.setTextSize(TypedValue.COMPLEX_UNIT_SP, _textSize.toFloat())

    }

    private fun configureBaseArrow() {
        val layout = RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layout.addRule(RelativeLayout.ALIGN_TOP, _spinner.id)
        layout.addRule(RelativeLayout.ALIGN_BOTTOM, _spinner.id)
        layout.addRule(RelativeLayout.ALIGN_END, _spinner.id)
        layout.setMargins(
            _arrow.marginStart,
            _arrow.marginTop,
            _dimenEnd,
            _arrow.marginBottom
        )

        _arrow.layoutParams = layout
        _arrow.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        _arrow.setImageResource(_arrowIcon)
        try {
            _arrow.setColorFilter(ContextCompat.getColor(context, _arrowColor), PorterDuff.Mode.SRC_IN)
        } catch (e: NotFoundException) {
            _arrow.setColorFilter(_arrowColor, PorterDuff.Mode.SRC_IN)
        }

    }

    private fun configureBaseError() {
        _error.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        try {
            _error.setTextColor(ContextCompat.getColor(context, _errorColor))
        } catch (e: NotFoundException) {
            _error.setTextColor(_errorColor)
        }
        if (_typeface != null) {
            _error.typeface = _typeface
        }
        _error.setTextSize(TypedValue.COMPLEX_UNIT_SP, _textSize.toFloat())
        _error.visibility = View.GONE

        if (_justify) {
            _error.setPadding(
                (_dimenStart + _dimenPaddingHint),
                _error.paddingTop,
                (_dimenEnd + _dimenPaddingHint),
                _error.paddingBottom
            )
        } else {
            _error.setPadding(0, 0, 0, 0)
        }
    }

    private fun updateAdapter() {

        _spinner.createAdapter(
            _hintView,
            _dropDownView,
            _textSize.toFloat(),
            _typeface,
            _list
        )

        post {
            _spinner.dropDownVerticalOffset = _spinner.measuredHeight
            val layoutSpinner = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            layoutSpinner.setMargins(
                _spinner.marginLeft, (_textSize + _borderStroke.toInt()), _spinner.marginRight,
                _spinner.marginBottom
            )
            _spinner.layoutParams = layoutSpinner
        }

    }

    fun animateHint(animate: Boolean) {

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

    fun setError(error: String?) {
        _error.text = error
    }

    fun setList(list: List<String>) {
        _list = list
        updateAdapter()
    }

    fun isErrorEnabled(show: Boolean) {
        _errorEnabled = show

        val background: GradientDrawable = _spinner.background as GradientDrawable

        if (_errorEnabled) {
            background.setStroke(
                _borderStroke.toInt(), ContextCompat.getColor(context, _errorColor)
            )
            _hint.setTextColor(ContextCompat.getColor(context, _errorColor))
        } else {
            background.setStroke(
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
        var _startSpinner = false
        _spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (_startSpinner) {
                    if (position == 0) {
                        animateHint(false)
                    } else {
                        animateHint(true)
                    }
                } else {
                    _startSpinner = true
                }

                selected(position)
            }
        }
    }

    companion object {

        private fun Float.sp2px(): Float {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem()
                    .displayMetrics
            )
        }

        private fun Float.px2sp(): Float {
            return this / Resources.getSystem()
                .displayMetrics.scaledDensity
        }

        private fun Float.dp2px(): Float {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem()
                    .displayMetrics
            )
        }

        private fun Float.px2dp(): Float {
            return this / Resources.getSystem()
                .displayMetrics.density
        }
    }

}