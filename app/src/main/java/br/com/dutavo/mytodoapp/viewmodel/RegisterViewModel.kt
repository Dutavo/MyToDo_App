package br.com.dutavo.mytodoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel : ViewModel(){

    private val _registerStatus = MutableLiveData<String>()
    val registerStatus : LiveData<String> get() = _registerStatus

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome : LiveData<Boolean> get() = _navigateToHome

    //Instância do Firebase Authentication
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    //Função para realizar o registro
    fun register(nome: String, email: String, senha: String) {
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { registro ->
            if (registro.isSuccessful) {
                _registerStatus.value = "Registro Bem-Sucessido!"
                _navigateToHome.value = true
            } else {
                _registerStatus.value = "Falha no Registro: ${registro.exception}"
            }
        }
    }

    //Navegação para a tela principal
    fun OnNavigatedToHomeComplete() {
        _navigateToHome.value = false
    }
}