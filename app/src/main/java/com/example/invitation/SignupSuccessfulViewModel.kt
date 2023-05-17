package com.example.invitation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.invitation.data.UserRepository
import com.example.invitation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignupSuccessfulViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel(){

    val isSignUpSuccessful: LiveData<Boolean>
        get() = _isSignUpSuccessful
    private val _isSignUpSuccessful: SingleLiveEvent<Boolean> = SingleLiveEvent()

    init {
        viewModelScope.launch {
            _isSignUpSuccessful.postValue(userRepository.isUserRegistered())
        }
    }

    fun onCancelClick() {
        viewModelScope.launch{
            userRepository.removeUserRegistered()
        }
    }
}