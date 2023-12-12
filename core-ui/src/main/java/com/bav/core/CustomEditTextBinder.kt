package com.bav.core

import android.text.InputType
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
 *
 * inputTypeId - пока что возможно установить только Email и Password (дальше изменится)
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

        /**
         * InputTypes у EditText (скрытый пароль и почта)
         */
        const val PASSWORD = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        const val EMAIL = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }

    fun bind() {
        titleView.text = title
        editTextView.apply {
            hint = title.uppercase(Locale.getDefault())
            inputType = when(inputTypeId) {
                CustomEditTextInputType.Password -> {
                    transformationMethod = PasswordTransformationMethod()
                    PASSWORD
                }

                CustomEditTextInputType.Email -> {
                    EMAIL
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
                    editTextView.inputType = EMAIL
                    clickableText.text = HIDE
                } else {
                    editTextView.inputType = PASSWORD
                    clickableText.text = SHOW
                }
            }
        }
    }
}

sealed class CustomEditTextInputType {
    data object Password : CustomEditTextInputType()
    data object Email : CustomEditTextInputType()
}
