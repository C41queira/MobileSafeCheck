package com.batatinhas.safechecktest

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.batatinhas.safechecktest.databinding.ActivityCadastroFuncinarioBinding
import com.batatinhas.safechecktest.enums.StatusFuncionario
import com.batatinhas.safechecktest.model.UserFuncionario
import com.batatinhas.safechecktest.util.exibirMenssagem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class CadastroFuncinarioActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCadastroFuncinarioBinding.inflate(layoutInflater) }
    private val firebaseFirestore by lazy{ FirebaseFirestore.getInstance() }
    private val firebaseAuth by lazy{ FirebaseAuth.getInstance() }

    private lateinit var nome : String
    private lateinit var documento: String
    private lateinit var email: String
    private lateinit var telefone: String
    private lateinit var setor: String

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

        binding.btnCadastroFuncionario.setOnClickListener{
            nome = binding.editNF.text.toString()
            documento = binding.editDF.text.toString()
            email = binding.editEF.text.toString()
            telefone = binding.editTF.text.toString()
            setor = binding.editSF.text.toString()

            inicializarEventoClick()
        }
    }

    private fun inicializarEventoClick() {
        if (validarCampos(nome, documento, email, telefone, setor)) cadastroFuncionario()
    }

    private fun cadastroFuncionario() {

        val usuarioEmpresa = firebaseAuth.currentUser

        val userFuncionario = UserFuncionario (
            Random.nextInt(1, 1000).toString(), nome, documento, email, telefone, setor, StatusFuncionario.NULO, usuarioEmpresa?.uid.toString()
        )

        firebaseFirestore
            .collection("Funcionarios")
            .document(userFuncionario.id)
            .set(userFuncionario)
            .addOnSuccessListener {
                exibirMenssagem("Sucesso ao fazer seu cadastro")
                startActivity(
                    Intent(applicationContext, MainActivity::class.java)
                )
            }.addOnFailureListener {  exibirMenssagem("Cadastro não pode ser efetuado")  }
    }

    private fun validarCampos(nome: String, documento: String, email: String, telefone: String, setor: String): Boolean {

        if (nome.isNotBlank()){
            binding.textInputNF.error = null
            if (documento.isNotBlank()){
                binding.textInputDF.error = null
                if (email.isNotBlank()){
                    binding.textInputEF.error = null
                    if (telefone.isNotBlank()) {
                        binding.textInputTF.error = null
                        if (setor.isNotBlank()) {
                            binding.textInputSF.error = null
                            return true
                        } else {
                            binding.textInputSF.error = "Campo de SETOR tem que ser preenchido"
                            return false
                        }
                    } else {
                        binding.textInputTF.error = "Campo de TELEFONE tem que ser preenchido"
                        return false
                    }
                } else {
                    binding.textInputEF.error = "Campo de EMAIL tem que ser preenchido"
                    return false
                }
            } else {
                binding.textInputDF.error = "Campo de DOCUMENTO tem que ser preenchido"
                return false
            }
        } else {
            binding.textInputNF.error = "Campo de NOME tem que ser preenchido"
            return false
        }
    }

    private fun inicializarToolbar() {
        val toolbar = binding.includeToolbarCadastroFuncionario.tbMain
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Faça o cadastro do funcioario"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}