package com.bav.wbapp.restaurants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bav.core.getNavController
import com.bav.wbapp.databinding.RestaurantMapScreenBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class RestaurantsMapScreen : Fragment() {

    companion object {
        private val START_POINT = Point(55.755218, 37.622521)
        private const val START_ZOOM = 10f
        private const val SELECT_ZOOM = 16f
        private val START_POSITION = CameraPosition(START_POINT, START_ZOOM, 0f, 0f)
    }

    private val viewModel: RestaurantsMapViewModel by activityViewModel()
    private lateinit var binding: RestaurantMapScreenBinding
    private lateinit var mapObjectCollection: MapObjectCollection

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = RestaurantMapScreenBinding.inflate(inflater, container, false)
        MapKitFactory.initialize(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMap()
        initToolbar()
        observeData()
    }

    private fun initMap() {
        with(binding) {
            /** Init map */
            mapView.map.apply {
                move(START_POSITION)
            }

            /** Init restaurants markers */
            val marker = com.bav.core.R.drawable.map_marker
            mapObjectCollection = mapView.map.mapObjects
            viewModel.getRestaurants().forEach { restaurantInfo ->
                mapObjectCollection.addPlacemark {
                    it.geometry = restaurantInfo.point
                    it.setIcon(ImageProvider.fromResource(requireContext(), marker))
                    it.addTapListener { _, _ ->
                        viewModel.setRestaurant(restaurantInfo)
                        area.text = restaurantInfo.area
                        address.text = restaurantInfo.address
                        mapView.map.apply {
                            move(CameraPosition(restaurantInfo.point, SELECT_ZOOM, 0f, 0f))
                        }
                        true
                    }
                }
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.currentRestaurant.collect { currentRestaurant ->
                if (currentRestaurant != null) {
                    binding.area.text = currentRestaurant.area
                    binding.address.text = currentRestaurant.address

                    binding.mapView.map.apply {
                        move(CameraPosition(currentRestaurant.point, SELECT_ZOOM, 0f, 0f))
                    }
                }
            }
        }
    }

    private fun initToolbar() {
        binding.mapToolbar.setNavigationOnClickListener {
            getNavController().popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}