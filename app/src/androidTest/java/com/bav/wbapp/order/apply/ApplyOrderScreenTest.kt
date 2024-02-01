package com.bav.wbapp.order.apply

import android.os.Bundle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.check.KCheckBox
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.bav.wbapp.R
import com.bav.wbapp.order.OrderActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ApplyOrderScreenTest {

    companion object {
        private const val PRICE = 1000
        private const val GUEST_AMOUNT = 3
        private const val NAME = "Mock Mock"
        private const val PHONE = "8 927 100-10-10"
        private const val ADDRESS = "test address"
    }

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(OrderActivity::class.java)

    private val screen = ApplyOrderTestScreen()

    @Before
    fun setup() {
        val bundle = Bundle().apply {
            putInt("price", PRICE)
            putInt("guestsAmount", GUEST_AMOUNT)
            putString("name", NAME)
            putString("phone", PHONE)
            putString("address", ADDRESS)
            putString("date", null)
        }

        activityRule.activity.supportFragmentManager.beginTransaction().replace(
            R.id.nav_host_fragment,
            ApplyOrderScreen::class.java,
            bundle,
            "fragment-tag"
        ).commitAllowingStateLoss()
        Thread.sleep(3000)
    }

    @Test
    fun createOrderScreenTestInitialData() {
        screen.price { hasText("₽ $PRICE") }
        screen.menu { hasText("$GUEST_AMOUNT персоны, 7 блюд") }
        screen.name { hasText(NAME) }
        screen.phone { hasText(PHONE) }
        screen.address { hasText(ADDRESS) }

        screen.timeText { isVisible() }
        screen.time {
            isVisible()
            hasText("1ч 20мин")
        }

        screen.cardInAppText { hasTextColor(com.bav.core.R.color.cool_grey) }
        screen.cashToCourierText { hasTextColor(com.bav.core.R.color.cool_grey) }

        screen.changeRequiredCheck { isGone() }
        screen.changeRequiredText { isGone() }
        screen.moneyAvailableText { isGone() }
        screen.moneyAvailable { isGone() }

        screen.applyButton { isVisible() }
        screen.bankCardButton { isInvisible() }
    }

    @Test
    fun createOrderScreenTestCardToCourier() {
        screen.cardToCourierCheck { click() }

        screen.cardInAppText { hasTextColor(com.bav.core.R.color.cool_grey) }
        screen.cashToCourierText { hasTextColor(com.bav.core.R.color.cool_grey) }

        screen.changeRequiredCheck { isGone() }
        screen.changeRequiredText { isGone() }
        screen.moneyAvailableText { isGone() }
        screen.moneyAvailable { isGone() }

        screen.applyButton { isVisible() }
        screen.bankCardButton { isInvisible() }
    }

    @Test
    fun createOrderScreenTestCardInApp() {
        screen.cardInAppCheck { click() }

        screen.cardToCourierText { hasTextColor(com.bav.core.R.color.cool_grey) }
        screen.cashToCourierText { hasTextColor(com.bav.core.R.color.cool_grey) }

        screen.changeRequiredCheck { isGone() }
        screen.changeRequiredText { isGone() }
        screen.moneyAvailableText { isGone() }
        screen.moneyAvailable { isGone() }

        screen.applyButton { isInvisible() }
        screen.bankCardButton { isVisible() }
    }

    @Test
    fun createOrderScreenTestCashToCourier() {
        screen.cashToCourierCheck { click() }

        screen.cardToCourierText { hasTextColor(com.bav.core.R.color.cool_grey) }
        screen.cardInAppText { hasTextColor(com.bav.core.R.color.cool_grey) }

        screen.changeRequiredCheck { isVisible() }
        screen.changeRequiredText { isVisible() }
        screen.moneyAvailableText { isGone() }
        screen.moneyAvailable { isGone() }

        screen.changeRequiredCheck { click() }
        screen.moneyAvailableText { isVisible() }
        screen.moneyAvailable { isVisible() }

        screen.moneyAvailable {
            replaceText("2000")
            hasText("2000")
        }

        screen.applyButton { isVisible() }
        screen.bankCardButton { isInvisible() }
    }
}

open class ApplyOrderTestScreen : Screen<ApplyOrderTestScreen>() {

    val price: KTextView = KTextView {
        withId(R.id.to_pay)
    }
    val menu: KTextView = KTextView {
        withId(R.id.menu_text)
    }
    val name: KTextView = KTextView {
        withId(R.id.name)
    }
    val phone: KTextView = KTextView {
        withId(R.id.phone)
    }
    val address: KTextView = KTextView {
        withId(R.id.address)
    }
    val timeText: KTextView = KTextView {
        withId(R.id.time_text)
    }
    val time: KTextView = KTextView {
        withId(R.id.time)
    }

    val cardToCourierText: KTextView = KTextView {
        withId(R.id.card_to_courier_text)
    }
    val cardInAppText: KTextView = KTextView {
        withId(R.id.card_in_app_text)
    }
    val cashToCourierText: KTextView = KTextView {
        withId(R.id.cash_to_courier_text)
    }

    val cardToCourierCheck: KButton = KButton { withId(R.id.card_to_courier_check) }
    val cardInAppCheck: KButton = KButton { withId(R.id.card_in_app_check) }
    val cashToCourierCheck: KButton = KButton { withId(R.id.cash_to_courier_check) }

    val changeRequiredCheck: KCheckBox = KCheckBox { withId(R.id.change_required_check) }
    val changeRequiredText: KTextView = KTextView {
        withId(R.id.change_required_text)
    }
    val moneyAvailableText: KTextView = KTextView {
        withId(R.id.money_available_text)
    }
    val moneyAvailable: KEditText = KEditText { withId(R.id.money_available) }

    val applyButton: KButton = KButton { withId(R.id.apply_order_button) }
    val bankCardButton: KButton = KButton { withId(R.id.bank_card_button) }
}