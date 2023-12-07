package com.bav.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bav.core.databinding.EmptyScreenBinding

open class EmptyScreen : Fragment() {

    private var _binding: EmptyScreenBinding? = null
    protected val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = EmptyScreenBinding.inflate(inflater, container, false)
        return binding.root
    }
}