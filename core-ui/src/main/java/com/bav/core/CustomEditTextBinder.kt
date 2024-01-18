package com.bav.core

import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.InputFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import java.util.Locale


/**
 * Кастомный EditText с текстовой кнопкой(сейчас только для пароля)
 * и отображением hint сверху, если text.length > 0
 *
 * Также есть возможность установить цвет, если EditText в фокусе - targetColor,
 * цвет по умолчанию - notTargetColor
 */
class CustomEditTextBinder(
    private val titleView: TextView,
    private val editTextView: EditText,
    private val rightClickView: TextView? = null,
    private val title: String,
    private val inputTypeId: CustomEditTextInputType,
    private val targetColor: Int,
    private val notTargetColor: Int,
    private val enterCallback: (String) -> Unit
) {

    companion object {
        /**
         * Текст на кликабельном TextView (возможно все что связано с ним (больше связано с clickListener)
         * можно вынести в отдельный параметр (если несколько параметров, то создать еще 1 data class))
         */
        const val HIDE = "Скрыть"
        const val SHOW = "Показать"

        const val PASSWORD = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        const val EMAIL = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        const val PHONE = InputType.TYPE_CLASS_PHONE
        const val NAME = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        const val NULL = InputType.TYPE_NULL
        const val DATE = InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_DATE

        const val MAX_PHONE_LENGTH = 16
        const val MAX_DATE_LENGTH = 10
    }

    fun bind() {
        titleView.text = title
        editTextView.apply {
            hint = title.uppercase(Locale.getDefault())
            when(inputTypeId) {
                CustomEditTextInputType.Password -> {
                    transformationMethod = PasswordTransformationMethod()
                    inputType = PASSWORD
                }

                CustomEditTextInputType.Email    -> inputType = EMAIL

                CustomEditTextInputType.Name     -> inputType = NAME

                CustomEditTextInputType.Phone    -> {
                    keyListener = DigitsKeyListener.getInstance("0123456789 ()-+")
                    filters = arrayOf(*this.filters, InputFilter.LengthFilter(MAX_PHONE_LENGTH))
                    inputType = PHONE
                    addTextChangedListener(PhoneNumberFormattingTextWatcher())
                }

                CustomEditTextInputType.Date     -> {
                    inputType = DATE
                    filters = arrayOf(*this.filters, InputFilter.LengthFilter(MAX_DATE_LENGTH))
                    keyListener = DigitsKeyListener.getInstance("0123456789-")
                }
            }
            setOnFocusChangeListener { v, hasFocus ->
                val colorRes = if (hasFocus) {
                    targetColor
                } else {
                    notTargetColor
                }
                v.resources.getColor(colorRes, v.context.theme).apply {
                    titleView.setTextColor(this)
                    rightClickView?.setTextColor(this)
                    editTextView.setTintColor(this)
                }
            }
            doOnTextChanged { text, _, _, _ ->
                enterCallback(text.toString())
                titleView.visibility = if (text?.length == 0) {
                    View.INVISIBLE
                } else {
                    View.VISIBLE
                }
            }
        }
        rightClickView?.let { clickableText ->
            clickableText.text = SHOW
            clickableText.visibility = View.VISIBLE
            clickableText.setOnClickListener {
                if (editTextView.inputType == PASSWORD) {
                    editTextView.inputType = NULL
                    clickableText.text = HIDE
                } else {
                    editTextView.inputType = PASSWORD
                    clickableText.text = SHOW
                }
            }
        }
    }
}

fun customEditTextBinder(
    titleView: TextView,
    editTextView: EditText,
    rightClickView: TextView? = null,
    title: String,
    inputTypeId: CustomEditTextInputType,
    targetColor: Int,
    notTargetColor: Int,
    enterCallback: (String) -> Unit
) {
    CustomEditTextBinder(
        titleView = titleView,
        editTextView = editTextView,
        rightClickView = rightClickView,
        title = title,
        inputTypeId = inputTypeId,
        targetColor = targetColor,
        notTargetColor = notTargetColor,
        enterCallback = enterCallback
    ).bind()
}

sealed class CustomEditTextInputType {
    data object Password : CustomEditTextInputType()
    data object Email : CustomEditTextInputType()
    data object Phone : CustomEditTextInputType()
    data object Name : CustomEditTextInputType()
    data object Date : CustomEditTextInputType()
}
