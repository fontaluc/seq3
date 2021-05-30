package com.example.seq1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class ChoixListActivity: AppCompatActivity(), View.OnClickListener, OnListClickListener {

    private lateinit var listButton: Button
    private lateinit var newList: EditText
    private lateinit var pseudo: String
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var chaineJson: String
    private lateinit var gson: Gson
    private lateinit var profil: ProfilListeToDo
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)

        listButton = findViewById(R.id.list_button)
        listButton.setOnClickListener(this)
        newList = findViewById(R.id.new_list)

        val bdl = this.intent.extras
        pseudo = bdl!!.getString("pseudo")!!

        sp = PreferenceManager.getDefaultSharedPreferences(this)
        editor = sp.edit()
        chaineJson = sp.getString(pseudo, "{\"login\": $pseudo, \"mesListeToDo\": []}").toString()
        //Log.i("TP1", chaineJson)
        gson = Gson()
        profil = gson.fromJson(chaineJson, ProfilListeToDo::class.java)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = ListAdapter(profil.mesListeToDo, this)

    }

    override fun onClick(v: View){
        when (v.id) {
            R.id.list_button -> {
                profil.mesListeToDo.add(ListeToDo(newList.text.toString(), mutableListOf()))
                chaineJson = gson.toJson(profil)
                editor.putString(pseudo, chaineJson)
                editor.commit()
                recyclerView.adapter = ListAdapter(profil.mesListeToDo, this)
            }
        }
    }

    private fun alerter(s: String) {
        val t = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        Log.i("TP1", s)
        t.show()
    }

    override fun onListClicked(list: ListeToDo) {
        alerter(list.titreListeToDo)
        // Fabrication d'un Bundle de données
        val bdl = Bundle()
        bdl.putString("pseudo", pseudo)
        bdl.putString("titre", list.titreListeToDo)
        // Changer d'activité
        // Intent explicite
        val versShowListAct = Intent(this@ChoixListActivity, ShowListActivity::class.java)
        // Ajout d'un bundle à l'intent
        versShowListAct.putExtras(bdl)
        startActivity(versShowListAct)
    }
}