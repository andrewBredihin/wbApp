package com.bav.wbapp.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.widget.Toolbar.LayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bav.core.getNavController
import com.bav.core.navigate
import com.bav.wbapp.databinding.MenuScreenBinding
import com.bav.wbapp.order.OrderActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenuScreen : Fragment() {

    private val viewModel: MenuScreenViewModel by viewModel()

    private var _binding: MenuScreenBinding? = null
    private val binding get() = _binding!!

    private var _currentAdapter: MenuScreenAdapter? = null
    private val currentAdapter get() = _currentAdapter!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = MenuScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeData()
    }

    private fun initViews() {
        viewModel.loadAllProducts()

        /** Добавление товара в корзину (в БД) */
        _currentAdapter = MenuScreenAdapter { position, amount ->
            _currentAdapter?.let {
                val product = it.getProduct(position)
                viewModel.updateProductInBasket(product, amount)
            }
        }
        binding.menuRecycler.apply {
            adapter = currentAdapter
        }

        val navIcon = binding.menuToolbar.navigationIcon
        binding.menuToolbar.setNavigationOnClickListener {
            getNavController().popBackStack()
        }
        binding.menuToolbarBasketButton.setOnClickListener {
            requireActivity().navigate(OrderActivity::class.java)
        }

        binding.menuSearchField.apply {
            setOnSearchClickListener {
                binding.menuToolbarTitle.visibility = View.GONE
                binding.menuToolbarBasketButton.visibility = View.GONE
                layoutParams.width = LayoutParams.MATCH_PARENT
                binding.menuToolbar.navigationIcon = null
            }

            setOnCloseListener {
                binding.menuToolbarTitle.visibility = View.VISIBLE
                binding.menuToolbarBasketButton.visibility = View.VISIBLE
                layoutParams.width = LayoutParams.WRAP_CONTENT
                binding.menuToolbar.navigationIcon = navIcon
                false
            }

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.updateSearchFlow(newText)
                    return true
                }
            })
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.menuDataState.collect { state ->
                when(state) {
                    MenuDataState.Default   -> {}
                    MenuDataState.Error     -> {}
                    MenuDataState.Loading   -> {}
                    is MenuDataState.Loaded -> {
                        currentAdapter.submitList(state.data)
                    }
                }
            }
        }
    }
}