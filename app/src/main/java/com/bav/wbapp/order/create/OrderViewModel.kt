package com.bav.wbapp.order.create

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bav.core.profile.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class OrderViewModel(
    private val userRepository: ProfileRepository
) : ViewModel() {

    companion object {
        const val PHONE_TEMPLATE = "([8 ]|[\\+7 ])?(\\d{3}) (\\d{3})-(\\d{2})-(\\d{2})"
    }

    private val _createOrderState: MutableStateFlow<CreateOrderState> = MutableStateFlow(CreateOrderState(isLoading = true))
    val createOrderState = _createOrderState.asStateFlow()

    fun startAction(action: CreateOrderAction) {
        when (action) {
            CreateOrderAction.LoadingAction    -> {
                _createOrderState.value = _createOrderState.value.copy(
                    isLoading = true
                )
                loadUserInfo()
            }

            is CreateOrderAction.SetOrderType  -> {
                _createOrderState.value = _createOrderState.value.copy(
                    type = action.type,
                    continueEnabled = checkContinueButtonEnabled(type = action.type)
                )
            }

            is CreateOrderAction.SetAddress    -> {
                _createOrderState.value = _createOrderState.value.copy(
                    address = action.address,
                    continueEnabled = checkContinueButtonEnabled(address = action.address)
                )
            }

            is CreateOrderAction.SetApartment  -> {
                _createOrderState.value = _createOrderState.value.copy(
                    apartment = action.apartment,
                    continueEnabled = checkContinueButtonEnabled()
                )
            }

            is CreateOrderAction.SetCode       -> {
                _createOrderState.value = _createOrderState.value.copy(
                    code = action.code,
                    continueEnabled = checkContinueButtonEnabled()
                )
            }

            is CreateOrderAction.SetCommentary -> {
                _createOrderState.value = _createOrderState.value.copy(
                    commentary = action.commentary,
                    continueEnabled = checkContinueButtonEnabled()
                )
            }

            is CreateOrderAction.SetDate       -> {
                _createOrderState.value = _createOrderState.value.copy(
                    date = action.date,
                    continueEnabled = checkContinueButtonEnabled(date = action.date)
                )
            }

            is CreateOrderAction.SetEmail      -> {
                _createOrderState.value = _createOrderState.value.copy(
                    email = action.email,
                    continueEnabled = checkContinueButtonEnabled(email = action.email)
                )
            }

            is CreateOrderAction.SetEntrance   -> {
                _createOrderState.value = _createOrderState.value.copy(
                    entrance = action.entrance,
                    continueEnabled = checkContinueButtonEnabled()
                )
            }

            is CreateOrderAction.SetFloor      -> {
                _createOrderState.value = _createOrderState.value.copy(
                    floor = action.floor,
                    continueEnabled = checkContinueButtonEnabled()
                )
            }

            is CreateOrderAction.SetLastName   -> {
                _createOrderState.value = _createOrderState.value.copy(
                    lastName = action.lastName,
                    continueEnabled = checkContinueButtonEnabled()
                )
            }

            is CreateOrderAction.SetName       -> {
                _createOrderState.value = _createOrderState.value.copy(
                    name = action.name,
                    continueEnabled = checkContinueButtonEnabled(name = action.name)
                )
            }

            is CreateOrderAction.SetPhone      -> {
                _createOrderState.value = _createOrderState.value.copy(
                    phone = action.phone,
                    continueEnabled = checkContinueButtonEnabled(phone = action.phone)
                )
            }

            is CreateOrderAction.Loaded        -> {
                _createOrderState.value = _createOrderState.value.copy(
                    isLoaded = true
                )
            }
        }
    }

    private fun loadUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = userRepository.loadProfile().body
                withContext(Dispatchers.Main) {
                    if (user != null) {
                        val nameArray = user.name.split(' ')
                        _createOrderState.value = _createOrderState.value.copy(
                            isLoading = false,
                            name = nameArray[0],
                            lastName = if (nameArray.size == 2) nameArray[1] else "",
                            email = user.email,
                            phone = user.phone,
                            continueEnabled = checkContinueButtonEnabled()
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("createOrder", e.message.toString())
            }
        }
    }

    private fun checkContinueButtonEnabled(
        name: String = _createOrderState.value.name,
        phone: String = _createOrderState.value.phone,
        email: String = _createOrderState.value.email,
        address: String = _createOrderState.value.address,
        date: LocalDateTime? = _createOrderState.value.date,
        type: OrderType = _createOrderState.value.type
    ): Boolean {
        val checkOrderTypeFields: Boolean = when (type) {
            OrderType.DELIVERY -> {
                address.isNotEmpty()
            }

            OrderType.TIME_IN  -> {
                address.isNotEmpty() && date != null
            }

            OrderType.PICKUP   -> {
                // TODO
                true
            }
        }
        val checkPhone: Boolean = !PHONE_TEMPLATE.toRegex().find(phone)?.value.isNullOrEmpty()
        val checkEmail: Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        return checkOrderTypeFields &&
                checkPhone &&
                checkEmail &&
                name.isNotEmpty()
    }

    fun getName(): String {
        val lastName = "${_createOrderState.value.lastName} "
        val name = _createOrderState.value.name
        return "$lastName$name"
    }
    fun getPhone() = _createOrderState.value.phone
    fun getAddress() = _createOrderState.value.address
    fun getDate() = _createOrderState.value.date.toString()
    fun getType() = _createOrderState.value.type

    fun clearState() {
        _createOrderState.value = CreateOrderState(isLoading = true)
    }
}