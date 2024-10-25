package org.eurekamps.dam2_2425_actividad1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    private val _profileImageUrl = MutableLiveData<String>()
    val profileImageUrl: LiveData<String> get() = _profileImageUrl

    fun setProfileImageUrl(url: String) {
        _profileImageUrl.value = url
    }
}
