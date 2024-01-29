package com.bav.ui

import android.content.Context
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.InputFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.text.method.PasswordTransformationMethod
import android.transition.ChangeBounds
import android.transition.Fade
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import com.bav.ui.databinding.CustomEditTextViewBinding

private enum class EnterType {
    NAME,
    EMAIL,
    PASSWORD,
    PHONE,
    DATE
}

typealias OnTextChangedListener = (String) -> Unit

class CustomEditText(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    companion object {
        private const val DEFAULT_TITLE_TEXT_SIZE = 12
        private const val DEFAULT_EDIT_TEXT_SIZE = 16
        private const val DEFAULT_BUTTON_TEXT_SIZE = 10
        private const val DEFAULT_LINE_SIZE = 1

        const val HIDE = "Скрыть"
        const val SHOW = "Показать"

        const val PASSWORD = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        const val EMAIL = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        const val PHONE = InputType.TYPE_CLASS_PHONE
        const val NAME = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        const val DATE = InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_DATE

        const val MAX_PHONE_LENGTH = 16
        const val MAX_DATE_LENGTH = 10

        @JvmField
        val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
            override fun createFromParcel(source: Parcel?): SavedState = SavedState(source)
            override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
        }
    }

    private val binding: CustomEditTextViewBinding

    private var buttonPressed = false

    private var listener: OnTextChangedListener? = null

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.custom_edit_text_view, this, true)
        binding = CustomEditTextViewBinding.bind(this)
        initAttrs(attrs, defStyleAttr, defStyleRes)
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText, defStyleAttr, defStyleRes)

        val enterType = EnterType.values()[typedArray.getInt(R.styleable.CustomEditText_enterType, 0)]

        with(binding) {
            title.apply {
                val titleText = typedArray.getString(R.styleable.CustomEditText_titleText)
                val titleTextSize = typedArray.getDimension(
                    R.styleable.CustomEditText_titleTextSize,
                    context.toPx(DEFAULT_TITLE_TEXT_SIZE)
                )
                val titleTextColor = typedArray.getColor(
                    R.styleable.CustomEditText_titleTextColor,
                    context.getColor(com.bav.core.R.color.cool_grey)
                )
                text = titleText
                setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize)
                setTextColor(titleTextColor)
                visibility = if (binding.customEditText.text.isEmpty()) {
                    GONE
                } else {
                    VISIBLE
                }
            }

            customEditText.apply {
                isSingleLine = true
                val allCaps = typedArray.getBoolean(R.styleable.CustomEditText_textAllCaps, false)
                val hintText = typedArray.getString(R.styleable.CustomEditText_hintText)
                val hintTextColor = typedArray.getColor(
                    R.styleable.CustomEditText_hintTextColor,
                    context.getColor(com.bav.core.R.color.cool_grey)
                )
                hint = if (allCaps) {
                    hintText?.uppercase()
                } else {
                    hintText
                }
                setHintTextColor(hintTextColor)

                val editTextSize = typedArray.getDimension(
                    R.styleable.CustomEditText_editTextSize,
                    context.toPx(DEFAULT_EDIT_TEXT_SIZE)
                )
                val editTextColor =
                    typedArray.getColor(R.styleable.CustomEditText_editTextColor, context.getColor(com.bav.core.R.color.white))
                setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize)
                setTextColor(editTextColor)

                val activeColor = typedArray.getColor(
                    R.styleable.CustomEditText_lineActiveColor,
                    context.getColor(com.bav.core.R.color.tangerine_two)
                )
                val passiveColor = typedArray.getColor(
                    R.styleable.CustomEditText_linePassiveColor,
                    context.getColor(com.bav.core.R.color.cool_grey)
                )
                setOnFocusChangeListener { _, hasFocus ->
                    val lineColor = if (hasFocus) {
                        activeColor
                    } else {
                        passiveColor
                    }
                    binding.bottomLine.setBackgroundColor(lineColor)
                }
                doOnTextChanged { text, _, _, _ ->
                    listener?.invoke(text.toString())

                    val transition = TransitionSet().apply {
                        duration = 100
                        addTransition(ChangeBounds())
                        addTransition(Fade())
                    }

                    TransitionManager.beginDelayedTransition(binding.root as ViewGroup?, transition)
                    binding.title.visibility = if (text?.length == 0) {
                        GONE
                    } else {
                        VISIBLE
                    }
                }

                when (enterType) {
                    EnterType.PASSWORD -> {
                        transformationMethod = PasswordTransformationMethod()
                        inputType = PASSWORD
                    }

                    EnterType.NAME     -> {
                        inputType = NAME
                    }

                    EnterType.EMAIL    -> {
                        inputType = EMAIL
                    }

                    EnterType.PHONE    -> {
                        inputType = PHONE
                        filters = arrayOf(*this.filters, InputFilter.LengthFilter(MAX_PHONE_LENGTH))
                        keyListener = DigitsKeyListener.getInstance("0123456789-+ ")
                        addTextChangedListener(PhoneNumberFormattingTextWatcher())
                    }

                    EnterType.DATE     -> {
                        inputType = DATE
                        filters = arrayOf(*this.filters, InputFilter.LengthFilter(MAX_DATE_LENGTH))
                        keyListener = DigitsKeyListener.getInstance("0123456789")
                    }
                }
            }

            textButton.apply {
                if (enterType == EnterType.PASSWORD) {
                    val buttonPressedText = typedArray.getString(R.styleable.CustomEditText_buttonTextPressed) ?: HIDE
                    val buttonNotPressedText = typedArray.getString(R.styleable.CustomEditText_buttonText) ?: SHOW
                    val buttonText = if (buttonPressed) {
                        buttonPressedText
                    } else {
                        buttonNotPressedText
                    }
                    val buttonTextSize = typedArray.getDimension(
                        R.styleable.CustomEditText_buttonTextSize,
                        context.toPx(DEFAULT_BUTTON_TEXT_SIZE)
                    )
                    val buttonTextColor = typedArray.getColor(R.styleable.CustomEditText_editTextColor, Color.GRAY)
                    visibility = VISIBLE
                    text = buttonText
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonTextSize)
                    setTextColor(buttonTextColor)

                    setOnClickListener {
                        buttonPressed = !buttonPressed
                        text = if (buttonPressed) {
                            buttonPressedText
                        } else {
                            buttonNotPressedText
                        }
                    }
                } else {
                    visibility = GONE
                }
            }

            bottomLine.apply {
                val lineSize = typedArray.getDimension(
                    R.styleable.CustomEditText_lineSize,
                    context.toPx(DEFAULT_LINE_SIZE)
                )
                val lineColor = typedArray.getColor(R.styleable.CustomEditText_linePassiveColor, Color.GRAY)
                setBackgroundColor(lineColor)
                layoutParams.apply {
                    height = lineSize.toInt()
                }
            }
        }

        typedArray.recycle()
    }

    fun setEditTextListener(listener: OnTextChangedListener) {
        this.listener = listener
    }

    fun setText(text: String) {
        binding.customEditText.setText(text)
    }

    fun getText() = binding.customEditText.text.toString()

    fun setLinesNumber(number: Int) {
        binding.customEditText.apply {
            isSingleLine = false
            setLines(number)
            minLines = number
            maxLines = number
            gravity = Gravity.TOP
        }
    }

    private fun Context.toPx(dp: Int): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        resources.displayMetrics
    )


    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.text = binding.customEditText.text.toString()
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        binding.customEditText.setText(savedState.text.toString())
    }

    class SavedState : BaseSavedState {
        var text: String? = ""

        constructor(superState: Parcelable?) : super(superState)
        constructor(source: Parcel?) : super(source) {
            text = source?.readString()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(text)
        }
    }
}