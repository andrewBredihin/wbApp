package com.bav.wbapp.order.create

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bav.core.getNavController
import com.bav.core.navigate
import com.bav.wbapp.R
import com.bav.wbapp.databinding.CreateOrderScreenBinding
import com.bav.wbapp.restaurants.RestaurantInfo
import com.bav.wbapp.restaurants.RestaurantsMapViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.Calendar

class CreateOrderScreen : Fragment() {

    companion object {
        private const val DELIVER_ON = "Доставить"
        private const val PICKUP = "Заберу заказ:"
    }

    private val args: CreateOrderScreenArgs by navArgs()
    private val viewModel: OrderViewModel by activityViewModel()
    private val restaurantsViewModel: RestaurantsMapViewModel by activityViewModel()

    private lateinit var binding: CreateOrderScreenBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.startAction(CreateOrderAction.LoadingAction)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CreateOrderScreenBinding.inflate(inflater, container, false)
        initToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOrderTypeContainers()
        initOrderTypeButtons()
        initEditTextFieldsListeners()
        initCalendarButton()
        initApplyButton()
        initChoiceRestaurantButton()
        observeData()
        restaurantObserve()
    }

    private fun initChoiceRestaurantButton() {
        binding.restaurantContainer.setOnClickListener {
            navigate(CreateOrderScreenDirections.actionCreateOrderScreenToRestaurantsMapScreen())
        }
    }

    private fun initApplyButton() {
        binding.applyButton.setOnClickListener {
            val price = args.price
            val guests = args.guestsAmount
            val name = viewModel.getName()
            val phone = viewModel.getPhone()
            val type = viewModel.getType()
            val address = if (type == OrderType.DELIVERY || type == OrderType.TIME_IN) {
                viewModel.getAddress()
            } else {
                null
            }
            val date = if (type == OrderType.PICKUP || type == OrderType.TIME_IN) {
                viewModel.getDate().toString()
            } else {
                null
            }
            val action = CreateOrderScreenDirections.actionCreateOrderScreenToApplyOrderScreen(
                price = price,
                guestsAmount = guests,
                name = name,
                phone = phone,
                address = address,
                date = date
            )
            navigate(action)
        }
    }

    private fun initCalendarButton() {
        binding.dateIcon.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            getDateDialog(year, month, day, hour, minute)
        }
    }

    private fun getDateDialog(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
        DatePickerDialog(requireActivity(), { _, y, m, d ->
            getTimeDialog(y, m + 1, d, hour, minute)
        }, year, month, day).show()
    }

    private fun getTimeDialog(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
        TimePickerDialog(requireActivity(), { _, h, m ->
            val date = LocalDateTime.of(year, month, day, h, m)
            binding.dateText.text = getDateText(date)
            viewModel.startAction(CreateOrderAction.SetDate(date))
        }, hour, minute, true).show()
    }

    private fun initEditTextFieldsListeners() {
        with(binding) {
            // User info
            name.setEditTextListener { text ->
                viewModel.startAction(CreateOrderAction.SetName(text))
            }
            lastName.setEditTextListener { text ->
                viewModel.startAction(CreateOrderAction.SetLastName(text))
            }
            phone.setEditTextListener { text ->
                viewModel.startAction(CreateOrderAction.SetPhone(text))
            }
            email.setEditTextListener { text ->
                viewModel.startAction(CreateOrderAction.SetEmail(text))
            }

            // Delivery info
            address.setEditTextListener { text ->
                viewModel.startAction(CreateOrderAction.SetAddress(text))
            }
            entrance.setEditTextListener { text ->
                viewModel.startAction(CreateOrderAction.SetEntrance(text))
            }
            code.setEditTextListener { text ->
                viewModel.startAction(CreateOrderAction.SetCode(text))
            }
            floor.setEditTextListener { text ->
                viewModel.startAction(CreateOrderAction.SetFloor(text))
            }
            apartment.setEditTextListener { text ->
                viewModel.startAction(CreateOrderAction.SetApartment(text))
            }

            // Commentary
            commentary.setEditTextListener { text ->
                viewModel.startAction(CreateOrderAction.SetCommentary(text))
            }
        }
    }

    private fun initOrderTypeContainers() {
        with(binding) {
            deliveryContainer.visibility = View.VISIBLE
            dateContainer.visibility = View.GONE
            restaurantContainer.visibility = View.GONE

            commentary.setLinesNumber(4)
        }
    }

    private fun initOrderTypeButtons() {
        with(binding) {
            deliveryButton.setOnClickListener { setDelivery() }
            inTimeButton.setOnClickListener { setInTime() }
            pickupButton.setOnClickListener { setPickup() }
        }
    }

    private fun setDelivery() {
        with(binding) {
            deliveryButton.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.order_button_background
            )
            inTimeButton.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.order_button_background_null
            )
            pickupButton.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.order_button_background_null
            )

            deliveryContainer.visibility = View.VISIBLE
            dateContainer.visibility = View.GONE
            restaurantContainer.visibility = View.GONE
        }
        viewModel.startAction(CreateOrderAction.SetOrderType(OrderType.DELIVERY))
    }

    private fun setInTime() {
        with(binding) {
            inTimeButton.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.order_button_background
            )
            deliveryButton.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.order_button_background_null
            )
            pickupButton.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.order_button_background_null
            )

            deliveryContainer.visibility = View.VISIBLE
            dateContainer.visibility = View.VISIBLE
            restaurantContainer.visibility = View.GONE

            dateInfo.text = DELIVER_ON
        }
        viewModel.startAction(CreateOrderAction.SetOrderType(OrderType.TIME_IN))
    }

    private fun setPickup() {
        with(binding) {
            pickupButton.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.order_button_background
            )
            inTimeButton.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.order_button_background_null
            )
            deliveryButton.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.order_button_background_null
            )

            deliveryContainer.visibility = View.GONE
            dateContainer.visibility = View.VISIBLE
            restaurantContainer.visibility = View.VISIBLE

            dateInfo.text = PICKUP
        }
        viewModel.startAction(CreateOrderAction.SetOrderType(OrderType.PICKUP))
    }

    private fun setRestaurant(restaurant: RestaurantInfo) {
        with(binding) {
            restaurantSelect.visibility = View.GONE
            restaurantInfo.visibility = View.VISIBLE
            restaurantName.text = restaurant.area
            restaurantAddress.text = restaurant.address
        }
    }

    private fun renderData(state: CreateOrderState) {
        with(binding) {
            progress.visibility = View.INVISIBLE
            container.visibility = View.VISIBLE

            if (!state.userDataLoaded) {
                name.setText(state.name)
                lastName.setText(state.lastName ?: "")
                email.setText(state.email)
                phone.setText(state.phone)
                address.setText(state.address)
                entrance.setText(state.entrance ?: "")
                code.setText(state.code ?: "")
                floor.setText(state.floor ?: "")
                apartment.setText(state.apartment ?: "")
                commentary.setText(state.commentary)

                viewModel.startAction(CreateOrderAction.SetUserInfoLoaded)
            }

            if (state.date != null) {
                dateText.text = getDateText(state.date)
            } else {
                val date = LocalDateTime.now()
                dateText.text = getDateText(date)
                viewModel.startAction(CreateOrderAction.SetDate(date))
            }

            when(state.type) {
                OrderType.DELIVERY -> setDelivery()
                OrderType.TIME_IN  -> setInTime()
                OrderType.PICKUP   -> {
                    setPickup()
                    if (state.restaurant != null) {
                        setRestaurant(state.restaurant)
                    }
                }
            }

            applyButton.isEnabled = state.continueEnabled
        }
    }

    private fun renderLoading() {
        binding.progress.visibility = View.VISIBLE
        binding.container.visibility = View.INVISIBLE
    }

    private fun observeData() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.createOrderState.collect { state ->
                if (state.isLoading) {
                    renderLoading()
                } else {
                    renderData(state)
                }
            }
        }
    }
    private fun restaurantObserve() {
        lifecycleScope.launch(Dispatchers.Main) {
            restaurantsViewModel.currentRestaurant.collect { restaurant ->
                if (restaurant != null) {
                    viewModel.startAction(CreateOrderAction.SetRestaurant(restaurant))
                }
            }
        }
    }

    private fun initToolbar() {
        binding.createOrederToolbar.setNavigationOnClickListener {
            getNavController().popBackStack()
        }
    }

    private fun getDateText(time: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("DD.MM.yyyy, HH:mm", Locale.ROOT)
        return time.format(formatter)
    }
}