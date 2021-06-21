package com.example.seq3.data

import android.app.Application
import android.util.Log
import com.example.seq3.data.model.Item
import com.example.seq3.data.model.Liste
import com.example.seq3.data.source.local.LocalDataSource
import com.example.seq3.data.source.remote.RemoteDataSource
import java.lang.Exception

class ListRepository(
    val localDataSource: LocalDataSource,
    val remoteDataSource: RemoteDataSource
) {

    suspend fun getList(hash: String, uid: Int): List<Liste> {
        return try {

            remoteDataSource.getLists(hash, uid).also {
                localDataSource.saveOrUpdateLists(it)
            }

        } catch (e: Exception) {
            localDataSource.getLists(uid)
        }
    }

    suspend fun getItem(hash: String, lid: Int): List<Item> {
        return try {

            remoteDataSource.getItems(hash, lid).also {
                localDataSource.saveOrUpdateItems(it)
            }

        } catch (e: Exception) {
            localDataSource.getItems(lid)
        }
    }

    companion object {
        fun newInstance(application: Application): ListRepository {
            return ListRepository(
                localDataSource = LocalDataSource(application),
                remoteDataSource = RemoteDataSource()
            )
        }
        private const val CAT = "TP3"
    }


}