package Controller
import Data.FirebaseZoneDataManager
import Data.IDataManager
import Entity.Person
import Entity.Zone
import android.content.Context
import cr.ac.utn.beekeepersnotebook.R

class ZoneController(context: Context) {

    private var dataManager: IDataManager<Zone> = FirebaseZoneDataManager()
    private var context: Context = context
    fun addZone(zone: Zone, onResult: (Boolean, String?) -> Unit) {
        dataManager.add(zone) { ok, error ->
            if (!ok) {
                onResult(false, context.getString(R.string.ErrorMsgAdd) + " $error")
            } else {
                onResult(true, null)
            }
        }
    }

    fun updateZone(zone: Zone, onResult: (Boolean, String?) -> Unit) {
        dataManager.update(zone) { ok, error ->
            if (!ok) {
                onResult(false, context.getString(R.string.ErrorMsgUpdate) + " $error")
            } else {
                onResult(true, null)
            }
        }
    }

    fun deleteZone(id: String, onResult: (Boolean, String?) -> Unit) {
        dataManager.getById(id) { zone ->
            if (zone == null) {

                onResult(false, context.getString(R.string.MsgDataNotFound))
                return@getById
            }

            dataManager.delete(id) { ok, error ->
                if (!ok) {
                    onResult(false, context.getString(R.string.ErrorMsgRemove) + " ${error ?: ""}")
                } else {
                    onResult(true, null)
                }
            }
        }
    }

    fun getById(id: String, onResult: (Zone?) -> Unit) {
        try {
            return dataManager.getById(id, onResult)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgGetById))
        }
    }

    fun getAllZones(onResult: (List<Zone>) -> Unit) {
       try {
           return dataManager.getAll { list -> onResult(list)
           }
       } catch (e: Exception) {
           throw Exception(context.getString(R.string.ErrorMsgGetAll))
       }
    }
    fun getByFullName(fullname: String, onResult: (Zone?) -> Unit)  {
        try {
            dataManager.getByFullName(fullname) { zone ->
                onResult(zone)
            }
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgGetById))
        }
    }


}




