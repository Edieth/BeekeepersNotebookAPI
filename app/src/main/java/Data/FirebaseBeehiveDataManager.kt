package Data
import Entity.Beehive
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseBeehiveDataManager : IDataManager<Beehive> {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("beehives")

    override fun add(item: Beehive, onResult: (Boolean, String?) -> Unit) {
        if (item.ID.isEmpty()) {
            item.ID = collection.document().id
        }
        collection.document(item.ID)
            .set(item)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    override fun update(item: Beehive, onResult: (Boolean, String?) -> Unit) {
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

    override fun getById(id: String, onResult: (Beehive?) -> Unit) {
        collection.document(id)
            .get()
            .addOnSuccessListener { snap ->
                onResult(snap.toObject(Beehive::class.java))
            }
            .addOnFailureListener { onResult(null) }
    }

    override fun getAll(onResult: (List<Beehive>) -> Unit) {
        collection
            .get()
            .addOnSuccessListener { result ->
                val list = result.mapNotNull { it.toObject(Beehive::class.java) }
                onResult(list)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    override fun getByFullName(fullname: String, onResult: (Beehive?) -> Unit) {
        collection
            .whereEqualTo("name", fullname)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                val hive = result.documents.firstOrNull()?.toObject(Beehive::class.java)
                onResult(hive)
            }
            .addOnFailureListener { onResult(null) }
    }
}
