package com.bav.wbapp.main


import android.view.View
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import com.bav.wbapp.MainActivity
import com.bav.wbapp.R
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private val screen = MainTestScreen()

    @Test
    fun mainScreenTest() {
        screen.recycler {
            firstChild<MainTestScreen.ItemBig> {
                isVisible()
                title { hasText("Акции") }
            }

            scrollToEnd()
            lastChild<MainTestScreen.ItemSmall> {
                isVisible()
                title { hasText("О приложении") }
            }
        }
    }
}

open class MainTestScreen : Screen<MainTestScreen>() {
    val recycler: KRecyclerView = KRecyclerView({
        withId(R.id.main_recycler)
    }, itemTypeBuilder = {
        itemType(::ItemBig)
        itemType(::ItemSmall)
    })


    class ItemBig(parent: Matcher<View>) : KRecyclerItem<ItemBig>(parent) {
        val title: KTextView = KTextView(parent) { withId(R.id.main_recycler_big_holder_title) }
    }
    class ItemSmall(parent: Matcher<View>) : KRecyclerItem<ItemSmall>(parent) {
        val title: KTextView = KTextView(parent) { withId(R.id.main_recycler_small_holder_title) }
    }
}
