package com.example.seq3.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.seq3.R
import com.example.seq3.data.model.Liste
import com.example.seq3.data.source.remote.SessionManager
import com.example.seq3.ui.adapter.ListAdapter
import kotlinx.coroutines.*

class ChoixListActivity: AppCompatActivity() {

    private lateinit var pseudo: String
    private lateinit var listAdapter: ListAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var hash: String
    private val listViewModel by viewModels<ListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)

        val bdl = this.intent.extras
        pseudo = bdl!!.getString("pseudo")!!

        sessionManager = SessionManager(application)
        hash = sessionManager.fetchAuthToken().toString()

        setupRecyclerView()
        loadAndDisplayLists(hash, pseudo)

    }

    private fun loadAndDisplayLists(hash: String, pseudo: String) {

        listViewModel.loadList(hash, pseudo)
        listViewModel.lists.observe(this) { viewState ->
            when (viewState) {
                is ListViewModel.ViewState.Content -> {
                    listAdapter.show(viewState.lists)
                    showProgress(false)
                }
                ListViewModel.ViewState.Loading -> showProgress(true)
                is ListViewModel.ViewState.Error -> {
                    showProgress(false)
                    alerter( "${viewState.message} ")
                }
            }

        }
    }

    private fun showProgress(show: Boolean) {
        val progress = findViewById<View>(R.id.progress)
        val list = findViewById<View>(R.id.recycler_view)
        progress.isVisible = show
        list.isVisible = !show
    }

    private fun setupRecyclerView() {
        val recyclerview = findViewById<RecyclerView>(R.id.recycler_view)
        listAdapter = ListAdapter(listClickListener = object : ListAdapter.OnListClickListener {
            override fun onListClicked(list: Liste) {
                alerter(list.label)
                // Fabrication d'un Bundle de données
                val bdl = Bundle()
                bdl.putString("id", list.id.toString())
                // Changer d'activité
                // Intent explicite
                val versShowListAct = Intent(this@ChoixListActivity, ShowListActivity::class.java)
                // Ajout d'un bundle à l'intent
                versShowListAct.putExtras(bdl)
                startActivity(versShowListAct)
            }
        })

        recyclerview.adapter = listAdapter
    }

    private fun alerter(s: String) {
        val t = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        Log.i(CAT, s)
        t.show()
    }

    companion object {
        private const val CAT = "TP3"
    }
}

