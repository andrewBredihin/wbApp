package com.bav.wbapp.auth.registration

import com.bav.core.auth.AuthorizationRepository
import com.bav.core.auth.AuthorizationRepositoryMock
import com.bav.wbapp.auth.RegistrationAction
import com.bav.wbapp.auth.login.MainDispatcherRule
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

class RegistrationViewModelTest {

    private val authRepository: AuthorizationRepository = AuthorizationRepositoryMock()
    private val viewModel: RegistrationViewModel = RegistrationViewModel(authRepository)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun check_set_check() {
        viewModel.updateRegistrationState(RegistrationAction.UpdateCheckAction(true))
        val actual = viewModel.registrationState.value.isCheck
        Assert.assertEquals(actual, true)
    }

    @Test
    fun check_set_email() {
        val email = "test@mail.ru"
        viewModel.updateRegistrationState(RegistrationAction.UpdateEmailAction(email))
        val actual = viewModel.registrationState.value.email
        Assert.assertEquals(actual, email)
    }

    @Test
    fun check_set_name() {
        val name = "test name"
        viewModel.updateRegistrationState(RegistrationAction.UpdateNameAction(name))
        val actual = viewModel.registrationState.value.name
        Assert.assertEquals(actual, name)
    }

    @Test
    fun check_set_phone() {
        val phone = "8 927 100-10-10"
        viewModel.updateRegistrationState(RegistrationAction.UpdatePhoneAction(phone))
        val actual = viewModel.registrationState.value.phone
        Assert.assertEquals(actual, phone)
    }

    @Test
    fun check_set_password() {
        val password = "123456"
        viewModel.updateRegistrationState(RegistrationAction.UpdatePasswordAction(password))
        val actual = viewModel.registrationState.value.password
        Assert.assertEquals(actual, password)
    }

    @Test
    fun check_button_enabled() {
        val email = "test@mail.ru"
        val name = "test name"
        val phone = "8 927 100-10-10"
        val password = "123456"

        var actual = viewModel.registrationState.value.isEnabled
        Assert.assertEquals(actual, false)

        viewModel.updateRegistrationState(RegistrationAction.UpdateCheckAction(true))
        actual = viewModel.registrationState.value.isEnabled
        Assert.assertEquals(actual, false)

        viewModel.updateRegistrationState(RegistrationAction.UpdateEmailAction(email))
        actual = viewModel.registrationState.value.isEnabled
        Assert.assertEquals(actual, false)

        viewModel.updateRegistrationState(RegistrationAction.UpdateNameAction(name))
        actual = viewModel.registrationState.value.isEnabled
        Assert.assertEquals(actual, false)

        viewModel.updateRegistrationState(RegistrationAction.UpdatePhoneAction(phone))
        actual = viewModel.registrationState.value.isEnabled
        Assert.assertEquals(actual, false)

        viewModel.updateRegistrationState(RegistrationAction.UpdatePasswordAction(password))
        actual = viewModel.registrationState.value.isEnabled
        Assert.assertEquals(actual, true)
    }

    @Test
    fun test_registration() = runTest {
        viewModel.updateRegistrationState(RegistrationAction.LoadingAction)

        var actual = viewModel.registrationState.value.isRegistration
        repeat(1000) {
            if (actual) return@repeat
            else {
                actual = viewModel.registrationState.value.isRegistration
                println(viewModel.registrationState.value)
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