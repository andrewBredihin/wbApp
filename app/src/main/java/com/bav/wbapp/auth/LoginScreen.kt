package com.bav.wbapp.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bav.core.CustomEditTextBinder
import com.bav.core.CustomEditTextInputType
import com.bav.core.R
import com.bav.core.ToolbarActivity
import com.bav.core.navigate
import com.bav.wbapp.MainActivity
import com.bav.wbapp.databinding.LoginScreenBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class LoginScreen : Fragment() {

    private val viewModel: AuthViewModel by activityViewModel()

    private var _binding: LoginScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = LoginScreenBinding.inflate(inflater, container, false)
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
                titleView = login.customTitle,
                editTextView = login.customEditText,
                title = context.getString(R.string.email),
                inputTypeId = CustomEditTextInputType.Email,
                targetColor = R.color.tangerine_two,
                notTargetColor = R.color.cool_grey,
                enterCallback = { value ->
                    viewModel.updateLoginState(LoginAction.UpdateEmailAction(value))
                }
            ).bind()
            CustomEditTextBinder(
                titleView = password.customTitle,
                editTextView = password.customEditText,
                rightClickView = password.customEditRightClick,
                title = context.getString(R.string.password),
                inputTypeId = CustomEditTextInputType.Password,
                targetColor = R.color.tangerine_two,
                notTargetColor = R.color.cool_grey,
                enterCallback = { value ->
                    viewModel.updateLoginState(LoginAction.UpdatePasswordAction(value))
                }
            ).bind()

            loginButton.setOnClickListener {
                viewModel.updateLoginState(LoginAction.LoadingAction)
            }

            registration.setOnClickListener {
                navigate(LoginScreenDirections.actionLoginScreenToRegistrationScreen())
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                if (state.isAuth) {
                    requireActivity().navigate(MainActivity::class.java)
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
        binding.progress.visibility = View.VISIBLE
        binding.loginButton.isEnabled = false
        binding.login.customEditText.isEnabled = false
        binding.password.customEditText.isEnabled = false
    }
    private fun renderLoaded(state: LoginState) {
        binding.loginButton.apply {
            isEnabled = state.isEnabled
            val textColor = if (state.isEnabled) {
                R.color.white
            } else {
                R.color.cool_grey
            }
            setTextColor(context.getColor(textColor))
        }
        if (state.errorMessage.isNotEmpty()) {
            showError(state.errorMessage)
        }
        if (binding.progress.visibility != View.INVISIBLE) {
            binding.progress.visibility = View.INVISIBLE
            binding.login.customEditText.isEnabled = true
            binding.password.customEditText.isEnabled = true
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