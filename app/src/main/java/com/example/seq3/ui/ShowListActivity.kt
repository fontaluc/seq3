package com.example.seq3.ui

import android.net.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.seq3.R
import com.example.seq3.data.ListRepository
import com.example.seq3.data.model.Item
import com.example.seq3.data.source.local.LocalDataSource
import com.example.seq3.data.source.remote.RemoteDataSource
import com.example.seq3.data.source.remote.SessionManager
import com.example.seq3.ui.adapter.ItemAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


class ShowListActivity: AppCompatActivity(), View.OnClickListener {

    private lateinit var itemAdapter: ItemAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var hash: String
    private lateinit var itemButton: Button
    private lateinit var newItem: EditText
    private var id by Delegates.notNull<Int>()
    private val itemViewModel by viewModels<ItemViewModel>()
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSource: LocalDataSource
    private lateinit var updateItems: List<Item>

    private val activityScope = CoroutineScope(
        SupervisorJob() +
                Dispatchers.Main
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_item)

        itemButton = findViewById(R.id.item_button)
        itemButton.setOnClickListener(this)
        newItem = findViewById(R.id.new_item)

        val bdl = this.intent.extras
        id = bdl!!.getString("id")!!.toInt()

        sessionManager = SessionManager(application)
        hash = sessionManager.fetchAuthToken().toString()

        remoteDataSource = RemoteDataSource()
        localDataSource = LocalDataSource(application)

        setupRecyclerView()
        loadAndDisplayItems(hash, id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listenForInternetConnectivity()
        }

    }

    override fun onResume() {
        super.onResume()

        itemButton.isEnabled = verifReseau()
    }

    private fun verifReseau(): Boolean {

        val cnMngr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cnMngr.activeNetworkInfo

        var sType = "Aucun réseau détecté"
        var bStatut = false
        if (netInfo != null) {
            val netState = netInfo.state
            if (netState.compareTo(NetworkInfo.State.CONNECTED) == 0) {
                bStatut = true
                when (netInfo.type) {
                    ConnectivityManager.TYPE_MOBILE -> sType = "Réseau mobile détecté"
                    ConnectivityManager.TYPE_WIFI -> sType = "Réseau wifi détecté"
                }
            }
        }

        Log.i(CAT, sType)
        return bStatut
    }

    private fun loadAndDisplayItems(hash: String, lid: Int) {

        itemViewModel.loadItem(hash, lid)
        itemViewModel.items.observe(this) { viewState ->
            when (viewState) {
                is ItemViewModel.ViewState.Content -> {
                    itemAdapter.show(viewState.items)
                    showProgress(false)
                }
                ItemViewModel.ViewState.Loading -> showProgress(true)
                is ItemViewModel.ViewState.Error -> {
                    showProgress(false)
                    alerter("${viewState.message} ")
                }
            }

        }
    }


    private fun alerter(s: String) {
        val t = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        Log.i(CAT, s)
        t.show()
    }

    companion object {
        private const val CAT = "TP3"
    }

    private fun showProgress(show: Boolean) {
        val progress = findViewById<View>(R.id.item_progress)
        val list = findViewById<View>(R.id.item_recycler_view)
        progress.isVisible = show
        list.isVisible = !show
        itemButton.isVisible = !show
        newItem.isVisible = !show
    }

    private fun setupRecyclerView() {
        val recyclerview = findViewById<RecyclerView>(R.id.item_recycler_view)
        itemAdapter = ItemAdapter(itemClickListener = object : ItemAdapter.OnItemClickListener {
            override fun onItemClicked(v: View, pos: Int) {
                val items = itemAdapter.items
                val checkBox = v as CheckBox
                val item = items[pos]
                item.checked = when(checkBox.isChecked) {
                    true -> 1
                    else -> 0
                }

                activityScope.launch {
                    try {
                        remoteDataSource.updateItem(hash, id, item.id, item.checked)
                    } catch (e: Exception) {
                        item.sync = (item.sync + 1) % 2
                        localDataSource.updateItem(item)
                    }
                }
            }
        })

        recyclerview.adapter = itemAdapter
    }

    override fun onClick(v: View){
        when (v.id) {
            R.id.item_button -> {
                activityScope.launch {
                    try {
                        remoteDataSource.addItem(hash, id, newItem.text.toString())
                        loadAndDisplayItems(hash, id)
                        setupRecyclerView()
                    } catch (e: java.lang.Exception) {
                        alerter(e.message.toString())
                    }
                }

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun listenForInternetConnectivity() {
        val networkCallback: ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                alerter("Internet Available")

                activityScope.launch {

                    updateItems = localDataSource.getUpdate()
                    alerter("to update : ${updateItems.size}")

                    if (updateItems.isNotEmpty()) {
                        for (updateItem in updateItems) {

                            try {
                                remoteDataSource.updateItem(
                                    hash,
                                    updateItem.idList,
                                    updateItem.id,
                                    updateItem.checked
                                )
                            } catch (e: java.lang.Exception) {
                                alerter(e.message.toString())
                            }

                            updateItem.sync = 0
                            localDataSource.updateItem(updateItem)
                        }
                    }
                }

                itemButton.isEnabled = verifReseau()
            }

            override fun onLost(network: Network) {
                alerter("Internet Lost")
            }
        }
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
            connectivityManager.registerNetworkCallback(request, networkCallback)
        }
    }
}

