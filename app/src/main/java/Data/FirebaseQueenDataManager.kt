package Data

import Entity.Queen
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseQueenDataManager: IDataManager<Queen> {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("queens")

    override fun add(item: Queen, onResult: (Boolean, String?) -> Unit) {
        if (item.ID.isEmpty()) {
            item.ID = collection.document().id
        }
        collection.document(item.ID)
            .set(item)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    override fun update(item: Queen, onResult: (Boolean, String?) -> Unit) {
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

    override fun getById(id: String, onResult: (Queen?) -> Unit) {
        collection.document(id)
            .get()
            .addOnSuccessListener { snap ->
                val queen = snap.toObject(Queen::class.java)
                onResult(queen)
            }
            .addOnFailureListener { onResult(null) }
    }

    override fun getAll(onResult: (List<Queen>) -> Unit) {
        collection
            .get()
            .addOnSuccessListener { result ->
                val list = result.mapNotNull { it.toObject(Queen::class.java) }
                onResult(list)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }
    override fun getByFullName(name: String, onResult: (Queen?) -> Unit) {
        collection
            .whereEqualTo("type", name)
            .get()
            .addOnSuccessListener { result ->
                val queen = result.documents.firstOrNull()?.toObject(Queen::class.java)
                onResult(queen)
            }
            .addOnFailureListener { onResult(null) }
    }
}