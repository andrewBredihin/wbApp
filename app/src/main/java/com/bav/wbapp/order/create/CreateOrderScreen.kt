package com.bav.wbapp.order.create

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bav.core.getNavController
import com.bav.core.navigate
import com.bav.wbapp.R
import com.bav.wbapp.databinding.CreateOrderScreenBinding
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class CreateOrderScreen : Fragment() {

    companion object {
        private const val DELIVER_ON = "Доставить"
        private const val PICKUP = "Заберу заказ:"
    }

    private val args: CreateOrderScreenArgs by navArgs()
    private val viewModel: OrderViewModel by viewModel()

    private lateinit var binding: CreateOrderScreenBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CreateOrderScreenBinding.inflate(inflater, container, false)
        initToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clearState()

        initOrderTypeContainers()
        initOrderTypeButtons()
        initEditTextFieldsListeners()
        initCalendarButton()
        initApplyButton()
        observeData()
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
                viewModel.getDate()
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

            // Date info
            // TODO -> viewModel.startAction(CreateOrderAction.SetDate(date))

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

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initOrderTypeButtons() {
        with(binding) {
            deliveryButton.setOnClickListener {
                it.background = context?.resources?.getDrawable(
                    R.drawable.order_button_background,
                    requireContext().theme
                )
                inTimeButton.background = context?.resources?.getDrawable(
                    R.drawable.order_button_background_null,
                    requireContext().theme
                )
                pickupButton.background = context?.resources?.getDrawable(
                    R.drawable.order_button_background_null,
                    requireContext().theme
                )

                deliveryContainer.visibility = View.VISIBLE
                dateContainer.visibility = View.GONE
                restaurantContainer.visibility = View.GONE

                viewModel.startAction(CreateOrderAction.SetOrderType(OrderType.DELIVERY))
            }

            inTimeButton.setOnClickListener {
                it.background = context?.resources?.getDrawable(
                    R.drawable.order_button_background,
                    requireContext().theme
                )
                deliveryButton.background = context?.resources?.getDrawable(
                    R.drawable.order_button_background_null,
                    requireContext().theme
                )
                pickupButton.background = context?.resources?.getDrawable(
                    R.drawable.order_button_background_null,
                    requireContext().theme
                )

                deliveryContainer.visibility = View.VISIBLE
                dateContainer.visibility = View.VISIBLE
                restaurantContainer.visibility = View.GONE

                dateInfo.text = DELIVER_ON

                viewModel.startAction(CreateOrderAction.SetOrderType(OrderType.TIME_IN))
            }

            pickupButton.setOnClickListener {
                it.background = context?.resources?.getDrawable(
                    R.drawable.order_button_background,
                    requireContext().theme
                )
                inTimeButton.background = context?.resources?.getDrawable(
                    R.drawable.order_button_background_null,
                    requireContext().theme
                )
                deliveryButton.background = context?.resources?.getDrawable(
                    R.drawable.order_button_background_null,
                    requireContext().theme
                )

                deliveryContainer.visibility = View.GONE
                dateContainer.visibility = View.VISIBLE
                restaurantContainer.visibility = View.VISIBLE

                dateInfo.text = PICKUP

                viewModel.startAction(CreateOrderAction.SetOrderType(OrderType.PICKUP))
            }
        }
    }

    private fun renderData(state: CreateOrderState) {
        with(binding) {
            /** Если данные о пользователе уже были загружены - не загружать их снова */
            if (!state.isLoaded) {
                viewModel.startAction(CreateOrderAction.Loaded)

                progress.visibility = View.INVISIBLE
                container.visibility = View.VISIBLE

                name.setText(state.name)
                lastName.setText(state.lastName ?: "")
                email.setText(state.email)
                phone.setText(state.phone)
            }

            if (state.date != null) {
                dateText.text = getDateText(state.date)
            } else {
                val date = LocalDateTime.now()
                dateText.text = getDateText(date)
                viewModel.startAction(CreateOrderAction.SetDate(date))
            }

            applyButton.isEnabled = state.continueEnabled
        }
    }

    private fun renderLoading() {
        viewModel.startAction(CreateOrderAction.LoadingAction)
        binding.progress.visibility = View.VISIBLE
        binding.container.visibility = View.INVISIBLE
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.createOrderState.collect { state ->
                Log.e("BAZA", state.toString())
                if (state.isLoading) {
                    renderLoading()
                } else {
                    renderData(state)
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