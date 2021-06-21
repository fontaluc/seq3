package com.example.seq3.ui

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.seq3.R
import com.example.seq3.data.ListRepository
import com.example.seq3.data.source.remote.RemoteDataSource
import com.example.seq3.data.source.remote.SessionManager
import com.example.seq3.data.source.remote.api.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var edtPseudo: EditText
    private lateinit var edtMdp: EditText
    private lateinit var mainButton: Button
    private lateinit var sessionManager: SessionManager
    private val remoteDataSource = RemoteDataSource()

    private val activityScope = CoroutineScope(
        SupervisorJob() +
                Dispatchers.Main
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtPseudo = findViewById(R.id.editText)
        edtMdp = findViewById(R.id.mdp_edt)
        mainButton = findViewById(R.id.main_button)
        mainButton.setOnClickListener(this)

        sessionManager = SessionManager(application)
    }

    override fun onResume() {
        super.onResume()

        mainButton.isEnabled = verifReseau()
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
                activityScope.launch {
                    try {
                        val loginResponse = remoteDataSource.login(edtPseudo.text.toString(), edtMdp.text.toString())
                        sessionManager.saveAuthToken(loginResponse.hash)
                        alerter(loginResponse.hash)
                    } catch (e: Exception) {
                        alerter(e.message.toString())
                    }
                }

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
        Log.i(CAT, s)
    }

    companion object {
        private const val CAT = "TP3"
    }
}