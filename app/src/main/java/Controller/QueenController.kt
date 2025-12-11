package Controller

import Data.FirebaseQueenDataManager
import Data.IDataManager
import Entity.Queen
import android.content.Context
import cr.ac.utn.beekeepersnotebook.R

class QueenController(private val context: Context) {

    private var dataManager: IDataManager<Queen> = FirebaseQueenDataManager()

    fun addQueen(q: Queen, onResult: (Boolean, String?) -> Unit) {
        dataManager.add(q) { ok, error ->
            if (!ok) onResult(false, context.getString(R.string.ErrorMsgAdd) + " " + (error ?: ""))
            else onResult(true, null)
        }
    }

    fun updateQueen(q: Queen, onResult: (Boolean, String?) -> Unit) {
        dataManager.update(q) { ok, error ->
            if (!ok) onResult(false, context.getString(R.string.ErrorMsgUpdate) + " " + (error ?: ""))
            else onResult(true, null)
        }
    }

    fun deleteQueen(id: String, onResult: (Boolean, String?) -> Unit) {
        dataManager.delete(id) { ok, error ->
            if (!ok) onResult(false, context.getString(R.string.ErrorMsgRemove) + " " + (error ?: ""))
            else onResult(true, null)
        }
    }

    fun getAll(onResult: (List<Queen>) -> Unit) {
        dataManager.getAll { list -> onResult(list) }
    }

    fun getById(id: String, onResult: (Queen?) -> Unit) {
        dataManager.getById(id, onResult)
    }
}
