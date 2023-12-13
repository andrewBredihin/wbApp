package com.bav.wbapp.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bav.core.CustomEditTextBinder
import com.bav.core.CustomEditTextInputType
import com.bav.core.ToolbarActivity
import com.bav.core.R
import com.bav.core.api.ResponseCode
import com.bav.wbapp.databinding.RegistrationScreenBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class RegistrationScreen : Fragment() {

    private val viewModel: AuthViewModel by activityViewModel()

    private var _binding: RegistrationScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = RegistrationScreenBinding.inflate(inflater, container, false)
        (requireActivity() as ToolbarActivity).setupToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeData()
    }

    private fun initViews() {
        with(binding) {
            val context = binding.root.context
            CustomEditTextBinder(
                titleView = nameReg.customTitle,
                editTextView = nameReg.customEditText,
                title = context.getString(R.string.name),
                inputTypeId = CustomEditTextInputType.Name,
                targetColor = R.color.tangerine_two,
                notTargetColor = R.color.cool_grey,
                enterCallback = { value ->
                    viewModel.updateNameRegFlow(value)
                }
            ).bind()
            CustomEditTextBinder(
                titleView = phoneReg.customTitle,
                editTextView = phoneReg.customEditText,
                title = context.getString(R.string.phone),
                inputTypeId = CustomEditTextInputType.Phone,
                targetColor = R.color.tangerine_two,
                notTargetColor = R.color.cool_grey,
                enterCallback = { value ->
                    viewModel.updatePhoneRegFlow(value)
                }
            ).bind()
            CustomEditTextBinder(
                titleView = loginReg.customTitle,
                editTextView = loginReg.customEditText,
                title = context.getString(R.string.email),
                inputTypeId = CustomEditTextInputType.Email,
                targetColor = R.color.tangerine_two,
                notTargetColor = R.color.cool_grey,
                enterCallback = { value ->
                    viewModel.updateLoginRegFlow(value)
                }
            ).bind()
            CustomEditTextBinder(
                titleView = passwordReg.customTitle,
                editTextView = passwordReg.customEditText,
                rightClickView = passwordReg.customEditRightClick,
                title = context.getString(R.string.password),
                inputTypeId = CustomEditTextInputType.Password,
                targetColor = R.color.tangerine_two,
                notTargetColor = R.color.cool_grey,
                enterCallback = { value ->
                    viewModel.updatePasswordRegFlow(value)
                }
            ).bind()

            personalDataCheck.setOnCheckedChangeListener { _, isChecked ->
                viewModel.updatePersonalDataCheck(isChecked)
            }

            registrationButton.setOnClickListener {
                viewModel.registration { response ->
                    when(response.code) {
                        ResponseCode.RESPONSE_SUCCESSFUL -> {
                            val loginPage = findNavController().graph.startDestinationId
                            findNavController().navigate(loginPage)
                        }

                        else                             -> {
                            response.message?.let { respMessage ->
                                val message = if (respMessage != "") {
                                    respMessage
                                } else {
                                    context.getString(R.string.registration_error)
                                }
                                showError(message)
                            }
                        }
                    }
                }
            }
        }
    }


    private fun observeData() {
        lifecycleScope.launch {
            viewModel.enableRegButton.collect { enabled ->
                binding.registrationButton.apply {
                    isEnabled = enabled
                    if (enabled) {
                        setTextColor(context.getColor(R.color.white))
                        setText(R.string.registration_ok)
                    } else {
                        setTextColor(context.getColor(R.color.cool_grey))
                        setText(R.string.registration_bad)
                    }

                }
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}