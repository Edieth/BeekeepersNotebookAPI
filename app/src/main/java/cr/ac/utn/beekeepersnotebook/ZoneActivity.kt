package cr.ac.utn.beekeepersnotebook

import Adapter.ZoneAdapter
import Controller.ZoneController
import Entity.Zone
import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth


class ZoneActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var btnAddZone: Button
    private lateinit var adapter: ZoneAdapter
    private lateinit var controller: ZoneController

    private val zones = mutableListOf<Zone>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_zone)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        controller = ZoneController(this)

        recycler = findViewById(R.id.recyclerZones)
        btnAddZone = findViewById(R.id.btnAddZone)

        adapter = ZoneAdapter(
            zones,
            onItemClick = { zone -> openBeehives(zone) },
            onItemLongClick = { zone -> showZoneOptions(zone) }
        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        btnAddZone.setOnClickListener { addZoneDialog() }

        loadZones()
    }
    private fun loadZones() {
        controller.getAllZones { list ->
            zones.clear()
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            zones.addAll(list.filter { it.PersonID == uid })
            adapter.notifyDataSetChanged()
        }
    }

    private fun openBeehives(zone: Zone) {
        val intent = Intent(this, ZoneMenuActivity::class.java)
        intent.putExtra("ZONE_ID", zone.ID)
        intent.putExtra("ZONE_NAME", zone.Name)
        startActivity(intent)
    }

    private fun showZoneOptions(zone: Zone) {
        val options = arrayOf("Editar", "Eliminar")
        AlertDialog.Builder(this)
            .setTitle("Zona: ${zone.Name}")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> editZoneDialog(zone)
                    1 -> deleteZone(zone)
                }
            }
            .show()
    }

    private fun addZoneDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_zone, null)
        val txtName = view.findViewById<EditText>(R.id.txtZoneName)

        AlertDialog.Builder(this)
            .setTitle("Nueva zona")
            .setView(view)
            .setPositiveButton(R.string.TextSave) { _, _ ->
                val name = txtName.text.toString().trim()
                if (name.isEmpty()) {
                    Toast.makeText(this, "Ingrese un nombre", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                val zone = Zone().apply {
                    Name = name
                    PersonID = uid
                }

                controller.addZone(zone) { ok, msg ->
                    if (ok) {
                        Toast.makeText(this, "Zona guardada", Toast.LENGTH_SHORT).show()
                        loadZones()
                    } else {
                        Toast.makeText(this, msg ?: "Error al guardar zona", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }


    private fun editZoneDialog(zone: Zone) {
        val view = layoutInflater.inflate(R.layout.dialog_zone, null)
        val txtName = view.findViewById<EditText>(R.id.txtZoneName)
        txtName.setText(zone.Name)

        AlertDialog.Builder(this)
            .setTitle("Editar zona")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val name = txtName.text.toString().trim()
                if (name.isEmpty()) {
                    Toast.makeText(this, "Ingrese un nombre", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                zone.Name = name
                controller.updateZone(zone) { ok, msg ->
                    if (ok) {
                        Toast.makeText(this, "Zona actualizada", Toast.LENGTH_SHORT).show()
                        loadZones()
                    } else {
                        Toast.makeText(
                            this,
                            msg ?: "Error al actualizar zona",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteZone(zone: Zone) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar zona")
            .setMessage("¿Desea eliminar la zona ${zone.Name}?")
            .setPositiveButton("Eliminar") { _, _ ->
                controller.deleteZone(zone.ID) { ok, msg ->
                    if (ok) {
                        Toast.makeText(this, "Zona eliminada", Toast.LENGTH_SHORT).show()
                        loadZones()
                    } else {
                        Toast.makeText(this, msg ?: "Error al eliminar zona", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}