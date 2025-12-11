package Data

import Entity.HarvestRecord
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseHarvestDataManager: IDataManager<HarvestRecord> {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("harvests")

    override fun add(item: HarvestRecord, onResult: (Boolean, String?) -> Unit) {
        if (item.ID.isEmpty()) {
            item.ID = collection.document().id
        }
        collection.document(item.ID)
            .set(item)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    override fun update(item: HarvestRecord, onResult: (Boolean, String?) -> Unit) {
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

    override fun getById(id: String, onResult: (HarvestRecord?) -> Unit) {
        collection.document(id)
            .get()
            .addOnSuccessListener { snap ->
                onResult(snap.toObject(HarvestRecord::class.java))
            }
            .addOnFailureListener { onResult(null) }
    }

    override fun getAll(onResult: (List<HarvestRecord>) -> Unit) {
        collection
            .get()
            .addOnSuccessListener { result ->
                val list = result.mapNotNull { it.toObject(HarvestRecord::class.java) }
                onResult(list)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }
}
