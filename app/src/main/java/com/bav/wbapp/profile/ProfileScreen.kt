package com.bav.wbapp.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bav.core.R
import com.bav.core.getNavController
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
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.profileDataState.collect { result ->
                when(result) {
                    ProfileDataState.Default -> {
                        binding.loading.visibility = View.INVISIBLE
                    }

                    ProfileDataState.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                    }

                    ProfileDataState.Error   -> {
                        binding.loading.visibility = View.INVISIBLE
                    }

                    is ProfileDataState.Loaded  -> {
                        result.response?.let { body ->
                            binding.loading.visibility = View.INVISIBLE

                            binding.name.text = body.name
                            binding.phone.customTextContent.text = body.phone
                            binding.email.customTextContent.text = body.email
                            binding.dateOfBirth.customTextContent.text = body.birthday
                        }
                    }
                }
            }
        }
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
            // TODO() кнопка редактирования профиля
        }
    }
}