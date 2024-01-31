package com.bav.wbapp.order

import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import com.bav.wbapp.MainActivity
import com.bav.wbapp.R
import com.bav.wbapp.order.basket.BasketScreen
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class BasketScreenTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    private val screen = BasketTestScreen()

    @Before
    fun setup() {
        activityRule.activity.supportFragmentManager.beginTransaction().replace(
            R.id.nav_host_fragment,
            BasketScreen(),
            "fragment-tag"
        ).commitAllowingStateLoss()
        Thread.sleep(3000)
    }

    @Test
    fun basketScreenTestEditAmountViewPlusAndMinusButtonsClick() {
        screen.recycler {
            if (getSize() > 0) {
                var price = screen.price.getText()

                firstChild<BasketTestScreen.Item> {
                    val startValue = editAmount.getValue()

                    editAmount.clickMinus()
                    editAmount.hasValue(startValue - 1)
                    screen.price.hasNoText(price)
                    price = screen.price.getText()

                    editAmount.clickPlus()
                    editAmount.hasValue(startValue)
                    screen.price.hasNoText(price)
                }
            }
        }
    }
}

open class BasketTestScreen : Screen<BasketTestScreen>() {
    val recycler: KRecyclerView = KRecyclerView({
        withId(R.id.basket_recycler)
    }, itemTypeBuilder = {
        itemType(::Item)
    })

    val price: KTextView = KTextView { withId(R.id.total_with_discount) }

    class Item(parent: Matcher<View>) : KRecyclerItem<Item>(parent) {
        val editAmount: KEditAmount = KEditAmount(parent) { withId(R.id.amount_view) }
    }
}