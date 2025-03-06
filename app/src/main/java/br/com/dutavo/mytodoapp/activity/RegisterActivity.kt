package br.com.dutavo.mytodoapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.dutavo.mytodoapp.MainActivity
import br.com.dutavo.mytodoapp.databinding.ActivityRegisterBinding
import br.com.dutavo.mytodoapp.ui.todo.ToDoActivity
import br.com.dutavo.mytodoapp.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity(){

    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel : RegisterViewModel by viewModels()

    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.buttonRegister.setOnClickListener {
            val nome = binding.editTextNome.text.toString()
            val email = binding.editTextEmail.text.toString()
            val senha = binding.editTextSenha.text.toString()

            if(nome.isEmpty() || email.isEmpty() || senha.isEmpty()){
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                registerViewModel.register(nome, email, senha)
            }
        }

        binding.imageViewFechar.setOnClickListener {
            finish()
        }

        registerViewModel.registerStatus.observe(this, Observer { status ->
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        })

        registerViewModel.navigateToHome.observe(this, Observer { navigate ->
            if(navigate) {
                startActivity(Intent(this, ToDoActivity::class.java))
                registerViewModel.OnNavigatedToHomeComplete()
            }
        })
    }
}