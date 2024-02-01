package com.bav.wbapp.auth.login

import com.bav.core.auth.AuthorizationRepository
import com.bav.core.auth.AuthorizationRepositoryMock
import com.bav.wbapp.auth.LoginAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class LoginViewModelTest {

    private val authRepository: AuthorizationRepository = AuthorizationRepositoryMock()
    private val viewModel: LoginViewModel = LoginViewModel(authRepository)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun check_set_email() {
        val email = "test@mail.ru"
        viewModel.updateLoginState(LoginAction.UpdateEmailAction(email))
        val actual = viewModel.loginState.value.email
        Assert.assertEquals(actual, email)
    }

    @Test
    fun check_set_password() {
        val password = "123456"
        viewModel.updateLoginState(LoginAction.UpdatePasswordAction(password))
        val actual = viewModel.loginState.value.password
        Assert.assertEquals(actual, password)
    }

    @Test
    fun check_start_loading() {
        viewModel.updateLoginState(LoginAction.LoadingAction)
        val actual = viewModel.loginState.value.isLoading
        Assert.assertEquals(actual, true)
    }

    @Test
    fun check_button_enabled() {
        val email = "test@mail.ru"
        val password = "123456"
        var actual = viewModel.loginState.value.isEnabled
        Assert.assertEquals(actual, false)

        viewModel.updateLoginState(LoginAction.UpdateEmailAction(email))
        actual = viewModel.loginState.value.isEnabled
        Assert.assertEquals(actual, false)

        viewModel.updateLoginState(LoginAction.UpdatePasswordAction(password))
        actual = viewModel.loginState.value.isEnabled
        Assert.assertEquals(actual, true)
    }

    @Test
    fun test_login() = runTest {
        viewModel.updateLoginState(LoginAction.LoadingAction)

        var actual = viewModel.loginState.value.isAuth
        repeat(1000) {
            if (actual) return@repeat
            else {
                actual = viewModel.loginState.value.isAuth
                println(viewModel.loginState.value)
            }
        }

        Assert.assertEquals(actual, true)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}