package br.com.dutavo.mytodoapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.dutavo.mytodoapp.databinding.ActivityLoginBinding
import br.com.dutavo.mytodoapp.ui.todo.ToDoActivity
import br.com.dutavo.mytodoapp.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity(){

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Realizar o login
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val senha = binding.editTextSenha.text.toString()

            if(email.isEmpty() || senha.isEmpty()){
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                //Realiza o login
                loginViewModel.login(email, senha)
            }
        }

        //Imagem X de fechar
        binding.imageViewFechar.setOnClickListener {
            finish()
        }

        //Observar o status do login
        loginViewModel.loginStatus.observe(this, Observer { status ->
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        })

        //Observar se o login foi bem-sucedido
        loginViewModel.navigateToHome.observe(this, Observer { navigate ->
            //Ir para a tela principal
            if (navigate) {
                startActivity(Intent(this, ToDoActivity::class.java))
                loginViewModel.OnNavigatedToHomeComplete()
            }
        })
    }
}