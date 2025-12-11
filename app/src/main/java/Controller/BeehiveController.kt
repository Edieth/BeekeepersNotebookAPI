package Controller
import Data.FirebaseBeehiveDataManager
import Data.IDataManager
import Entity.Beehive
import android.content.Context
import cr.ac.utn.beekeepersnotebook.R

class BeehiveController {
    private var dataManager: IDataManager<Beehive> = FirebaseBeehiveDataManager()
    private var context: Context

    constructor(context: Context){
        this.context=context
    }

    fun addBeehive(hive: Beehive, onResult: (Boolean, String?) -> Unit) {
        dataManager.add(hive) { ok, error ->
            if (!ok) {
                onResult(false, context.getString(R.string.ErrorMsgAdd) + " " + (error ?: ""))
            } else {
                onResult(true, null)
            }
        }
    }

    fun updateBeehive(hive: Beehive, onResult: (Boolean, String?) -> Unit) {
        dataManager.update(hive) { ok, error ->
            if (!ok) {
                onResult(false, context.getString(R.string.ErrorMsgUpdate) + " " + (error ?: ""))
            } else {
                onResult(true, null)
            }
        }
    }

    fun deleteBeehive(id: String, onResult: (Boolean, String?) -> Unit) {
        dataManager.delete(id) { ok, error ->
            if (!ok) {
                onResult(false, context.getString(R.string.ErrorMsgRemove) + " " + (error ?: ""))
            } else {
                onResult(true, null)
            }
        }
    }

    fun getAll(onResult: (List<Beehive>) -> Unit) {
        dataManager.getAll { list -> onResult(list) }
    }

    fun getById(id: String, onResult: (Beehive?) -> Unit) {
        dataManager.getById(id, onResult)
    }


    }
