package com.batatinhas.safechecktest

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.batatinhas.safechecktest.databinding.ActivityCadastroBinding
import com.batatinhas.safechecktest.model.UsuarioEmpresa
import com.batatinhas.safechecktest.util.exibirMenssagem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class CadastroActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCadastroBinding.inflate(layoutInflater)
    }
    private val firebaseAuth by lazy{
        FirebaseAuth.getInstance()
    }

    private val firebaseFirestore by lazy{
        FirebaseFirestore.getInstance()
    }

    private lateinit var nome : String
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

        inicializarToolbar()
        binding.btnCadastrar.setOnClickListener{
            nome = binding.inputCadastroNome.text.toString()
            email = binding.inputCadastroEmail.text.toString()
            senha = binding.inputCadastroSenha.text.toString()

            inicializarEventoClick()
        }
    }

    private fun inicializarEventoClick() {
        if(validarCampos(nome, email, senha)){
            cadastrarUsuario(nome, email, senha)
        }
    }

    private fun cadastrarUsuario(nome: String, email: String, senha: String) {
        firebaseAuth.createUserWithEmailAndPassword(
            email, senha
        ).addOnCompleteListener{ resultado ->
            if (resultado.isSuccessful){
                val idUserEmpresa = resultado.result.user?.uid
                if (idUserEmpresa != null){
                    val userEmpresa = UsuarioEmpresa(
                        idUserEmpresa, nome, email, senha
                    )
                    salvarUserEmpresa(userEmpresa)
                }
            }
        }.addOnFailureListener{ erro ->
            try {
                throw erro
            }catch (erroSenhaInadequada: FirebaseAuthWeakPasswordException){
                erroSenhaInadequada.printStackTrace()
                exibirMenssagem("SENHA fraca, a incremente")
            }catch (erroUsuarioExistente: FirebaseAuthUserCollisionException){
                erroUsuarioExistente.printStackTrace()
                exibirMenssagem("E-MAIL já existente em nosso sistema")
            }catch (erroCredenciaisInvalidas: FirebaseAuthInvalidCredentialsException){
                erroCredenciaisInvalidas.printStackTrace()
                exibirMenssagem("E-MAIL invalido, digite outro e-mail")
            }

        }
    }

    private fun salvarUserEmpresa(userEmpresa: UsuarioEmpresa) {

        firebaseFirestore
            .collection("usuarioEmpresa")
            .document(userEmpresa.id)
            .set(userEmpresa)
            .addOnSuccessListener {
                exibirMenssagem("Sucesso ao fazer seu cadastro")
                startActivity(
                    Intent(applicationContext, MainActivity::class.java)
                )
            }.addOnFailureListener {  exibirMenssagem("Cadastro não pode ser efetuado")  }
    }

    private fun validarCampos(nome:String, email:String, senha:String): Boolean {

        if (nome.isNotBlank()){
            binding.ilCadastroNome.error = null

            if (email.isNotBlank()){
                binding.ilCadastroEmail.error = null

                if (senha.isNotBlank()){
                    binding.ilCadastroSenha.error = null
                    return true
                }else{
                    binding.ilCadastroSenha.error = "Campo de SENHA tem que ser preenchido"
                    return false
                }

            }else{
                binding.ilCadastroEmail.error = "Campo de EMAIL tem que ser preenchido"
                return false
            }

        }else{
            binding.ilCadastroNome.error = "Campo de NOME tem que ser preenchido"
            return false
        }
    }

    private fun inicializarToolbar() {
        val toolbar = binding.includeToolbar.tbMain
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Faça seu cadastro"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}