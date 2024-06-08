package com.batatinhas.safechecktest.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.batatinhas.safechecktest.enums.StatusFuncionario

@Parcelize
data class UserFuncionario(
    var id: String = "",
    var nome: String = "",
    var documento: String = "",
    var email: String = "",
    var numero: String = "",
    var setor: String = "",
    var statusFuncionario: StatusFuncionario = StatusFuncionario.NULO,
    var fkEmpresa: String = ""
) : Parcelable
