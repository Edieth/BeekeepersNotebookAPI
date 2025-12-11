package Data

import Entity.Person
import Entity.Zone

interface IDataManager<T>{
    fun add(item: T, onResult: (Boolean, String?) -> Unit)
    fun update(item: T, onResult: (Boolean, String?) -> Unit)
    fun delete(id: String, onResult: (Boolean, String?) -> Unit)
    fun getById(id: String, onResult: (T?) -> Unit)
    fun getAll(onResult: (List<T>) -> Unit)
    fun getByFullName(name: String, onResult: (T?) -> Unit) {
        onResult(null)
    }
}


