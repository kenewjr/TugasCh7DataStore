package and5.abrar.estafet.datastore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserManager(context: Context) {
    private val dataStore : DataStore<Preferences> = context.createDataStore("user_login")

    companion object{
        val USERNAME = preferencesKey<String>("username")
    }
    suspend fun savedata(username:String){
        dataStore.edit{
            it[USERNAME] = username
        }
    }
    val userName : Flow<String> = dataStore.data.map {
        it[USERNAME] ?:""
    }
    suspend fun hapusData(){
        dataStore.edit {
            it.clear()
        }

    }
}