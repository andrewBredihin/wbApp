package com.bav.wbapp.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bav.core.R
import com.bav.core.getNavController
import com.bav.core.navigate
import com.bav.core.setupImage
import com.bav.wbapp.AuthActivity
import com.bav.wbapp.databinding.ProfileScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


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

            editPhoto.setOnClickListener {
                requestPermissions.launch(permissions())
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.profileDataState.collect { result ->
                when (result) {
                    ProfileDataState.Default   -> {
                        binding.loading.visibility = View.INVISIBLE
                        renderVisibility(View.INVISIBLE)
                    }

                    ProfileDataState.Loading   -> {
                        binding.loading.visibility = View.VISIBLE
                        renderVisibility(View.INVISIBLE)
                    }

                    ProfileDataState.Error     -> {
                        binding.loading.visibility = View.INVISIBLE
                        renderVisibility(View.INVISIBLE)
                    }

                    is ProfileDataState.Loaded -> {
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
                                /*val path = "https://mobile-study-java.simbirsoft.dev$url"
                                binding.profilePhoto.setupImage(path)*/
                            }
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.fileImg.collect { uri ->
                val bitmap = BitmapFactory.decodeFile(uri)
                binding.profilePhoto.setImageBitmap(bitmap)
                Log.e("URI","${uri.toString()} | $bitmap")
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

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val imgUri = data?.data
                Log.e("URI 2", imgUri.toString())
                Log.e("URI 3", imgUri?.path.toString())
                //binding.profilePhoto.setImageURI(imgUri)
                /*imgUri?.let { uri ->
                    val cursor = requireActivity().contentResolver.query(
                        uri,
                        arrayOf(MediaStore.Images.ImageColumns.DATA),
                        null,
                        null,
                        null
                    )
                    cursor?.moveToFirst()
                    val path = cursor?.getString(0)
                    cursor?.close()
                    path?.let {
                        viewModel.uploadAvatar(it)
                    }
                }*/

                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        if (imgUri == null) {
                            return@launch
                        }
                        val path = getFIleName(imgUri) ?: return@launch
                        val file = File(path)
                        viewModel.uploadAvatar(file)
                    } catch (e: Exception) {
                        Log.e("uploadFile", e.message.toString())
                    }
                }
            }
        }

    private val storagePermissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private val storagePermissions33 = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES
    )

    private fun permissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storagePermissions33
        } else {
            storagePermissions
        }
    }

    private val requestPermissions =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            var areAllGranted = true
            for (isGranted in result.values) {
                areAllGranted = areAllGranted && isGranted
            }
            if (areAllGranted) {
                val pickImg = Intent(Intent.ACTION_PICK).also {
                    it.type = "image/*"
                    val mineTypes = arrayOf("images/jpeg", "image/png")
                    it.putExtra(Intent.EXTRA_MIME_TYPES, mineTypes)
                }
                changeImage.launch(pickImg)
            } else {
                Toast.makeText(
                    context,
                    R.string.permissions_denied,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun getFIleName(uri: Uri): String? {
        val cursor = requireActivity().contentResolver.query(
            uri,
            arrayOf(MediaStore.Images.ImageColumns.DATA),
            null,
            null,
            null
        )
        cursor?.moveToFirst()
        val path = cursor?.getString(0)
        cursor?.close()
        return path
    }
}