package com.example.seq3.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.seq3.data.ListRepository
import com.example.seq3.data.model.Liste
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : AndroidViewModel(application) {

    private val listRepository by lazy { ListRepository.newInstance(application) }

    val lists = MutableLiveData<ViewState>()

    fun loadList(hash: String, pseudo: String) {

        viewModelScope.launch {
            lists.value = ViewState.Loading
            try {
                val uid = listRepository.remoteDataSource.getUserId(hash, pseudo)
                lists.value = ViewState.Content(lists = listRepository.getList(hash, uid))

            } catch (e: Exception) {
                lists.value = ViewState.Error(e.message.orEmpty())
            }

        }
    }

    sealed class ViewState {
        object Loading : ViewState()
        data class Content(val lists: List<Liste>) : ViewState()
        data class Error(val message: String) : ViewState()
    }
}