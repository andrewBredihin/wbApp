package com.bav.wbapp.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bav.core.R
import com.bav.core.getNavController
import com.bav.core.navigate
import com.bav.core.setupImage
import com.bav.wbapp.AuthActivity
import com.bav.wbapp.databinding.ProfileScreenBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileScreen : Fragment() {
    private val viewModel: ProfileViewModel by viewModel()

    private var _binding: ProfileScreenBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = ProfileScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindToolbar()
        initViews()
        observeData()
    }

    private fun initViews() {
        viewModel.loadProfile()

        with(binding) {
            phone.customTextTitle.text = context?.getString(R.string.profile_phone)
            email.customTextTitle.text = context?.getString(R.string.profile_email)
            dateOfBirth.customTextTitle.text = context?.getString(R.string.profile_date_of_birth)

            logoutButton.setOnClickListener {
                viewModel.logout {
                    requireActivity().navigate(AuthActivity::class.java)
                }
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.profileDataState.collect { result ->
                when(result) {
                    ProfileDataState.Default -> {
                        binding.loading.visibility = View.INVISIBLE
                        renderVisibility(View.INVISIBLE)
                    }

                    ProfileDataState.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                        renderVisibility(View.INVISIBLE)
                    }

                    ProfileDataState.Error   -> {
                        binding.loading.visibility = View.INVISIBLE
                        renderVisibility(View.INVISIBLE)
                    }

                    is ProfileDataState.Loaded  -> {
                        result.response?.let { body ->
                            binding.loading.visibility = View.INVISIBLE
                            renderVisibility(View.VISIBLE)

                            binding.name.text = body.name
                            binding.phone.customTextContent.text = body.phone
                            binding.email.customTextContent.text = body.email
                            binding.dateOfBirth.customTextContent.apply {
                                if (!body.birthday.isNullOrEmpty()) {
                                    text = body.birthday
                                } else {
                                    text = context?.getString(R.string.not_data)
                                    context?.getColor(R.color.cool_grey)?.let { color ->
                                        setTextColor(color)
                                    }
                                }
                            }

                            body.imageUrl?.let { url ->
                                binding.profilePhoto.setupImage(url)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun renderVisibility(visibility: Int) {
        binding.name.visibility = visibility
        binding.phone.customTextContent.visibility = visibility
        binding.phone.customTextTitle.visibility = visibility
        binding.email.customTextContent.visibility = visibility
        binding.email.customTextTitle.visibility = visibility
        binding.dateOfBirth.customTextContent.visibility = visibility
        binding.dateOfBirth.customTextTitle.visibility = visibility
        binding.separator.visibility = visibility
        binding.profilePhoto.visibility = visibility
        binding.editPhoto.visibility = visibility
        binding.logoutButton.visibility = visibility
    }

    private fun bindToolbar() {
        val toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            getNavController().popBackStack()
        }
        binding.basketButton.setOnClickListener {
            // TODO() кнопка корзины
        }
        binding.editButton.setOnClickListener {
            navigate(ProfileScreenDirections.actionProfileScreenToProfileEditScreen())
        }
    }
}