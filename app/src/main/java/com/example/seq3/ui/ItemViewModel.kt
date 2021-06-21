package com.example.seq3.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.seq3.data.ListRepository
import com.example.seq3.data.model.Item
import kotlinx.coroutines.launch

class ItemViewModel(application: Application) : AndroidViewModel(application) {

    private val listRepository by lazy { ListRepository.newInstance(application) }

    val items = MutableLiveData<ViewState>()

    fun loadItem(hash: String, lid: Int) {

        viewModelScope.launch {
            items.value = ViewState.Loading
            try {
                items.value = ViewState.Content(items = listRepository.getItem(hash, lid))

            } catch (e: Exception) {
                items.value = ViewState.Error(e.message.orEmpty())
            }

        }
    }

    sealed class ViewState {
        object Loading : ViewState()
        data class Content(val items: List<Item>) : ViewState()
        data class Error(val message: String) : ViewState()
    }
}