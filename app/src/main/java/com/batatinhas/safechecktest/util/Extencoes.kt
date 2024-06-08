package com.batatinhas.safechecktest.util

import android.app.Activity
import android.widget.Toast

fun Activity.exibirMenssagem(msg:String){
    Toast.makeText(
        this,
        msg,
        Toast.LENGTH_LONG
    ).show()
}