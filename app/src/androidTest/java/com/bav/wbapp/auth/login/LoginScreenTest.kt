package com.bav.wbapp.auth.login


import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.bav.wbapp.AuthActivity
import com.bav.wbapp.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(AuthActivity::class.java)

    private val screen = LoginTestScreen()

    @Test
    fun loginScreenTest() {
        screen.loginText {
            replaceText("test@mail.ru")
            closeSoftKeyboard()
        }
        screen.passwordText {
            replaceText("123456")
            closeSoftKeyboard()
        }
        screen.loginButton {
            isEnabled()
            click()
        }
    }

    @Test
    fun loginScreenTestEmailEmpty() {
        screen.passwordText {
            replaceText("123456")
            closeSoftKeyboard()
        }
        screen.loginButton {
            isNotEnabled()
        }
    }

    @Test
    fun loginScreenTestEmailIncorrect() {
        screen.loginText {
            replaceText("test@mail")
            closeSoftKeyboard()
        }
        screen.passwordText {
            replaceText("123456")
            closeSoftKeyboard()
        }
        screen.loginButton {
            isNotEnabled()
        }
    }

    @Test
    fun loginScreenTestPasswordEmpty() {
        screen.loginText {
            replaceText("test@mail.ru")
            closeSoftKeyboard()
        }
        screen.passwordText {
            replaceText("12345")
            closeSoftKeyboard()
        }
        screen.loginButton {
            isNotEnabled()
        }
    }

    @Test
    fun loginScreenTestPasswordLessThanMinimumNumberCharacters() {
        screen.loginText {
            replaceText("test@mail.ru")
            closeSoftKeyboard()
        }
        screen.passwordText {
            replaceText("12345")
            closeSoftKeyboard()
        }
        screen.loginButton {
            isNotEnabled()
        }
    }
}

open class LoginTestScreen: Screen<LoginTestScreen>() {
    val loginText: KEditText = KEditText {
        withId(com.bav.core.R.id.custom_edit_text)
        withParent {
            withId(R.id.login)
        }
    }

    val passwordText: KEditText = KEditText {
        withId(com.bav.core.R.id.custom_edit_text)
        withParent {
            withId(R.id.password)
        }
    }

    val loginButton: KButton = KButton {
        withId(R.id.login_button)
    }
}
