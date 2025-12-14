package Controller
import Data.FirebaseInventoryDataManager
import android.content.Context
import Data.IDataManager
import Entity.InventoryItem
import java.lang.Exception
import cr.ac.utn.beekeepersnotebook.R



class InventoryController {
    private var dataManager: IDataManager<InventoryItem> = FirebaseInventoryDataManager()
    private  var context: Context

    constructor(context: Context){
        this.context=context
    }
    fun addItem(item: InventoryItem, onResult: (Boolean, String?) -> Unit) {
        dataManager.add(item) { ok, error ->
            if (!ok) onResult(false, context.getString(R.string.ErrorMsgAdd) + " " + (error ?: ""))
            else onResult(true, null)
        }
    }

    fun updateItem(item: InventoryItem, onResult: (Boolean, String?) -> Unit) {
        dataManager.update(item) { ok, error ->
            if (!ok) onResult(false, context.getString(R.string.ErrorMsgUpdate) + " " + (error ?: ""))
            else onResult(true, null)
        }
    }

    fun deleteItem(id: String, onResult: (Boolean, String?) -> Unit) {
        dataManager.delete(id) { ok, error ->
            if (!ok) onResult(false, context.getString(R.string.ErrorMsgRemove) + " " + (error ?: ""))
            else onResult(true, null)
        }
    }

    fun getAll(onResult: (List<InventoryItem>) -> Unit) {
        dataManager.getAll { list -> onResult(list) }
    }

    fun getById(id: String, onResult: (InventoryItem?) -> Unit) {
        dataManager.getById(id, onResult)
    }
    fun getAllByPerson(personId: String, onResult: (List<InventoryItem>) -> Unit) {
        (dataManager as? FirebaseInventoryDataManager)
            ?.getAllByPerson(personId, onResult)
            ?: getAll(onResult)
    }


}