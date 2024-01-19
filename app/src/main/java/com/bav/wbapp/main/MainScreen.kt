package com.bav.wbapp.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bav.core.navigate
import com.bav.wbapp.databinding.MainScreenBinding
import com.bav.wbapp.main.model.MainCategoryModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainScreen : Fragment() {

    private val viewModel: MainScreenViewModel by viewModel()

    private var _binding: MainScreenBinding? = null
    private val binding get() = _binding!!

    private var _currentAdapter: MainScreenAdapter? = null
    private val currentAdapter get() = _currentAdapter!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = MainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeData()
    }

    private fun initViews() {
        _currentAdapter = MainScreenAdapter { position ->
            val action = when (currentAdapter.currentList[position].id) {
                MainCategoryModel.PROFILE     -> MainScreenDirections.actionMainScreenToProfileScreen()
                MainCategoryModel.STOCK       -> MainScreenDirections.actionMainScreenToPromotionsScreen()
                MainCategoryModel.MENU        -> MainScreenDirections.actionMainScreenToMenuScreen()
                MainCategoryModel.INFO        -> MainScreenDirections.actionMainScreenToInfoScreen()
                MainCategoryModel.REVIEW      -> MainScreenDirections.actionMainScreenToReviewScreen()
                MainCategoryModel.RESTAURANTS -> MainScreenDirections.actionMainScreenToRestaurantsScreen()
                MainCategoryModel.HISTORY     -> MainScreenDirections.actionMainScreenToHistoryScreen2()
                MainCategoryModel.DELIVERY    -> MainScreenDirections.actionMainScreenToDeliveryScreen()

                else                          -> null
            }
            action?.let { navigate(action) }
        }.apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        }
        binding.mainRecycler.apply {
            adapter = currentAdapter
        }
    }

    private fun observeData() {
        viewModel.mainDataList.observe(viewLifecycleOwner) { data ->
            currentAdapter.submitList(data)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}