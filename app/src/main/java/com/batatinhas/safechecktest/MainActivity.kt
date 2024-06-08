package com.batatinhas.safechecktest

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.batatinhas.safechecktest.adapter.FuncionarioAdapter
import com.batatinhas.safechecktest.databinding.ActivityMainBinding
import com.batatinhas.safechecktest.model.UserFuncionario
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val firebaseAuth by lazy{
        FirebaseAuth.getInstance()
    }

    private val firebaseFirestore by lazy{
        FirebaseFirestore.getInstance()
    }

    private lateinit var  listFuncionarios: RecyclerView
    private lateinit var funcionarioAdapter: FuncionarioAdapter
    private lateinit var filterContainer: ConstraintLayout

    private lateinit var  valueFilterNome: TextInputEditText
    private lateinit var valueFilterSetor: TextInputEditText
    private lateinit var valueFilterData: TextInputEditText
    private lateinit var btnPesquisaFilter: Button
    private lateinit var btnLimparFilter: Button

    private val listaFuncionarios = mutableListOf<UserFuncionario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        funcionarioAdapter = FuncionarioAdapter{ userFuncionario ->
            val intent = Intent(this, LayerFuncionarioActivity::class.java)
           intent.putExtra("funcionario", userFuncionario)

            startActivity(intent)
        }

        listFuncionarios = binding.listFuncionario
        listFuncionarios.adapter = funcionarioAdapter
        listFuncionarios.layoutManager = LinearLayoutManager(this)
        listFuncionarios.addItemDecoration(
            DividerItemDecoration(this, RecyclerView.VERTICAL)
        )
        inicializarToolbar()
        adcionarListaFuncionarios()

        /*** FILTER ***/
        filterContainer = binding.filterLayout
        filterContainer.visibility = View.GONE

        valueFilterNome = binding.valueFilterNome
        valueFilterData = binding.valueFilterData
        valueFilterSetor = binding.valueFilterSetor

        btnPesquisaFilter = binding.btnPesquisaFilter
        btnPesquisaFilter.setOnClickListener{
            val nome = valueFilterNome.text.toString()
            val data = valueFilterData.text.toString()
            val setor = valueFilterSetor.text.toString()

            filtra(nome, data, setor, listaFuncionarios)
        }

        btnLimparFilter = binding.btnLimparFiltro
        btnLimparFilter.setOnClickListener{
            limparFiltro(listaFuncionarios)
        }
    }

    private fun limparFiltro(listaFuncionarios: MutableList<UserFuncionario>) {
        for (i in 0 .. listaFuncionarios.size) {
            if(listFuncionarios.get(i).visibility != View.VISIBLE){
                listFuncionarios.get(i).visibility = View.VISIBLE
            }
        }
    }

    private fun filtra(nome: String, data: String, setor: String, listaFuncionarios: MutableList<UserFuncionario>) {
        for (i in 0 .. (listaFuncionarios.size - 1))
        {
            var funcionario:UserFuncionario = listaFuncionarios.get(i)

            if(nome != null && !nome.equals("") && !funcionario.nome.equals(nome)){
                listFuncionarios.get(i).visibility = View.GONE
            }
            if(setor != null && !setor.equals("") && !funcionario.setor.equals(setor)){
                listFuncionarios.get(i).visibility = View.GONE
            }
            //if(!funcionario.data.equals(data)){listFuncionarios.get(i).visibility = View.GONE}
        }
    }

    private fun inicializarToolbar() {
        val toolbar = binding.includeMainToolbar.tbMain
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Safe Check"
        }

        addMenuProvider(
            object : MenuProvider{
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_main, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                   when(menuItem.itemId){
                       R.id.item_filtro -> {
                            if(filterContainer.visibility == View.GONE)  {filterContainer.visibility = View.VISIBLE}
                            else                                         {filterContainer.visibility = View.GONE}
                       }

                       R.id.item_cadastro_funcinario -> {
                            startActivity(
                                Intent(applicationContext, CadastroFuncinarioActivity::class.java)
                            )
                       }

                       R.id.item_sair -> {
                            deslogarApp()
                       }
                   }
                    return true
                }
            }
        )
    }

    private fun deslogarApp() {
        AlertDialog.Builder(this)
            .setTitle("Deslogar")
            .setMessage("Deseja realmente sair ?")
            .setNegativeButton("Cancelar"){dialog, posicao ->}
            .setPositiveButton("Confirmar"){dialog, posicao ->
                firebaseAuth.signOut()
                startActivity(
                    Intent(applicationContext, LoginActivity::class.java)
                )
            }
            .create()
            .show()
    }

    private fun adcionarListaFuncionarios() {
        firebaseFirestore
            .collection("Funcionarios")
            .addSnapshotListener{querySnapshot, erro ->
                val id = querySnapshot?.documents

                id?.forEach {documentSnapshot ->
                    val userFuncionario = documentSnapshot.toObject(UserFuncionario::class.java)
                    val idUserEmpresa = firebaseAuth.currentUser?.uid

                    if(userFuncionario != null && idUserEmpresa != null)  listaFuncionarios.add(userFuncionario)
                }
                if(listaFuncionarios.isNotEmpty()) funcionarioAdapter.adicionarLista(listaFuncionarios)
            }
    }
}
