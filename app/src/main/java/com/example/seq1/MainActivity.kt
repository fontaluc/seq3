package com.example.seq1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var edtPseudo: EditText
    private lateinit var mainButton: Button
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtPseudo = findViewById(R.id.editText)
        mainButton = findViewById(R.id.main_button)
        mainButton.setOnClickListener(this)
        sp = PreferenceManager.getDefaultSharedPreferences(this)
        editor = sp.edit()
    }

    override fun onStart() {
        super.onStart()

        val l: String? = sp.getString("pseudo", "pseudo inconnu")
        edtPseudo.setText(l)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val iGP = Intent(this, SettingsActivity::class.java)
        startActivity(iGP)
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.main_button -> {
                alerter(edtPseudo.text.toString())
                editor.putString("pseudo", edtPseudo.text.toString())
                editor.commit()

                // Fabrication d'un Bundle de données
                val bdl = Bundle()
                bdl.putString("pseudo", edtPseudo.text.toString())
                // Changer d'activité
                // Intent explicite
                val versChoixListAct = Intent(this@MainActivity, ChoixListActivity::class.java)
                // Ajout d'un bundle à l'intent
                versChoixListAct.putExtras(bdl)
                startActivity(versChoixListAct)
            }
        }
    }

    private fun alerter(s: String) {
        val t = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        t.show()
        Log.i("TP1", s)
    }
}