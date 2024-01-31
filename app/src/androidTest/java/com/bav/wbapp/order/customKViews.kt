package com.bav.wbapp.order

import android.view.View
import android.widget.TextView
import androidx.test.espresso.DataInteraction
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import com.agoda.kakao.common.actions.BaseActions
import com.agoda.kakao.common.assertions.BaseAssertions
import com.agoda.kakao.common.builders.ViewBuilder
import com.agoda.kakao.common.views.KBaseView
import com.agoda.kakao.text.TextViewActions
import com.bav.ui.EditAmountView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers

/** KView для EditAmountView */
class KEditAmount : KBaseView<KEditAmount>, EditAmountActions, EditAmountAssertions {
    constructor(function: ViewBuilder.() -> Unit) : super(function)
    constructor(parent: Matcher<View>, function: ViewBuilder.() -> Unit) : super(parent, function)
    constructor(parent: DataInteraction, function: ViewBuilder.() -> Unit) : super(parent, function)
}

interface EditAmountActions : BaseActions {
    fun getValue(): Int {
        var value = 0

        view.perform(object : ViewAction {
            override fun getDescription() = "Get EditAmountView value"

            override fun getConstraints() =
                Matchers.allOf(ViewMatchers.isAssignableFrom(EditAmountView::class.java), ViewMatchers.isDisplayed())

            override fun perform(uiController: UiController?, view: View?) {
                if (view is EditAmountView) {
                    value = view.getAmount()
                }
            }
        })

        return value
    }

    fun clickMinus() {
        view.perform(object : ViewAction {
            override fun getDescription() = "EditAmountView click on minus button"

            override fun getConstraints() =
                Matchers.allOf(ViewMatchers.isAssignableFrom(EditAmountView::class.java), ViewMatchers.isDisplayed())

            override fun perform(uiController: UiController?, view: View?) {
                if (view is EditAmountView) {
                    val value = view.getAmount()
                    view.setCurrentAmount(value - 1)
                }
            }
        })
    }

    fun clickPlus() {
        view.perform(object : ViewAction {
            override fun getDescription() = "EditAmountView click on plus button"

            override fun getConstraints() =
                Matchers.allOf(ViewMatchers.isAssignableFrom(EditAmountView::class.java), ViewMatchers.isDisplayed())

            override fun perform(uiController: UiController?, view: View?) {
                if (view is EditAmountView) {
                    val value = view.getAmount()
                    view.setCurrentAmount(value + 1)
                }
            }
        })
    }
}

interface EditAmountAssertions : BaseAssertions {
    fun hasValue(value: Int) {
        view.check(ViewAssertions.matches(EditAmountValueMatcher(value)))
    }
}

class EditAmountValueMatcher(private val value: Int) : BoundedMatcher<View, EditAmountView>(EditAmountView::class.java) {

    private var currentValue: Int = 0

    override fun matchesSafely(view: EditAmountView) = run {
        currentValue = view.getAmount()
        currentValue == value
    }

    override fun describeTo(description: Description?) {
        description
            ?.appendText("EditAmountView with ")
            ?.appendValue(value)
            ?.appendText(" , but got with ")
            ?.appendValue(currentValue)
    }
}


/** Расширения для KViews */

fun TextViewActions.getText(): String {
    var value = ""

    view.perform(object : ViewAction {
        override fun getDescription() = "Get TextView text"

        override fun getConstraints() =
            Matchers.allOf(ViewMatchers.isAssignableFrom(TextView::class.java), ViewMatchers.isDisplayed())

        override fun perform(uiController: UiController?, view: View?) {
            if (view is TextView) {
                value = view.text.toString()
            }
        }
    })

    return value
}
