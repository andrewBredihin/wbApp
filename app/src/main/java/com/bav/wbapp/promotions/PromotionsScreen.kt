package com.bav.wbapp.promotions

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.bav.core.getNavController
import com.bav.core.promotions.Promotion
import com.bav.wbapp.auth.PromotionsAction
import com.bav.wbapp.databinding.PromotionsScreenBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class PromotionsScreen : Fragment() {

    private lateinit var binding: PromotionsScreenBinding

    private val viewModel: PromotionViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = PromotionsScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindToolbar()
        render()
    }

    private fun initViews() {
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((6 * Resources.getSystem().displayMetrics.density).toInt()))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.alpha = (0.5f + r)
        }

        binding.viewPager.apply {
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(compositePageTransformer)
        }
    }

    private fun render() {
        initViews()
        lifecycleScope.launch {
            viewModel.data.collect { state ->
                if (state.isLoading) {
                    renderLoading()
                    viewModel.loadingPromotions(PromotionsAction.LoadingAction)
                } else {
                    renderData(state.data)
                }
            }
        }
    }

    private fun bindToolbar() {
        val toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            getNavController().popBackStack()
        }
    }

    private fun renderData(data: List<Promotion>) {
        with(binding) {
            binding.viewPager.apply {
                adapter = CarouselRVAdapter(data)
            }
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()

            progress.visibility = View.INVISIBLE
            tabLayout.visibility = View.VISIBLE
            viewPager.visibility = View.VISIBLE
        }
    }

    private fun renderLoading() {
        with(binding) {
            progress.visibility = View.VISIBLE
            tabLayout.visibility = View.INVISIBLE
            viewPager.visibility = View.INVISIBLE
        }
    }
}