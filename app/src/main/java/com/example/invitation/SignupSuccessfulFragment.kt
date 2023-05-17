package com.example.invitation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.invitation.databinding.FragmentRegisterBinding
import com.example.invitation.databinding.FragmentSignupSuccessfulBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupSuccessfulFragment : Fragment() {

    private val viewModel: SignupSuccessfulViewModel by viewModels()

    private var _binding: FragmentSignupSuccessfulBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupSuccessfulBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Warning")
                .setMessage("Are you sure you want to remove the Invitation?")
                .setPositiveButton(
                    "Yes"
                ) { _, _ ->
                    viewModel.onCancelClick()
                    val action =
                        SignupSuccessfulFragmentDirections.toRegisterFragment()
                    findNavController().navigate(action)
                }
                .setNegativeButton("No") { _, _ -> }
                .show()
        }
    }

}