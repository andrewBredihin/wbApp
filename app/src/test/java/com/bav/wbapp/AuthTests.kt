package com.bav.wbapp

import androidx.core.util.PatternsCompat
import com.bav.wbapp.auth.login.LoginViewModel
import com.bav.wbapp.auth.registration.RegistrationViewModel
import org.junit.Assert
import org.junit.Test

class AuthTests {

    @Test
    fun loginTestTrue() {
        val email = "test@mail.ru"
        val password1 = "123456"
        val password2 = "123456789"

        val result1 = checkLoginFields(email, password1)
        Assert.assertEquals(result1, true)

        val result2 = checkLoginFields(email, password2)
        Assert.assertEquals(result2, true)
    }

    @Test
    fun loginTestFalse() {
        val email1 = "test@mail.ru"
        val email2 = "test@mail"
        val password1 = "12345"
        val password2 = "123456789"

        val result1 = checkLoginFields(email1, password1)
        Assert.assertEquals(result1, false)

        val result2 = checkLoginFields(email2, password1)
        Assert.assertEquals(result2, false)

        val result3 = checkLoginFields(email2, password2)
        Assert.assertEquals(result3, false)
    }

    @Test
    fun registrationTestTrue() {
        val name = "123456"
        val phone = "8 927 100-10-10"
        val email = "test@mail.ru"
        val password = "123456"
        val isCheck = true

        val result = checkRegistrationFields(name, phone, email, password, isCheck)
        Assert.assertEquals(result, true)
    }

    @Test
    fun registrationTestFalse() {
        val name = "123456"
        val phone = "8 927 100-10-10"
        val email = "test@mail.ru"
        val password = "123456"
        val isCheck = true

        val result1 = checkRegistrationFields("12345", phone, email, password, isCheck)
        Assert.assertEquals(result1, false)

        val result2 = checkRegistrationFields(name, "100-10-10", email, password, isCheck)
        Assert.assertEquals(result2, false)

        val result3 = checkRegistrationFields(name, phone, "test@mail", password, isCheck)
        Assert.assertEquals(result3, false)

        val result4 = checkRegistrationFields(name, phone, email, "12345", isCheck)
        Assert.assertEquals(result4, false)

        val result5 = checkRegistrationFields(name, phone, email, password, false)
        Assert.assertEquals(result5, false)
    }

    private fun checkLoginFields(email: String, password: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
                && password.length >= LoginViewModel.MIN_FIELD_LENGTH
    }

    private fun checkRegistrationFields(
        name: String,
        phone: String,
        email: String,
        password: String,
        isCheck: Boolean
    ): Boolean {
        return name.length >= RegistrationViewModel.MIN_FIELD_LENGTH
                && checkPhone(phone)
                && PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
                && password.length >= RegistrationViewModel.MIN_FIELD_LENGTH
                && isCheck
    }

    private fun checkPhone(phone: String): Boolean {
        val result = RegistrationViewModel.PHONE_TEMPLATE.toRegex().find(phone)?.value
        return !result.isNullOrEmpty()
    }
}