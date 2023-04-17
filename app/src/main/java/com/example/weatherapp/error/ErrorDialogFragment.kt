package com.example.weatherapp.error

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.weatherapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ErrorDialogFragment : DialogFragment() {

    private val errorDialogData: ErrorDialogData by lazy {
        when {
            SDK_INT >= 33 -> requireArguments().getParcelable(ARGS, ErrorDialogData::class.java)!!
            else -> @Suppress("DEPRECATION") {
                requireArguments().getParcelable(ARGS)!!
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setStyle(STYLE_NO_FRAME, R.style.Theme_WeatherApp)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                ErrorDialogScreen(message = errorDialogData.message, action = ::handleAction)
            }
        }
    }


    private fun handleAction() {
        errorDialogData.action?.let { it() } ?: dismiss()
    }

    companion object {
        private const val ARGS = "args"

        fun newInstance(errorDialogData: ErrorDialogData) = ErrorDialogFragment().apply {
            arguments = bundleOf(ARGS to errorDialogData)
            isCancelable = false
        }
    }
}