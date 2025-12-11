package Controller

import Data.FirebaseHarvestDataManager
import cr.ac.utn.beekeepersnotebook.R
import Data.IDataManager
import Entity.HarvestRecord
import android.content.Context


class HarvestController {
    private var dataManager: IDataManager<HarvestRecord> = FirebaseHarvestDataManager()
    private var context: Context

    constructor(context: Context){
        this.context=context
    }

    fun addHarvest(h: HarvestRecord, onResult: (Boolean, String?) -> Unit) {
        dataManager.add(h) { ok, error ->
            if (!ok) onResult(false, context.getString(R.string.ErrorMsgAdd) + " " + (error ?: ""))
            else onResult(true, null)
        }
    }

    fun updateHarvest(h: HarvestRecord, onResult: (Boolean, String?) -> Unit) {
        dataManager.update(h) { ok, error ->
            if (!ok) onResult(false, context.getString(R.string.ErrorMsgUpdate) + " " + (error ?: ""))
            else onResult(true, null)
        }
    }

    fun deleteHarvest(id: String, onResult: (Boolean, String?) -> Unit) {
        dataManager.delete(id) { ok, error ->
            if (!ok) onResult(false, context.getString(R.string.ErrorMsgRemove) + " " + (error ?: ""))
            else onResult(true, null)
        }
    }

    fun getAll(onResult: (List<HarvestRecord>) -> Unit) {
        dataManager.getAll { list -> onResult(list) }
    }

    fun getById(id: String, onResult: (HarvestRecord?) -> Unit) {
      try {
          return dataManager.getById(id, onResult)
      } catch (e: Exception) {
          throw Exception(context.getString(R.string.ErrorMsgGetById))
      }
    }

    }



