package com.example.invitation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.invitation.data.UserRepository
import com.example.invitation.util.SingleLiveEvent
import com.example.invitation.util.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {

    val signUpResult: LiveData<Result<Unit>>
        get() = _signUpResult
    private val _signUpResult: SingleLiveEvent<Result<Unit>> = SingleLiveEvent()

    val loading: LiveData<Boolean>
        get() = _loading
    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)

    val emailState: LiveData<InputViewState>
        get() = _emailViewState.asLiveData()
    private var _emailViewState: MutableStateFlow<InputViewState> =
        MutableStateFlow(InputViewState())

    val nameState: LiveData<InputViewState>
        get() = _nameViewState.asLiveData()
    private var _nameViewState: MutableStateFlow<InputViewState> =
        MutableStateFlow(InputViewState())

    val confirmEmailState: LiveData<InputViewState>
        get() = _confirmEmailViewState.asLiveData()
    private var _confirmEmailViewState: MutableStateFlow<InputViewState> =
        MutableStateFlow(InputViewState())


    val submitButtonState: LiveData<Boolean>
        get() = combine(
            _nameViewState,
            _emailViewState,
            _confirmEmailViewState
        ) { t1, t2, t3 -> t1.isSuccess && t2.isSuccess && t3.isSuccess }.asLiveData()

    fun signUp(name: String, email: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _loading.postValue(true)
                val result = userRepository.signup(name, email)
                _signUpResult.postValue(result)
                _loading.postValue(false)
            }
        }
    }

    fun onNameChange(text: CharSequence?) {
        if (text == null) _nameViewState.value =
            InputViewState(false, "This field cannot be empty", text)

        val isthreeCharacterLong = text!!.length >= 3

        if (isthreeCharacterLong) {
            _nameViewState.value =
                InputViewState(true, currentValue = text.toString())
        } else {
            _nameViewState.value = InputViewState(
                false,
                "This field need to have atleast 3 character",
                text.toString()
            )
        }
    }

    fun onEmailChange(text: CharSequence?) {
        if (text == null) _emailViewState.value =
            InputViewState(false, "This field cannot be empty", text)
        val isValidEmail = text!!.isEmailValid()

        if (isValidEmail) {
            _emailViewState.value = InputViewState(true, currentValue = text.toString())
        } else {
            _emailViewState.value = InputViewState(false, "Not valid email", text.toString())
        }

        _confirmEmailViewState.value.currentValue?.let {
            if (!it.isNullOrEmpty()) {
                val isConfirmEmailSame = text.toString() == it
                if (!isConfirmEmailSame) {
                    _confirmEmailViewState.value =
                        InputViewState(false, "email need to be the same", it)
                } else {
                    _confirmEmailViewState.value = InputViewState(true, currentValue = it)
                }
            }
        }
    }

    fun onConfirmEmailChange(text: CharSequence?) {
        if (text == null) _confirmEmailViewState.value =
            InputViewState(false, "This field cannot be empty")
        val isValidEmail = text!!.isEmailValid()

        if (isValidEmail) {

            if (_emailViewState.value.currentValue == text.toString()) {
                _confirmEmailViewState.value = InputViewState(true, currentValue = text.toString())
            } else {
                _confirmEmailViewState.value =
                    InputViewState(false, "email need to be the same", text.toString())
            }
        } else {
            _confirmEmailViewState.value =
                InputViewState(false, "Not valid email", text.toString())
        }
    }

    data class InputViewState(
        val isSuccess: Boolean = false,
        val message: String? = null,
        val currentValue: String? = null
    )
}