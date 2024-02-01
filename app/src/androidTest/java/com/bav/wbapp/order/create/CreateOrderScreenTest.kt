package com.bav.wbapp.order.create

import androidx.test.espresso.matcher.ViewMatchers.isNotEnabled
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.common.views.KView
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.bav.core.profile.ProfileResponseDataModel
import com.bav.wbapp.R
import com.bav.wbapp.order.OrderActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CreateOrderScreenTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(OrderActivity::class.java)

    private val screen = CreateOrderTestScreen()

    @Before
    fun setup() {
        activityRule.activity.supportFragmentManager.beginTransaction().replace(
            R.id.nav_host_fragment,
            CreateOrderScreen(),
            "fragment-tag"
        ).commitAllowingStateLoss()
        Thread.sleep(1000)
    }

    @Test
    fun createOrderScreenTestInitialData() {
        val user = ProfileResponseDataModel(
            name = "Mock Mock",
            email = "mock@mail.ru",
            phone = "8 927 100-10-10",
            password = "123456"
        )
        val userName = user.name.split(" ")
        var uName = ""
        var uLastName = ""
        if (userName.size == 2) {
            uName = userName[1]
            uLastName = userName[0]
        }

        screen.name { hasText(uName) }
        screen.lastName { hasText(uLastName) }
        screen.phone { hasText(user.phone) }
        screen.email { hasText(user.email) }

        screen.dateContainer { isGone() }
        screen.restaurantContainer { isGone() }
        screen.deliveryContainer { isVisible() }

        screen.applyButton { isNotEnabled() }
    }

    @Test
    fun createOrderScreenTestDelivery() {
        screen.deliveryButton { click() }

        screen.dateContainer { isGone() }
        screen.restaurantContainer { isGone() }
        screen.deliveryContainer { isVisible() }

        screen.applyButton { isNotEnabled() }
        screen.address { replaceText("test address") }
        screen.applyButton { isEnabled() }
    }

    @Test
    fun createOrderScreenTestInTime() {
        screen.inTimeButton { click() }

        screen.dateContainer { isVisible() }
        screen.restaurantContainer { isGone() }
        screen.deliveryContainer { isVisible() }

        screen.address { replaceText("") }
        screen.applyButton { isNotEnabled() }
        screen.address { replaceText("test address") }
        screen.applyButton { isEnabled() }
    }

    @Test
    fun createOrderScreenTestPickup() {
        screen.pickupButton { click() }

        screen.dateContainer { isVisible() }
        screen.restaurantContainer { isVisible() }
        screen.deliveryContainer { isGone() }

        screen.applyButton { isNotEnabled() }
    }
}

open class CreateOrderTestScreen : Screen<CreateOrderTestScreen>() {

    val name: KEditText = KEditText {
        withId(com.bav.ui.R.id.customEditText)
        withParent {
            withId(R.id.name)
        }
    }
    val lastName: KEditText = KEditText {
        withId(com.bav.ui.R.id.customEditText)
        withParent {
            withId(R.id.last_name)
        }
    }
    val phone: KEditText = KEditText {
        withId(com.bav.ui.R.id.customEditText)
        withParent {
            withId(R.id.phone)
        }
    }
    val email: KEditText = KEditText {
        withId(com.bav.ui.R.id.customEditText)
        withParent {
            withId(R.id.email)
        }
    }

    val deliveryButton: KButton = KButton { withId(R.id.delivery_button) }
    val inTimeButton: KButton = KButton { withId(R.id.in_time_button) }
    val pickupButton: KButton = KButton { withId(R.id.pickup_button) }

    val dateContainer: KView = KView { withId(R.id.date_container) }
    val restaurantContainer: KView = KView { withId(R.id.restaurant_container) }
    val deliveryContainer: KView = KView { withId(R.id.delivery_container) }

    val address: KEditText = KEditText {
        withId(com.bav.ui.R.id.customEditText)
        withParent {
            withId(R.id.address)
        }
    }

    val applyButton: KButton = KButton { withId(R.id.apply_button) }
}