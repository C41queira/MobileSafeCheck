package com.batatinhas.safechecktest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.batatinhas.safechecktest.databinding.ItemFuncionarioBinding
import com.batatinhas.safechecktest.model.UserFuncionario

class FuncionarioAdapter(
    private val click: (UserFuncionario) -> Unit
) : Adapter<FuncionarioAdapter.FuncionarioViewHolder>(){

    private var listaFuncionarios = mutableListOf<UserFuncionario>()

    fun adicionarLista(lista: List<UserFuncionario>){
        for(funcionario in lista){
            if (!listaFuncionarios.contains(funcionario))
            {
                listaFuncionarios.add(funcionario)
            }
        }
        notifyDataSetChanged()
    }


    inner class FuncionarioViewHolder(
        private val binding: ItemFuncionarioBinding
    ): ViewHolder(binding.root){

        val layoutViewFuncionario : ConstraintLayout = binding.layoutFuncionarioView

        fun bind(funcionario: UserFuncionario){
            binding.textFNome.text = funcionario.nome
            binding.textFSetor.text = funcionario.setor
            binding.textFStatus.text = funcionario.statusFuncionario.toString()

            layoutViewFuncionario.setOnClickListener(){
                click(funcionario)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuncionarioViewHolder {

        val inflate = LayoutInflater.from(parent.context)
        val itemView = ItemFuncionarioBinding.inflate(
            inflate, parent, false
        )
        return FuncionarioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FuncionarioViewHolder, position: Int) {
        val funcionario = listaFuncionarios[position]
        holder.bind(funcionario)
    }

    override fun getItemCount(): Int {
        return listaFuncionarios.size
    }
}