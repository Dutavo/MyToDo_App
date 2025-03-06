package br.com.dutavo.mytodoapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.dutavo.mytodoapp.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity(){
    
    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Inicializa o view Binding
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Ir para o Registro
        binding.buttonRegister.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        //Ir para o Login
        binding.buttonLogin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))

        }
    }
}