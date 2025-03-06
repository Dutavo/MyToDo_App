package br.com.dutavo.mytodoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel(){

    private val _loginStatus = MutableLiveData<String>()
    val loginStatus : LiveData<String> get() = _loginStatus

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome : LiveData<Boolean> get() = _navigateToHome

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(email: String, senha: String) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { login ->
                if(login.isSuccessful){
                    _loginStatus.value = "Login Bem-Sucedido!"
                    _navigateToHome.value = true
                } else {
                    _loginStatus.value = "Falha no Login: ${login.exception?.message}"
                }
            }
    }

    fun OnNavigatedToHomeComplete() {
        _navigateToHome.value = false
    }
}