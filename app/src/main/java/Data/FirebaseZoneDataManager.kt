package Data
import Entity.Zone
import com.google.firebase.firestore.FirebaseFirestore
import Data.IDataManager
import Entity.Person

class FirebaseZoneDataManager : IDataManager<Zone> {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("zones")

    override fun add(item: Zone, onResult: (Boolean, String?) -> Unit) {
        if (item.ID.isEmpty()) {
            item.ID = collection.document().id
        }
        collection.document(item.ID)
            .set(item)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    override fun update(item: Zone, onResult: (Boolean, String?) -> Unit) {
        collection.document(item.ID)
            .set(item)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    override fun delete(id: String, onResult: (Boolean, String?) -> Unit) {
        collection.document(id)
            .delete()
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    override fun getById(id: String, onResult: (Zone?) -> Unit) {
        collection.document(id)
            .get()
            .addOnSuccessListener { snap ->
                onResult(snap.toObject(Zone::class.java))
            }
            .addOnFailureListener { onResult(null) }
    }

    override fun getAll(onResult: (List<Zone>) -> Unit) {
        collection.get()
            .addOnSuccessListener { result ->
                val list = result.mapNotNull { it.toObject(Zone::class.java) }
                onResult(list)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    override fun getByFullName(name: String, onResult: (Zone?) -> Unit) {
        collection
            .whereEqualTo("name", name)
            .get()
            .addOnSuccessListener { result ->
                val zone = result.documents.firstOrNull()?.toObject(Zone::class.java)
                onResult(zone)
            }
            .addOnFailureListener { onResult(null) }
    }

}