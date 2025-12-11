package Data

import Entity.Person
import com.google.firebase.firestore.FirebaseFirestore

class FirebasePersonDataManager : IDataManager<Person>{

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("person")

    override fun add(item: Person, onResult: (Boolean, String?) -> Unit) {
        if (item.ID.isEmpty()) {
            item.ID = collection.document().id
        }
        collection.document(item.ID)
            .set(item)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    override fun update(item: Person, onResult: (Boolean, String?) -> Unit) {
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

    override fun getById(id: String, onResult: (Person?) -> Unit) {
        collection.document(id)
            .get()
            .addOnSuccessListener { snap ->
                onResult(snap.toObject(Person::class.java))
            }
            .addOnFailureListener { onResult(null) }
    }
        override fun getAll(onResult: (List<Person>) -> Unit) {
            collection
                .get()
                .addOnSuccessListener { result ->

                    val list = result.mapNotNull { it.toObject(Person::class.java) }
                    onResult(list)
                }
                .addOnFailureListener {
                    onResult(emptyList())
                }
        }

    override fun getByFullName(name: String, onResult: (Person?) -> Unit) {
       collection
            .whereEqualTo("name", name) // o "fullName", según tu estructura
            .get()
            .addOnSuccessListener { result ->
                val person = result.documents.firstOrNull()?.toObject(Person::class.java)
                onResult(person)
            }
            .addOnFailureListener { onResult(null) }
    }


}
