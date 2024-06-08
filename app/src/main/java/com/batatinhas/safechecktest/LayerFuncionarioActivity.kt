package com.batatinhas.safechecktest

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.batatinhas.safechecktest.databinding.ActivityLayerFuncionarioBinding
import com.batatinhas.safechecktest.model.UserFuncionario

class LayerFuncionarioActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLayerFuncionarioBinding.inflate(layoutInflater)
    }

    private lateinit var nomeText: TextView
    private lateinit var setorText: TextView
    private lateinit var statusText: TextView
    private lateinit var telefoneText: TextView
    private lateinit var emailText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bundle = intent.extras //Todos os parametros passados

        val funcionario = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            bundle?.getParcelable("funcionario", UserFuncionario::class.java)
        }else{
            bundle?.getParcelable("obj")
        }

        nomeText = binding.textValueNome
        nomeText.text = funcionario?.nome

        setorText = binding.textValueSetor
        setorText.text = funcionario?.setor

        statusText = binding.textValueStatus
        statusText.text = funcionario?.statusFuncionario.toString()

        telefoneText = binding.textValueTelefone

        emailText = binding.textValueEmail
        emailText.text = funcionario?.email
        }
}