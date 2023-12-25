package com.bav.wbapp.profile.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bav.core.CustomEditTextBinder
import com.bav.core.CustomEditTextInputType
import com.bav.core.R
import com.bav.core.getNavController
import com.bav.wbapp.databinding.ProfileEditScreenBinding
import com.bav.wbapp.profile.ProfileDataState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileEditScreen : Fragment() {

    private val viewModel: ProfileEditViewModel by viewModel()

    private var _binding: ProfileEditScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = ProfileEditScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindToolbar()
        observeData()
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.profileDataState.collect { state ->
                renderLoadingState(state)
            }
        }
        lifecycleScope.launch {
            viewModel.enableEditProfileButton.collect { enabled ->
                binding.editProfileButton.apply {
                    isEnabled = enabled
                    if (enabled) {
                        setTextColor(context.getColor(R.color.white))
                        setText(R.string.edit_profile)
                    } else {
                        setTextColor(context.getColor(R.color.cool_grey))
                        setText(R.string.registration_bad)
                    }
                }
            }
        }
    }

    private fun renderLoadingState(state: ProfileDataState) {
        when (state) {
            ProfileDataState.Default -> {
                renderDefault()
                binding.loading.visibility = View.INVISIBLE
                renderVisibility(View.INVISIBLE)
                renderPasswordFieldsVisibility(View.GONE)
                viewModel.loadProfile()
            }

            ProfileDataState.Loading -> {
                binding.loading.visibility = View.VISIBLE
                renderVisibility(View.INVISIBLE)
            }

            ProfileDataState.Error -> {

            }

            is ProfileDataState.Loaded -> {
                state.response?.let { body ->
                    binding.loading.visibility = View.INVISIBLE
                    renderVisibility(View.VISIBLE)

                    val nameAndLastName = body.name.split(' ')
                    binding.editName.customEditText.setText(nameAndLastName[0])
                    binding.editLastName.customEditText.setText(nameAndLastName.getOrNull(1) ?: "")
                    binding.editDateOfBirth.customEditText.setText(body.birthday)
                    binding.editPhone.customEditText.setText(body.phone)
                    binding.editEmail.customEditText.setText(body.email)
                }
            }
        }

    }

    private fun renderDefault() {
        with(binding) {
            val context = binding.root.context
            CustomEditTextBinder(
                titleView = editName.customTitle,
                editTextView = editName.customEditText,
                title = context.getString(R.string.name),
                inputTypeId = CustomEditTextInputType.Name,
                targetColor = R.color.tangerine_two,
                notTargetColor = R.color.cool_grey,
                enterCallback = { newValue ->
                    viewModel.updateNameFlow(newValue)
                }
            ).bind()
            CustomEditTextBinder(
                titleView = editLastName.customTitle,
                editTextView = editLastName.customEditText,
                title = context.getString(R.string.last_name),
                inputTypeId = CustomEditTextInputType.Name,
                targetColor = R.color.tangerine_two,
                notTargetColor = R.color.cool_grey,
                enterCallback = { newValue ->
                    viewModel.updateLastNameFlow(newValue)
                }
            ).bind()
            CustomEditTextBinder(
                titleView = editDateOfBirth.customTitle,
                editTextView = editDateOfBirth.customEditText,
                title = context.getString(R.string.profile_date_of_birth),
                inputTypeId = CustomEditTextInputType.Date,
                targetColor = R.color.tangerine_two,
                notTargetColor = R.color.cool_grey,
                enterCallback = { newValue ->
                    viewModel.updateDateFlow(newValue)
                }
            ).bind()
            CustomEditTextBinder(
                titleView = editPhone.customTitle,
                editTextView = editPhone.customEditText,
                title = context.getString(R.string.profile_phone),
                inputTypeId = CustomEditTextInputType.Phone,
                targetColor = R.color.tangerine_two,
                notTargetColor = R.color.cool_grey,
                enterCallback = { newValue ->
                    viewModel.updatePhoneFlow(newValue)
                }
            ).bind()
            CustomEditTextBinder(
                titleView = editEmail.customTitle,
                editTextView = editEmail.customEditText,
                title = context.getString(R.string.profile_email),
                inputTypeId = CustomEditTextInputType.Email,
                targetColor = R.color.tangerine_two,
                notTargetColor = R.color.cool_grey,
                enterCallback = { newValue ->
                    viewModel.updateEmailFlow(newValue)
                }
            ).bind()

            CustomEditTextBinder(
                titleView = editPassword.customTitle,
                editTextView = editPassword.customEditText,
                rightClickView = editPassword.customEditRightClick,
                title = context.getString(R.string.new_password),
                inputTypeId = CustomEditTextInputType.Password,
                targetColor = R.color.tangerine_two,
                notTargetColor = R.color.cool_grey,
                enterCallback = { newValue ->
                    viewModel.updatePasswordFlow(newValue)
                }
            ).bind()

            CustomEditTextBinder(
                titleView = confirmPassword.customTitle,
                editTextView = confirmPassword.customEditText,
                rightClickView = confirmPassword.customEditRightClick,
                title = context.getString(R.string.confirm_password),
                inputTypeId = CustomEditTextInputType.Password,
                targetColor = R.color.tangerine_two,
                notTargetColor = R.color.cool_grey,
                enterCallback = { newValue ->
                    viewModel.updateConfirmPasswordFlow(newValue)
                }
            ).bind()

            editPasswordCheck.setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateEditPasswordCheckFlow(isChecked)
                if(isChecked) {
                    renderPasswordFieldsVisibility(View.VISIBLE)
                } else {
                    renderPasswordFieldsVisibility(View.GONE)
                }
            }

            // FIXME() сервер не обрабатывает этот запрос
            editProfileButton.setOnClickListener {
                viewModel.editProfile {
                    getNavController().popBackStack()
                }
            }
        }
    }

    private fun renderVisibility(visibility: Int) {
        binding.editName.customEditText.visibility = visibility
        binding.editLastName.customEditText.visibility = visibility
        binding.editDateOfBirth.customEditText.visibility = visibility
        binding.editPhone.customEditText.visibility = visibility
        binding.editEmail.customEditText.visibility = visibility
        binding.editProfileButton.visibility = visibility
    }
    private fun renderPasswordFieldsVisibility(visibility: Int) {
        binding.confirmPassword.customTitle.visibility = visibility
        binding.confirmPassword.customEditText.visibility = visibility
        binding.confirmPassword.customEditRightClick.visibility = visibility

        binding.editPassword.customTitle.visibility = visibility
        binding.editPassword.customEditText.visibility = visibility
        binding.editPassword.customEditRightClick.visibility = visibility
    }

    private fun bindToolbar() {
        val toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            getNavController().popBackStack()
        }
        binding.basketButton.setOnClickListener {
            // TODO() кнопка корзины
        }
    }
}