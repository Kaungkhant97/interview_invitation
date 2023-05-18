package com.example.invitation.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.invitation.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()

    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            textInputEdName.doOnTextChanged { text, _, _, _ ->
                viewModel.onNameChange(text)
            }

            textInputEdEmail.doOnTextChanged { text, _, _, _ ->
                viewModel.onEmailChange(text)
            }

            textInputEdconfirmEmail.doOnTextChanged { text, _, _, _ ->
                viewModel.onConfirmEmailChange(text)
            }
            btnSubmit.setOnClickListener {
                viewModel.signUp(textInputEdName.text.toString(), textInputEdEmail.text.toString())
            }
        }

        observeViewState()
    }

    private fun observeViewState() {
        with(viewModel) {
            emailState.observe(viewLifecycleOwner) {

                if (!it.isSuccess) {
                    binding.textInputEdEmail.error = it.message
                } else {
                    binding.textInputEdEmail.error = null
                }
            }
            nameState.observe(viewLifecycleOwner) {
                if (!it.isSuccess) {
                    binding.textInputEdName.error = it.message
                } else {
                    binding.textInputEdName.error = null
                }
            }
            confirmEmailState.observe(viewLifecycleOwner) {
                if (!it.isSuccess) {
                    binding.textInputEdconfirmEmail.error = it.message
                } else {
                    binding.textInputEdconfirmEmail.error = null
                }
            }

            submitButtonState.observe(viewLifecycleOwner) {
                binding.btnSubmit.isEnabled = it
            }

            signUpResult.observe(viewLifecycleOwner) {
                if (it.isSuccess) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Congratulation")
                        .setMessage("You Have Successfully Registered")
                        .setPositiveButton(
                            "Ok"
                        ) { _, _ -> }
                        .setOnDismissListener {
                            val action =
                                RegisterFragmentDirections.toSignupSuccessfulFragment()
                            findNavController().navigate(action)
                        }.show()
                } else {
                    val msg = it.exceptionOrNull()?.message
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                }
            }

            loading.observe(viewLifecycleOwner) {
                binding.loading.isVisible = it
            }
        }
    }

}