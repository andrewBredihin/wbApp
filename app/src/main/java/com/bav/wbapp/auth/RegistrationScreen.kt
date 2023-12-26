package com.bav.wbapp.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bav.core.CustomEditTextInputType
import com.bav.core.ToolbarActivity
import com.bav.core.R
import com.bav.core.customEditTextBinder
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
        render()
    }

    private fun initViews() {
        viewModel.defaultStates()
        with(binding) {
            val context = binding.root.context
            customEditTextBinder(
                titleView = nameReg.customTitle,
                editTextView = nameReg.customEditText,
                title = context.getString(R.string.name),
                inputTypeId = CustomEditTextInputType.Name,
                targetColor = R.color.tangerine_two,
                notTargetColor = R.color.cool_grey,
                enterCallback = { value ->
                    viewModel.updateRegistrationState(RegistrationAction.UpdateNameAction(value))
                }
            )
            customEditTextBinder(
                titleView = phoneReg.customTitle,
                editTextView = phoneReg.customEditText,
                title = context.getString(R.string.phone),
                inputTypeId = CustomEditTextInputType.Phone,
                targetColor = R.color.tangerine_two,
                notTargetColor = R.color.cool_grey,
                enterCallback = { value ->
                    viewModel.updateRegistrationState(RegistrationAction.UpdatePhoneAction(value))
                }
            )
            customEditTextBinder(
                titleView = loginReg.customTitle,
                editTextView = loginReg.customEditText,
                title = context.getString(R.string.email),
                inputTypeId = CustomEditTextInputType.Email,
                targetColor = R.color.tangerine_two,
                notTargetColor = R.color.cool_grey,
                enterCallback = { value ->
                    viewModel.updateRegistrationState(RegistrationAction.UpdateEmailAction(value))
                }
            )
            customEditTextBinder(
                titleView = passwordReg.customTitle,
                editTextView = passwordReg.customEditText,
                rightClickView = passwordReg.customEditRightClick,
                title = context.getString(R.string.password),
                inputTypeId = CustomEditTextInputType.Password,
                targetColor = R.color.tangerine_two,
                notTargetColor = R.color.cool_grey,
                enterCallback = { value ->
                    viewModel.updateRegistrationState(RegistrationAction.UpdatePasswordAction(value))
                }
            )

            personalDataCheck.setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateRegistrationState(RegistrationAction.UpdateCheckAction(isChecked))
            }

            registrationButton.setOnClickListener {
                viewModel.updateRegistrationState(RegistrationAction.LoadingAction)
            }
        }
    }


    private fun render() {
        lifecycleScope.launch {
            viewModel.registrationState.collect { state ->
                if (state.isRegistration) {
                    val loginPage = findNavController().graph.startDestinationId
                    findNavController().navigate(loginPage)
                } else {
                    if (state.isLoading) {
                        renderLoading()
                    } else {
                        renderLoaded(state)
                    }
                }
            }
        }
    }

    private fun renderLoading() {
        with(binding) {
            progress.visibility = View.VISIBLE
            registrationButton.isEnabled = false
            personalDataCheck.isEnabled = false
            nameReg.customEditText.isEnabled = false
            loginReg.customEditText.isEnabled = false
            phoneReg.customEditText.isEnabled = false
            passwordReg.customEditText.isEnabled = false
        }
    }
    private fun renderLoaded(state: RegistrationState) {
        with(binding) {
            registrationButton.apply {
                isEnabled = state.isEnabled
                if (state.isEnabled) {
                    setTextColor(context.getColor(R.color.white))
                    setText(R.string.registration_ok)
                } else {
                    setTextColor(context.getColor(R.color.cool_grey))
                    setText(R.string.registration_bad)
                }
            }
            if (state.errorMessage.isNotEmpty()) {
                showError(state.errorMessage)
            }
            if (progress.visibility != View.INVISIBLE) {
                progress.visibility = View.INVISIBLE
                personalDataCheck.isEnabled = true
                nameReg.customEditText.isEnabled = true
                loginReg.customEditText.isEnabled = true
                phoneReg.customEditText.isEnabled = true
                passwordReg.customEditText.isEnabled = true
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