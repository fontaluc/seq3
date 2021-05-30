package com.example.seq1

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class ShowListActivity: AppCompatActivity(), View.OnClickListener, OnItemClickListener {

    private lateinit var itemButton: Button
    private lateinit var newItem: EditText
    private lateinit var pseudo: String
    private lateinit var titre: String
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var chaineJson: String
    private lateinit var gson: Gson
    private lateinit var liste: ListeToDo
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_item)

        itemButton = findViewById(R.id.item_button)
        itemButton.setOnClickListener(this)
        newItem = findViewById(R.id.new_item)

        val bdl = this.intent.extras
        pseudo = bdl!!.getString("pseudo")!!
        titre = bdl.getString("titre")!!.replace(" ", "_")

        sp = PreferenceManager.getDefaultSharedPreferences(this)
        editor = sp.edit()
        chaineJson = sp.getString(Pair(pseudo, titre).toString(),
            "{\"titreListeToDo\": $titre\", \"lesItems\": []}"
        )
            .toString()
        gson = Gson()
        liste = gson.fromJson(chaineJson, ListeToDo::class.java)

        recyclerView = findViewById(R.id.item_recycler_view)
        recyclerView.adapter = ItemAdapter(liste.lesItems, this)

    }

    override fun onClick(v: View){
        when (v.id) {
            R.id.item_button -> {
                liste.lesItems.add(ItemToDo(newItem.text.toString()))
                chaineJson = gson.toJson(liste)
                editor.putString(Pair(pseudo, titre).toString(), chaineJson)
                editor.commit()
                recyclerView.adapter = ItemAdapter(liste.lesItems, this)
            }
        }
    }

    override fun onItemClicked(v: View, pos: Int) {
        val checkBox = v as CheckBox
        val item = liste.lesItems[pos]
        item.fait = checkBox.isChecked
        chaineJson = gson.toJson(liste)
        editor.putString(Pair(pseudo, titre).toString(), chaineJson)
        editor.commit()

    }
}