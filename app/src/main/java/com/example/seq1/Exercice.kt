package com.example.seq1

import android.util.Log
import com.google.gson.Gson

const val CAT = "TP1"

fun main(){
    val titre = "list 1"
    var chaine_json = """{"titreListeToDo": $titre, "lesItems": []}"""
    val gson = Gson()
    val liste = gson.fromJson(chaine_json, ListeToDo::class.java)
    println(liste.toString())
    liste.lesItems.add(ItemToDo("Item 1"))
    chaine_json = gson.toJson(liste)
    println(chaine_json)
}