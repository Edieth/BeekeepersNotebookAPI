package Data

import Entity.InventoryItem
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseInventoryDataManager: IDataManager<InventoryItem> {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("inventory")

    override fun add(item: InventoryItem, onResult: (Boolean, String?) -> Unit) {
        if (item.ID.isEmpty()) {
            item.ID = collection.document().id
        }
        collection.document(item.ID)
            .set(item)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    override fun update(item: InventoryItem, onResult: (Boolean, String?) -> Unit) {
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

    override fun getById(id: String, onResult: (InventoryItem?) -> Unit) {
        collection.document(id)
            .get()
            .addOnSuccessListener { snap ->
                onResult(snap.toObject(InventoryItem::class.java))
            }
            .addOnFailureListener { onResult(null) }
    }

    override fun getAll(onResult: (List<InventoryItem>) -> Unit) {
        collection
            .get()
            .addOnSuccessListener { result ->
                try {
                    val list = result.mapNotNull { it.toObject(InventoryItem::class.java) }
                    onResult(list)
                } catch (e: Exception) {
                    e.printStackTrace()
                    onResult(emptyList())
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                onResult(emptyList())
            }
    }

    override fun getByFullName(name: String, onResult: (InventoryItem?) -> Unit) {
        collection
            .whereEqualTo("name", name)  // campo 'name' del InventoryItem
            .get()
            .addOnSuccessListener { result ->
                val item = result.documents.firstOrNull()?.toObject(InventoryItem::class.java)
                onResult(item)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
    fun getAllByPerson(personId: String, onResult: (List<InventoryItem>) -> Unit) {
        collection
            .whereEqualTo("personID", personId)   // <-- usa exactamente el nombre del campo
            .get()
            .addOnSuccessListener { result ->
                try {
                    val list = result.mapNotNull { it.toObject(InventoryItem::class.java) }
                    onResult(list)
                } catch (e: Exception) {
                    e.printStackTrace()
                    onResult(emptyList())
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                onResult(emptyList())
            }
    }



}
