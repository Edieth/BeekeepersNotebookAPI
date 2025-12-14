package cr.ac.utn.beekeepersnotebook

import Adapter.InventoryAdapter
import Controller.InventoryController
import Entity.InventoryItem
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class InventoryActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    private lateinit var btnAddItemInventory: Button
    private lateinit var adapter: InventoryAdapter
    private lateinit var controller: InventoryController

    private val items = mutableListOf<InventoryItem>()
    private var personId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inventory)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        personId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        Toast.makeText(this, "UID actual: $personId", Toast.LENGTH_SHORT).show()

        controller = InventoryController(this)

        recycler = findViewById(R.id.recyclerInventory)
        btnAddItemInventory = findViewById(R.id.btnAddItemInventory)

        adapter = InventoryAdapter(
            items,
            onItemClick = { item -> editItemDialog(item) },
            onItemLongClick = { item -> deleteItem(item) }
        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        btnAddItemInventory.setOnClickListener { addItemDialog() }


        loadItems()
    }

    private fun loadItems() {
        controller.getAllByPerson(personId) { list ->
            items.clear()
            items.addAll(list)
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Inventario cargado: ${items.size} items", Toast.LENGTH_SHORT).show() }
    }

    private fun addItemDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_inventory, null)
        val txtName = view.findViewById<EditText>(R.id.txtItemName)
        val txtTotal = view.findViewById<EditText>(R.id.txtItemTotal)
        val txtDataType = view.findViewById<EditText>(R.id.txtItemDataType)

        AlertDialog.Builder(this)
            .setTitle("Nuevo material")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                try {
                    val name = txtName.text.toString().trim()
                    val tStr = txtTotal.text.toString().trim()
                    val typeStr = txtDataType.text.toString().trim()

                    if (name.isEmpty() || tStr.isEmpty()) {
                        Toast.makeText(
                            this,
                            "Nombre y total son obligatorios",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setPositiveButton
                    }

                    val total = tStr.toIntOrNull()
                    if (total == null) {
                        Toast.makeText(
                            this,
                            "El total debe ser un número",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setPositiveButton
                    }

                    val inventoryDataType = typeStr

                    val item = InventoryItem().apply {
                        PersonID = personId
                        Name = name
                        InventoryTotalQuantity = total
                        InventoryDataType = inventoryDataType
                    }

                    controller.addItem(item) { ok, msg ->
                        if (ok) {
                            Toast.makeText(this, "Material guardado", Toast.LENGTH_SHORT).show()
                            loadItems()
                        } else {
                            Toast.makeText(
                                this,
                                msg ?: "Error al guardar material",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    // Si algo raro pasa, que NO se caiga la app
                    e.printStackTrace()
                    Toast.makeText(
                        this,
                        "Error inesperado al guardar: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }


    private fun editItemDialog(item: InventoryItem) {
        val view = layoutInflater.inflate(R.layout.dialog_inventory, null)
        val txtName = view.findViewById<EditText>(R.id.txtItemName)
        val txtTotal = view.findViewById<EditText>(R.id.txtItemTotal)
        val txtDataType = view.findViewById<EditText>(R.id.txtItemDataType)

        txtName.setText(item.Name)
        txtTotal.setText(item.InventoryTotalQuantity.toString())
        txtDataType.setText(item.InventoryDataType)

        AlertDialog.Builder(this)
            .setTitle("Editar material")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val name = txtName.text.toString().trim()
                val tStr = txtTotal.text.toString().trim()
                val typeStr = txtDataType.text.toString().trim()

                if (name.isEmpty() || tStr.isEmpty()) {
                    Toast.makeText(this, "Nombre y total son obligatorios", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                item.Name = name
                item.InventoryTotalQuantity = tStr.toIntOrNull() ?: 0
                item.InventoryDataType = typeStr

                controller.updateItem(item) { ok, msg ->
                    if (ok) {
                        Toast.makeText(this, "Material actualizado", Toast.LENGTH_SHORT).show()
                        loadItems()
                    } else {
                        Toast.makeText(this, msg ?: "Error al actualizar material", Toast.LENGTH_LONG).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteItem(item: InventoryItem) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar material")
            .setMessage("¿Desea eliminar ${item.Name}?")
            .setPositiveButton("Eliminar") { _, _ ->
                controller.deleteItem(item.ID) { ok, msg ->
                    if (ok) {
                        Toast.makeText(this, "Material eliminado", Toast.LENGTH_SHORT).show()
                        loadItems()
                    } else {
                        Toast.makeText(this, msg ?: "Error al eliminar material", Toast.LENGTH_LONG).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}