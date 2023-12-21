package com.bav.core

import android.widget.TextView

class EditAmountBinder(
    private val amount: TextView,
    private val amountMinus: TextView,
    private val amountPlus: TextView,
    private val callback: () -> Unit
) {
    private var currentAmount: Int = 0

    fun getAmount() = currentAmount
    fun setAmount() {
        currentAmount = 1
        amount.text = currentAmount.toString()
    }

    fun bind() {
        amountMinus.setOnClickListener {
            if (currentAmount > 1) {
                currentAmount--
            } else {
                currentAmount = 0
                callback.invoke()
            }
            amount.text = currentAmount.toString()
        }
        amountPlus.setOnClickListener {
            currentAmount++
            amount.text = currentAmount.toString()
        }
    }
}