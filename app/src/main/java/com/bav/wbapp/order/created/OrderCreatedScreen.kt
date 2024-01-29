package com.bav.wbapp.order.created

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bav.core.navigate
import com.bav.wbapp.MainActivity
import com.bav.wbapp.databinding.OrderCreatedScreenBinding

class OrderCreatedScreen : Fragment() {

    private val args: OrderCreatedScreenArgs by navArgs()

    private lateinit var binding: OrderCreatedScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = OrderCreatedScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initArgs()

        binding.toMenuButton.setOnClickListener {
            requireActivity().navigate(MainActivity::class.java)
            requireActivity().finish()
        }
    }

    private fun initArgs() {
        val orderNum = args.orderNum
        binding.orderNumber.text = orderNum

        val deliveryTime = args.deliveryTime
        if (deliveryTime.isNullOrEmpty()) {
            binding.deliveryTime.visibility = View.GONE
            binding.deliveryTimeText.visibility = View.GONE
        } else {
            binding.deliveryTime.text = deliveryTime

            binding.deliveryTime.visibility = View.VISIBLE
            binding.deliveryTimeText.visibility = View.VISIBLE
        }
    }
}