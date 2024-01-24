package com.bav.wbapp.basket

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bav.core.basket.ProductEntity
import com.bav.core.getNavController
import com.bav.core.navigate
import com.bav.wbapp.R
import com.bav.wbapp.databinding.BasketScreenBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class BasketScreen : Fragment() {

    private val viewModel: BasketViewModel by viewModel()

    private lateinit var binding: BasketScreenBinding

    private var _currentAdapter: BasketScreenAdapter? = null
    private val currentAdapter get() = _currentAdapter!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = BasketScreenBinding.inflate(inflater, container, false)
        initToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Обновить количество товара в корзине в БД */
        _currentAdapter = BasketScreenAdapter { position, amount ->
            _currentAdapter?.let {
                val productId = it.getProductId(position)
                viewModel.updateProductInBasket(productId, amount)
            }
        }
        with(binding) {
            basketRecycler.apply {
                adapter = currentAdapter
            }
            numberOfGuests.apply {
                setText("0")
                setMinAmount(0)
                setMaxAmount(100)
            }

            orderButton.setOnClickListener {
                navigate(BasketScreenDirections.actionBasketScreenToCreateOrderScreen())
            }

            enterPromoButton.setOnClickListener {
                val promoCode = binding.enterPromo.text.toString()
                viewModel.startAction(BasketAction.ActivatePromoCode(promoCode))
            }
        }

        observeData()
    }

    private fun renderData(state: BasketState) {
        with(binding) {
            loading.visibility = View.INVISIBLE
            if (state.data.isNotEmpty()) {
                container.visibility = View.VISIBLE
            }
            _currentAdapter?.submitList(state.data)

            /** Актуальная цена за все товары */
            calculatePrice(state.data)

            /** Promo code */
            when (state.promoCodeStatus) {
                PromoCodeStatus.ERROR  -> {
                    enterPromoText.setTextColor(Color.RED)
                }

                PromoCodeStatus.ENTER  -> {
                    enterPromoText.setTextColor(requireContext().getColor(R.color.cool_grey))
                }

                PromoCodeStatus.ACTIVE -> {
                    enterPromoText.setTextColor(requireContext().getColor(R.color.tangerine_two))
                    enterPromo.setTextColor(requireContext().getColor(R.color.cool_grey))
                    enterPromo.isEnabled = false
                    enterPromoButton.isEnabled = false
                }
            }

            enterPromoText.text = state.promoCodeMessage
        }
    }

    private fun renderLoading() {
        viewModel.startAction(BasketAction.LoadingAction)
        binding.loading.visibility = View.VISIBLE
        binding.container.visibility = View.INVISIBLE
    }


    private fun observeData() {
        lifecycleScope.launch {
            viewModel.basketState.collect { state ->
                if (state.isLoading) {
                    renderLoading()
                } else {
                    renderData(state)
                }
            }
        }
    }

    private fun initToolbar() {
        binding.basketToolbar.setNavigationOnClickListener {
            getNavController().popBackStack()
        }
    }

    private fun calculatePrice(products: List<ProductEntity>) {
        var price = 0f
        val discountValue = 10
        products.forEach { product ->
            price += product.price * product.amountInBasket
        }
        price *= (100f - discountValue) / 100f
        binding.discount.text = "$discountValue%"
        binding.totalWithDiscount.text = "$price P"
    }
}