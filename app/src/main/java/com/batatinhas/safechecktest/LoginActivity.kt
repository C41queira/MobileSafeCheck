package com.batatinhas.safechecktest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.batatinhas.safechecktest.databinding.ActivityLoginBinding
import com.batatinhas.safechecktest.util.exibirMenssagem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private val binding by lazy{
       ActivityLoginBinding.inflate(layoutInflater)
    }

    private val firebaseAuth by lazy{
        FirebaseAuth.getInstance()
    }

    private val firebaseFirestore by lazy{
        FirebaseFirestore.getInstance()
    }

    private lateinit var email: String
    private lateinit var senha: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inicializarClickCadastro()

    }

    override fun onStart() {
        super.onStart()
        verificarUsuarioLogado()
    }

    private fun verificarUsuarioLogado() {
        val usuarioAtual = firebaseAuth.currentUser
        if(usuarioAtual != null){
            startActivity(
                Intent(applicationContext, MainActivity::class.java)
            )
        }
    }

    private fun inicializarClickCadastro() {
        binding.textBtnCadastro.setOnClickListener{
            startActivity(
                Intent(this, CadastroActivity::class.java)
            )
        }

        binding.btnEntrarLogin.setOnClickListener{
            email = binding.textInputEmail.text.toString()
            senha = binding.textInputSenha.text.toString()

            if (validarCampos(email, senha)){
                logarUsuario()
            }
        }
    }

    private fun logarUsuario() {
        firebaseAuth.signInWithEmailAndPassword(
            email, senha
        ).addOnSuccessListener {
            exibirMenssagem("Sucesso ao fazer seu login")
            startActivity(
                Intent(applicationContext, MainActivity::class.java)
            )
        }.addOnFailureListener {erro ->
            try {
                throw erro
            }catch (erroUsuarioInvalido: FirebaseAuthInvalidUserException){
                erroUsuarioInvalido.printStackTrace()
                exibirMenssagem("Email não cadastrado")
            }catch (erroCredenciaisInvalidades: FirebaseAuthInvalidCredentialsException){
                erroCredenciaisInvalidades.printStackTrace()
                exibirMenssagem("E-MAIL e/ou SENHA estão invalidos")
            }
        }
    }

    private fun validarCampos(email: String, senha: String): Boolean {
        if(email.isNotBlank() && senha.isNotBlank() ){
            binding.inputLayoutEmail.error = null
            binding.InputLayoutSenha.error = null
           return true;
        }else if (email.isBlank()){
            binding.inputLayoutEmail.error = "Campo de email invalido"
            return false
        }else if(senha.isBlank()){
            binding.InputLayoutSenha.error = "Campo de senha invalido"
            return false
        }else{
            return false
        }
    }
}