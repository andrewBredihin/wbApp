package com.bav.wbapp.auth.registration


import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.check.KCheckBox
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.bav.wbapp.AuthActivity
import com.bav.wbapp.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class RegistrationScreenTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(AuthActivity::class.java)

    private val screen = RegistrationTestScreen()

    @Before
    fun setup() {
        activityRule.activity.supportFragmentManager.beginTransaction().replace(
            R.id.nav_host_fragment,
            RegistrationScreen(),
            "fragment-tag"
        ).commitAllowingStateLoss()
        Thread.sleep(500)
    }

    @Test
    fun registrationScreenTest() {
        screen.nameText {
            replaceText("test test")
            closeSoftKeyboard()
        }
        screen.phoneText {
            replaceText("+7 927 100-10-10")
            closeSoftKeyboard()
        }
        screen.loginText {
            replaceText("test@mail.ru")
            closeSoftKeyboard()
        }
        screen.passwordText {
            replaceText("123456")
            closeSoftKeyboard()
        }
        screen.check {
            setChecked(true)
        }
        screen.registrationButton {
            isEnabled()
            click()
        }
    }

    @Test
    fun registrationScreenTestAllFieldsEmpty() {
        screen.registrationButton {
            isNotEnabled()
        }
    }

    @Test
    fun registrationScreenTestNameLessThanMinimumNumberCharacters() {
        screen.nameText {
            replaceText("12345")
            closeSoftKeyboard()
        }
        screen.phoneText {
            replaceText("+7 927 100-10-10")
            closeSoftKeyboard()
        }
        screen.loginText {
            replaceText("test@mail.ru")
            closeSoftKeyboard()
        }
        screen.passwordText {
            replaceText("123456")
            closeSoftKeyboard()
        }
        screen.check {
            setChecked(true)
        }
        screen.registrationButton {
            isNotEnabled()
        }
    }

    @Test
    fun registrationScreenTestNameEmpty() {
        screen.phoneText {
            replaceText("+7 927 100-10-10")
            closeSoftKeyboard()
        }
        screen.loginText {
            replaceText("test@mail.ru")
            closeSoftKeyboard()
        }
        screen.passwordText {
            replaceText("123456")
            closeSoftKeyboard()
        }
        screen.check {
            setChecked(true)
        }
        screen.registrationButton {
            isNotEnabled()
        }
    }

    @Test
    fun registrationScreenTestPhoneEmpty() {
        screen.nameText {
            replaceText("test test")
            closeSoftKeyboard()
        }
        screen.loginText {
            replaceText("test@mail.ru")
            closeSoftKeyboard()
        }
        screen.passwordText {
            replaceText("123456")
            closeSoftKeyboard()
        }
        screen.check {
            setChecked(true)
        }
        screen.registrationButton {
            isNotEnabled()
        }
    }

    @Test
    fun registrationScreenTestPhoneIncorrect() {
        screen.nameText {
            replaceText("test test")
            closeSoftKeyboard()
        }
        screen.phoneText {
            replaceText("123456789")
            closeSoftKeyboard()
        }
        screen.loginText {
            replaceText("test@mail.ru")
            closeSoftKeyboard()
        }
        screen.passwordText {
            replaceText("123456")
            closeSoftKeyboard()
        }
        screen.check {
            setChecked(true)
        }
        screen.registrationButton {
            isNotEnabled()
        }
    }

    @Test
    fun registrationScreenTestLoginEmpty() {
        screen.nameText {
            replaceText("test test")
            closeSoftKeyboard()
        }
        screen.phoneText {
            replaceText("+7 927 100-10-10")
            closeSoftKeyboard()
        }
        screen.passwordText {
            replaceText("123456")
            closeSoftKeyboard()
        }
        screen.check {
            setChecked(true)
        }
        screen.registrationButton {
            isNotEnabled()
        }
    }

    @Test
    fun registrationScreenTestLoginIncorrect() {
        screen.nameText {
            replaceText("test test")
            closeSoftKeyboard()
        }
        screen.phoneText {
            replaceText("+7 927 100-10-10")
            closeSoftKeyboard()
        }
        screen.loginText {
            replaceText("test@mail")
            closeSoftKeyboard()
        }
        screen.passwordText {
            replaceText("123456")
            closeSoftKeyboard()
        }
        screen.check {
            setChecked(true)
        }
        screen.registrationButton {
            isNotEnabled()
        }
    }

    @Test
    fun registrationScreenTestPasswordLessThanMinimumNumberCharacters() {
        screen.nameText {
            replaceText("test test")
            closeSoftKeyboard()
        }
        screen.phoneText {
            replaceText("+7 927 100-10-10")
            closeSoftKeyboard()
        }
        screen.loginText {
            replaceText("test@mail.ru")
            closeSoftKeyboard()
        }
        screen.passwordText {
            replaceText("12345")
            closeSoftKeyboard()
        }
        screen.check {
            setChecked(true)
        }
        screen.registrationButton {
            isNotEnabled()
        }
    }

    @Test
    fun registrationScreenTestPasswordEmpty() {
        screen.nameText {
            replaceText("test test")
            closeSoftKeyboard()
        }
        screen.phoneText {
            replaceText("+7 927 100-10-10")
            closeSoftKeyboard()
        }
        screen.loginText {
            replaceText("test@mail.ru")
            closeSoftKeyboard()
        }
        screen.check {
            setChecked(true)
        }
        screen.registrationButton {
            isNotEnabled()
        }
    }

    @Test
    fun registrationScreenTestNotCheck() {
        screen.nameText {
            replaceText("test test")
            closeSoftKeyboard()
        }
        screen.phoneText {
            replaceText("+7 927 100-10-10")
            closeSoftKeyboard()
        }
        screen.loginText {
            replaceText("test@mail.ru")
            closeSoftKeyboard()
        }
        screen.passwordText {
            replaceText("123456")
            closeSoftKeyboard()
        }
        screen.registrationButton {
            isNotEnabled()
        }
    }
}

open class RegistrationTestScreen : Screen<RegistrationTestScreen>() {

    val nameText: KEditText = KEditText {
        withId(com.bav.core.R.id.custom_edit_text)
        withParent {
            withId(R.id.name_reg)
        }
    }

    val phoneText: KEditText = KEditText {
        withId(com.bav.core.R.id.custom_edit_text)
        withParent {
            withId(R.id.phone_reg)
        }
    }

    val loginText: KEditText = KEditText {
        withId(com.bav.core.R.id.custom_edit_text)
        withParent {
            withId(R.id.login_reg)
        }
    }

    val passwordText: KEditText = KEditText {
        withId(com.bav.core.R.id.custom_edit_text)
        withParent {
            withId(R.id.password_reg)
        }
    }

    val check: KCheckBox = KCheckBox {
        withId(R.id.personal_data_check)
    }

    val registrationButton: KButton = KButton {
        withId(R.id.registration_button)
    }
}
