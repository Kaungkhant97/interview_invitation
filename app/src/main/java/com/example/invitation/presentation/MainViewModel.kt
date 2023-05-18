package com.example.invitation.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.invitation.data.UserRepository
import com.example.invitation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel(){

    val isSignUpSuccessful: LiveData<Boolean>
        get() = _isSignUpSuccessful
    private val _isSignUpSuccessful: SingleLiveEvent<Boolean> = SingleLiveEvent()

    init {
        viewModelScope.launch {
            _isSignUpSuccessful.postValue(userRepository.isUserRegistered())
        }
    }

}