package com.bav.wbapp.order.apply

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bav.core.getNavController
import com.bav.core.navigate
import com.bav.wbapp.R
import com.bav.wbapp.databinding.ApplyOrderScreenBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random


class ApplyOrderScreen : Fragment() {

    private val args: ApplyOrderScreenArgs by navArgs()
    private val viewModel: ApplyOrderViewModel by viewModel()

    private lateinit var binding: ApplyOrderScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = ApplyOrderScreenBinding.inflate(inflater, container, false)
        initToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun initArgs() {
        with(binding) {
            toPay.text = "₽ ${args.price}"

            val guestsNumber = resources.getQuantityString(R.plurals.guests_number, args.guestsAmount, args.guestsAmount)
            val menuAmount = viewModel.getMenuAmount()
            val menuNumber = resources.getQuantityString(R.plurals.menu_number, menuAmount, menuAmount)
            menuText.text = "$guestsNumber, $menuNumber"

            name.text = args.name
            phone.text = args.phone

            val addressText = args.address
            val dateText = args.date

            address.visibility = View.GONE
            time.visibility = View.GONE
            timeText.visibility = View.GONE
            timeSeparator.visibility = View.GONE
            if (addressText != null) {
                address.text = args.address
                address.visibility = View.VISIBLE

                if (dateText == null) {
                    time.text = "1ч 20мин"

                    time.visibility = View.VISIBLE
                    timeText.visibility = View.VISIBLE
                    timeSeparator.visibility = View.VISIBLE

                    viewModel.startAction(ApplyOrderAction.SetDeliveryTime("1ч 20мин"))
                }
            }
        }
    }

    private fun initPaymentTypes() {
        with(binding) {
            cardToCourierCheck.isChecked = true
            cardInAppCheck.isChecked = false
            cashToCourierCheck.isChecked = false

            changeRequiredCheck.visibility = View.GONE
            changeRequiredText.visibility = View.GONE
            moneyAvailable.visibility = View.GONE
            moneyAvailableText.visibility = View.GONE

            applyOrderButton.visibility = View.VISIBLE
            bankCardButton.visibility = View.INVISIBLE
        }
    }

    private fun initPaymentTypesListeners() {
        with(binding) {
            cardToCourierCheck.setOnClickListener {
                cardToCourierCheck.isChecked = true
                cardInAppCheck.isChecked = false
                cashToCourierCheck.isChecked = false

                cardToCourierText.setTextColor(Color.WHITE)
                cardInAppText.setTextColor(requireContext().getColor(R.color.cool_grey))
                cashToCourierText.setTextColor(requireContext().getColor(R.color.cool_grey))

                changeRequiredCheck.visibility = View.GONE
                changeRequiredText.visibility = View.GONE
                moneyAvailable.visibility = View.GONE
                moneyAvailableText.visibility = View.GONE

                applyOrderButton.visibility = View.VISIBLE
                bankCardButton.visibility = View.INVISIBLE
            }

            cardInAppCheck.setOnClickListener {
                cardInAppCheck.isChecked = true
                cardToCourierCheck.isChecked = false
                cashToCourierCheck.isChecked = false

                cardInAppText.setTextColor(Color.WHITE)
                cardToCourierText.setTextColor(requireContext().getColor(R.color.cool_grey))
                cashToCourierText.setTextColor(requireContext().getColor(R.color.cool_grey))

                changeRequiredCheck.visibility = View.GONE
                changeRequiredText.visibility = View.GONE
                moneyAvailable.visibility = View.GONE
                moneyAvailableText.visibility = View.GONE

                applyOrderButton.visibility = View.INVISIBLE
                bankCardButton.visibility = View.VISIBLE
            }

            cashToCourierCheck.setOnClickListener {
                cashToCourierCheck.isChecked = true
                cardToCourierCheck.isChecked = false
                cardInAppCheck.isChecked = false

                cashToCourierText.setTextColor(Color.WHITE)
                cardToCourierText.setTextColor(requireContext().getColor(R.color.cool_grey))
                cardInAppText.setTextColor(requireContext().getColor(R.color.cool_grey))

                changeRequiredCheck.visibility = View.VISIBLE
                changeRequiredText.visibility = View.VISIBLE

                if (changeRequiredCheck.isChecked) {
                    moneyAvailable.visibility = View.VISIBLE
                    moneyAvailableText.visibility = View.VISIBLE
                }

                applyOrderButton.visibility = View.VISIBLE
                bankCardButton.visibility = View.INVISIBLE
            }
        }
    }

    private fun initChangeRequiredCheck() {
        with(binding) {
            changeRequiredCheck.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    moneyAvailable.visibility = View.VISIBLE
                    moneyAvailableText.visibility = View.VISIBLE
                } else {
                    moneyAvailable.visibility = View.GONE
                    moneyAvailableText.visibility = View.GONE
                }
            }
        }
    }

    private fun initButtons() {
        binding.applyOrderButton.setOnClickListener { viewModel.startAction(ApplyOrderAction.CreateOrder) }

        binding.bankCardButton.setOnClickListener { viewModel.startAction(ApplyOrderAction.CreateOrder) }
    }

    private fun renderLoading() {
        binding.container.visibility = View.INVISIBLE
        binding.progress.visibility = View.VISIBLE

        viewModel.startAction(ApplyOrderAction.StartLoading)
    }
    private fun renderCreatingOrder() {
        binding.container.visibility = View.INVISIBLE
        binding.progress.visibility = View.VISIBLE
    }


    private fun renderLoaded() {
        val deliveryTime = viewModel.getDeliveryTime()
        val orderNum = Random.nextInt(999999)
        val action = ApplyOrderScreenDirections.actionApplyOrderScreenToOrderCreatedScreen("№ $orderNum", deliveryTime)
        navigate(action)
    }

    private fun renderData() {
        binding.container.visibility = View.VISIBLE
        binding.progress.visibility = View.INVISIBLE

        initArgs()
        initPaymentTypes()
        initPaymentTypesListeners()
        initChangeRequiredCheck()
        initButtons()
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.applyOrderState.collect { state ->
                if (state.isLoaded) {
                    renderLoaded()
                } else if (state.isCreatingOrder) {
                    renderCreatingOrder()
                } else if (state.isLoading) {
                    renderLoading()
                } else {
                    renderData()
                }
            }
        }
    }

    private fun initToolbar() {
        binding.applyOrderToolbar.setNavigationOnClickListener {
            getNavController().popBackStack()
        }
    }

}