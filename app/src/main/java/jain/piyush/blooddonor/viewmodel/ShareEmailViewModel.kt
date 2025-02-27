package jain.piyush.blooddonor.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ShareEmailViewModel: ViewModel(){
    var email by mutableStateOf("")
        private set
    fun updateEmail(newEmail:String){
        email = newEmail
    }
}