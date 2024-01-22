package com.bav.wbapp.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.widget.Toolbar.LayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bav.core.getNavController
import com.bav.wbapp.databinding.MenuScreenBinding
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

        /**
         * Callback - переход на страницу с детальной информацией о товаре
         * position -> позиция товара в списке адаптера
         * amount -> выбранное количество товара
         */
        _currentAdapter = MenuScreenAdapter { position, amount ->
            Log.e("TEST", "position: $position, amount: $amount")
            /*val id = currentAdapter.currentList[position].id
            val bundle = Bundle().apply {
                // FIXME() вынести в константы
                putInt("id", id)
                putInt("amount", amount)
            }*/
            // TODO() переход на страницу с детальной информацией о товаре
        }
        binding.menuRecycler.apply {
            adapter = currentAdapter
        }

        val navIcon = binding.menuToolbar.navigationIcon
        binding.menuToolbar.setNavigationOnClickListener {
            getNavController().popBackStack()
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